package com.guohanhealth.shop.ui.goods.goodsdetailed.fragment.comment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.adapter.BaseRecyclerAdapter;
import com.guohanhealth.shop.adapter.BaseViewHolder;
import com.guohanhealth.shop.adapter.CommonAdapter;
import com.guohanhealth.shop.adapter.ViewHolder;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseFragment;
import com.guohanhealth.shop.bean.EvalInfo;
import com.guohanhealth.shop.custom.EmptyView;
import com.guohanhealth.shop.event.OnFragmentInteractionListener;
import com.guohanhealth.shop.ui.goods.GoodsModel;
import com.guohanhealth.shop.utils.Logutils;
import com.guohanhealth.shop.utils.StringUtil;
import com.guohanhealth.shop.utils.Utils;
import com.guohanhealth.shop.utils.engine.GlideEngine;

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
    @BindView(R.id.emptyview)
    EmptyView emptyview;

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
    private int steclectNumber = 0;

    @Override
    protected void initView(Bundle savedInstanceState) {
        goods_id = (String) onButtonPressed(Constants.GETGOODSID, "");
        gridview.setAdapter(new CommonAdapter<String>(mContext, Arrays.asList(title), R.layout.comment_item) {
            @Override
            public void convert(ViewHolder viewHolder, String item, int position, View convertView, ViewGroup parentViewGroup) {
                TextView textView = viewHolder.getView(R.id.text);
                textView.setText(item);
                if (position == steclectNumber) {
                    textView.setActivated(true);
                } else {
                    textView.setActivated(false);
                }
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
            steclectNumber = position;
        });
        adapter = new BaseRecyclerAdapter<EvalInfo.Goods_eval_list, com.chad.library.adapter.base.BaseViewHolder>(mContext, R.layout.comment_list_item, new ArrayList<>()) {
            @Override
            public void convert(BaseViewHolder holder, EvalInfo.Goods_eval_list s) {
                holder.setText(R.id.name, Utils.getString(s.geval_frommembername));
                holder.setText(R.id.time, Utils.getString(s.geval_addtime_date));
                holder.setText(R.id.content, Utils.getString(s.geval_content));
                LinearLayout layout = holder.getView(R.id.ratinglayout);
                GlideEngine.getInstance().loadImage(mContext, holder.getView(R.id.img), s.member_avatar);
                layout.removeAllViews();
                for (int i = 0; i < 5; i++) {
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(45, 45);
                    ImageView imageView = (ImageView) getView(R.layout.img_star);
                    imageView.setLayoutParams(layoutParams);
                    if ((i + 1) <= StringUtil.toInt(s.geval_scores)) {
                        imageView.setSelected(true);
                    } else {
                        imageView.setSelected(false);
                    }
                    layout.addView(imageView);
                }
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
            emptyview.setVisibility(View.GONE);
            adapter.upDataAdapter(list);
        } else {
            emptyview.setVisibility(View.VISIBLE);
            emptyview.setEmptyText("期待您的评价哟");
            emptyview.setEmptyText1("评价占沙发");
            adapter.upDataAdapter(new ArrayList<>());
        }
    }
}
