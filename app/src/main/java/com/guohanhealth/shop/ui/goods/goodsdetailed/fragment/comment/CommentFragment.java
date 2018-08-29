package com.guohanhealth.shop.ui.goods.goodsdetailed.fragment.comment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.guohanhealth.shop.R;
import com.guohanhealth.shop.adapter.BaseRecyclerAdapter;
import com.guohanhealth.shop.adapter.BaseViewHolder;
import com.guohanhealth.shop.adapter.CommonAdapter;
import com.guohanhealth.shop.adapter.ViewHolder;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseFragment;
import com.guohanhealth.shop.bean.EvalInfo;
import com.guohanhealth.shop.bean.VoucherInfo;
import com.guohanhealth.shop.custom.CustomPopuWindow;
import com.guohanhealth.shop.custom.EmptyView;
import com.guohanhealth.shop.event.OnFragmentInteractionListener;
import com.guohanhealth.shop.ui.goods.GoodsModel;
import com.guohanhealth.shop.utils.Logutils;
import com.guohanhealth.shop.utils.StringUtil;
import com.guohanhealth.shop.utils.Utils;
import com.guohanhealth.shop.utils.engine.GlideEngine;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class CommentFragment extends BaseFragment<CommentPersenter, GoodsModel> implements CommentView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.gridview)
    GridView gridview;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;
    @BindView(R.id.emptyview)
    EmptyView emptyview;


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private BaseRecyclerAdapter<EvalInfo.Goods_eval_list, com.chad.library.adapter.base.BaseViewHolder> adapter;
    private boolean isLoad = false;

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
    private int curpage = 1;
    private String type = "";
    private String page = "10";
    private String[] title = {"全部", "好评", "中评", "差评", "晒图", "追加"};
    private int selectPosition = 0;
    private List<EvalInfo.Goods_eval_list> mList;

    @Override
    protected void initView(Bundle savedInstanceState) {
        goods_id = (String) onButtonPressed(Constants.GETGOODSID, "");
        emptyview.setEmptyText("期待您的评价哟");
        emptyview.setEmptyText1("评价占沙发");
        mList = new ArrayList();
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        smartrefreshlayout.setRefreshHeader(new ClassicsHeader(mContext));
        smartrefreshlayout.setOnRefreshListener(refreshLayout -> {
            curpage = 1;
            isLoad = false;
            getdata();
        });
        smartrefreshlayout.setOnLoadMoreListener(refreshLayout -> {
            if (Boolean.valueOf(App.getApp().Hasmore()) && App.getApp().getPage_total() > curpage) {
                isLoad = true;
                curpage++;
                getdata();
            } else {
                stopReOrLoad();
            }
        });
        gridview.setAdapter(new CommonAdapter<String>(mContext, Arrays.asList(title), R.layout.comment_item) {
            @Override
            public void convert(ViewHolder viewHolder, String item, int position, View convertView, ViewGroup parentViewGroup) {
                TextView textView = viewHolder.getView(R.id.text);
                textView.setText(item);
                if (position == selectPosition) {
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
            selectPosition = position;
        });
        adapter = new BaseRecyclerAdapter<EvalInfo.Goods_eval_list, com.chad.library.adapter.base.BaseViewHolder>(mContext, R.layout.comment_list_item, mList) {
            @Override
            public void convert(BaseViewHolder holder, EvalInfo.Goods_eval_list s) {
                holder.setText(R.id.name, Utils.getString(s.geval_frommembername));
                holder.setText(R.id.time, Utils.getString(s.geval_addtime_date));
                holder.setText(R.id.content, Utils.getString(s.geval_content));
                LinearLayout layout = holder.getView(R.id.ratinglayout);
                LinearLayout againView = holder.getView(R.id.againView);
                RecyclerView recyclerView1 = holder.getView(R.id.recycleview1);
                RecyclerView recyclerView2 = holder.getView(R.id.recycleview2);
                GlideEngine.getInstance().loadImage(mContext, holder.getView(R.id.img), s.member_avatar);
                layout.removeAllViews();
                if (Utils.isEmpty(s.geval_image_240)) {
                    recyclerView1.setLayoutManager(new GridLayoutManager(mContext, 5));
                    setImageAdapter(recyclerView1, s.geval_image_240, s.geval_image_1024);
                }
                if (Utils.isEmpty(s.geval_content_again)) {
                    againView.setVisibility(View.VISIBLE);
                    if (Utils.isEmpty(s.geval_image_240)) {
                        recyclerView2.setLayoutManager(new GridLayoutManager(mContext, 5));
                        setImageAdapter(recyclerView2, s.geval_image_again_240, s.geval_image_again_1024);
                    }
                    holder.setText(R.id.timeagain, Utils.getString(s.geval_addtime_again_date));
                    holder.setText(R.id.contentagain, Utils.getString(s.geval_content_again));
                } else {
                    againView.setVisibility(View.GONE);
                }
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
        recyclerview.setAdapter(adapter);
    }

    class ImageAdapter extends BaseQuickAdapter<String, com.chad.library.adapter.base.BaseViewHolder> {

        public ImageAdapter(int layoutResId, @Nullable List<String> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(com.chad.library.adapter.base.BaseViewHolder helper, String item) {
            GlideEngine.getInstance().loadImage(mContext, helper.getView(R.id.img), item);
        }

    }

    public void setImageAdapter(RecyclerView recyclerview, List<String> data, List<String> bigdata) {
        ImageAdapter imageAdapter = new ImageAdapter(R.layout.img_item, data);
        recyclerview.setAdapter(imageAdapter);
        imageAdapter.setOnItemClickListener((adapter, view, position) -> {
            showBigImage(bigdata.get(position));
        });

    }

    public void showBigImage(String url) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView imageView = new ImageView(mContext);
        imageView.setLayoutParams(layoutParams);
        GlideEngine.getInstance().loadImage(mContext, imageView, url);
        Utils.getPopuWindown(mContext, imageView, Gravity.CENTER);
    }

    @Override
    public void onResume() {
        super.onResume();
        getdata();
    }

    public void getdata() {
        mPresenter.getGoodsEvaluateData(goods_id, curpage + "", type, page);
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
            Logutils.i("显示" + isVisibleToUser);
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
        stopReOrLoad();
        emptyview.setVisibility(View.VISIBLE);
        recyclerview.setVisibility(View.GONE);
    }

    private void stopReOrLoad() {
        if (smartrefreshlayout != null) {
            if (smartrefreshlayout.isLoading()) {
                smartrefreshlayout.finishLoadMore();
            }
            if (smartrefreshlayout.isRefreshing()) {
                smartrefreshlayout.finishRefresh();
            }
        }
    }

    @Override
    public void getData(List<EvalInfo.Goods_eval_list> list) {
        stopReOrLoad();
        if (!isLoad) {
            mList.clear();
        }
        if (Utils.isEmpty(list)) {
            recyclerview.setVisibility(View.VISIBLE);
            emptyview.setVisibility(View.GONE);
            mList.addAll(list);
        } else {
            recyclerview.setVisibility(View.GONE);
            emptyview.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();

    }


}
