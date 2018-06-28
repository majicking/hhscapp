package com.majick.guohanhealth.ui.goods.goodsdetailed.fragment.comment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.majick.guohanhealth.R;
import com.majick.guohanhealth.adapter.BaseRecyclerAdapter;
import com.majick.guohanhealth.adapter.BaseViewHolder;
import com.majick.guohanhealth.adapter.CommonAdapter;
import com.majick.guohanhealth.adapter.ViewHolder;
import com.majick.guohanhealth.app.Constants;
import com.majick.guohanhealth.base.BaseFragment;
import com.majick.guohanhealth.bean.EvalInfo;
import com.majick.guohanhealth.event.OnFragmentInteractionListener;
import com.majick.guohanhealth.ui.goods.GoodsModel;
import com.majick.guohanhealth.utils.Logutils;
import com.majick.guohanhealth.utils.Utils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CommentFragment extends BaseFragment<CommentPersenter, GoodsModel> implements CommentView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Unbinder unbinder;
    @BindView(R.id.gridview)
    GridView gridview;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private BaseRecyclerAdapter<EvalInfo.Goods_eval_list, com.chad.library.adapter.base.BaseViewHolder> adapter;

    public CommentFragment() {
    }

    public static CommentFragment newInstance(String param1, String param2) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_comment;
    }

    private String goods_id;
    private String curpage = "1";
    private String type = "";
    private String page = "10";
    private String[] title = {"全部评价", "好评", "中评", "差评", "订单晒图", "追加评论"};

    @Override
    protected void initView(Bundle savedInstanceState) {
        goods_id = (String) onButtonPressed(Constants.GETGOODSID, "");
        gridview.setAdapter(new CommonAdapter<String>(mContext, Arrays.asList(title), R.layout.comment_item) {
            @Override
            public void convert(ViewHolder viewHolder, String item, int position, View convertView, ViewGroup parentViewGroup) {
                TextView textView = viewHolder.getView(R.id.text);
                textView.setText(item);
            }
        });
        gridview.setOnItemClickListener((parent, view, position, id) -> {
            for (int i = 0; i < gridview.getChildCount(); i++) {
                TextView textView = (TextView) gridview.getChildAt(i);
                if (textView.getText().toString().equals(title[position])) {
                    textView.setActivated(true);
                    type = position + "";
                    getdata();
                } else {
                    textView.setActivated(false);
                }
            }

        });
        ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(mContext)
                .setChildGravity(Gravity.TOP)
                .setScrollingEnabled(true)
                .setMaxViewsInRow(6)
                .setGravityResolver(position -> Gravity.CENTER).setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .withLastRow(true)
                .build();
        recycleview.setLayoutManager(chipsLayoutManager);
        adapter = new BaseRecyclerAdapter<EvalInfo.Goods_eval_list, com.chad.library.adapter.base.BaseViewHolder>(mContext, R.layout.comment_item, new ArrayList<>()) {
            @Override
            public void convert(BaseViewHolder holder, EvalInfo.Goods_eval_list s) {
                TextView t = holder.getView(R.id.text);
                t.setText(s.geval_content);
            }
        };
        recycleview.setAdapter(adapter);

    }


    @Override
    public void onResume() {
        super.onResume();
        getdata();
    }

    public void getdata() {
        mPresenter.getGoodsEvaluateData(goods_id, curpage, type, page);
    }

    public Object onButtonPressed(String key, Object value) {
        if (mListener != null) {
            return mListener.doSomeThing(key, value);
        }
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void faild(String msg) {
        showToast(msg);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void getData(List<EvalInfo.Goods_eval_list> list) {
        Logutils.i(list.toString());

        if (Utils.isEmpty(list)) {
            adapter.upDataAdapter(list);
        } else {
            adapter.upDataAdapter(new ArrayList<>());
        }
    }
}
