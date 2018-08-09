package com.guohanhealth.shop.ui.main.fragment.mine.returngoods;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseFragment;
import com.guohanhealth.shop.bean.GoodsCollectInfo;
import com.guohanhealth.shop.bean.RefundlistInfo;
import com.guohanhealth.shop.custom.EmptyView;
import com.guohanhealth.shop.event.OnFragmentInteractionListener;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.ApiService;
import com.guohanhealth.shop.http.HttpErrorCode;
import com.guohanhealth.shop.ui.goods.goodsdetailed.activity.GoodsDetailsActivity;
import com.guohanhealth.shop.ui.main.activity.MainActivity;
import com.guohanhealth.shop.ui.main.fragment.mine.collect.GoodsCollectFragment;
import com.guohanhealth.shop.utils.Utils;
import com.guohanhealth.shop.utils.engine.GlideEngine;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class RefundFragment extends BaseFragment<ReturnPersenter, ReturnModel> implements ReturnView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;
    @BindView(R.id.emptyview)
    EmptyView emptyview;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Adapters mAdapters;

    public RefundFragment() {
        mList = new ArrayList<>();
    }

    public static RefundFragment newInstance(String param1, String param2) {
        RefundFragment fragment = new RefundFragment();
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
        return R.layout.recycleview;
    }

    boolean isLoad;
    public List<RefundlistInfo.DataBean> mList;

    @Override
    protected void initView(Bundle savedInstanceState) {
        smartrefreshlayout.setRefreshHeader(new ClassicsHeader(mContext));
        smartrefreshlayout.setOnRefreshListener(refreshLayout -> {
            curpage = 1;
            isLoad = false;
            getData();
        });
        smartrefreshlayout.setOnLoadMoreListener(refreshLayout -> {
            if (Boolean.valueOf(App.getApp().Hasmore()) && App.getApp().getPage_total() > curpage) {
                isLoad = true;
                curpage++;
                getData();
            } else {
                stopReOrLoad();
            }
        });
        emptyview.setEmpty("暂无退款商品", "");
        emptyview.setVisibility(View.VISIBLE);
        emptyview.setClickListener(v -> {
            readyGoThenKill(MainActivity.class);
        });
        mAdapters = new Adapters(R.layout.refund_list_item, mList);
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerview.setAdapter(mAdapters);
        mAdapters.setOnItemClickListener((a, v, i) -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.REFUND_ID, mList.get(i).refund_id);
            readyGo(ReturnDetailsActivity.class, bundle);
        });
    }

    private void getData() {
        mPresenter.geRefundList(App.getApp().getKey(), curpage + "");

    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
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


    class Adapters extends BaseQuickAdapter<RefundlistInfo.DataBean, BaseViewHolder> {

        public Adapters(int layoutResId, @Nullable List<RefundlistInfo.DataBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, RefundlistInfo.DataBean item) {
            helper.setText(R.id.store_name, item.store_name);
            helper.setText(R.id.store_stauts, item.seller_state);
            helper.setText(R.id.text1, item.add_time);
            helper.setText(R.id.text2, "￥ " + item.refund_amount);
            LinearLayout layout = helper.getView(R.id.addlistview);
            layout.removeAllViews();
            for (int i = 0; i < item.goods_list.size(); i++) {
                View view = getView(R.layout.refund_goodslist_item);
                TextView textView = getView(view, R.id.text);
                ImageView img = getView(view, R.id.img);
                textView.setText(item.goods_list.get(i).goods_name);
                GlideEngine.getInstance().loadImage(mContext, img, item.goods_list.get(i).goods_img_360);
                layout.addView(view);
                view.setOnClickListener(v->{
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.REFUND_ID,item.refund_id);
                    readyGo(ReturnDetailsActivity.class, bundle);
                });
            }

        }
    }

    int curpage = 1;

    public void onButtonPressed(String key, String value) {
        if (mListener != null) {
            mListener.doSomeThing(key, value);
        }
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void faild(String msg) {
        showToast(msg);
        stopReOrLoad();
        recyclerview.setVisibility(View.GONE);
        emptyview.setVisibility(View.VISIBLE);
    }

    @Override
    public void getData(Object data) {
        stopReOrLoad();
        RefundlistInfo info = (RefundlistInfo) data;
        if (info != null) {
            if (!isLoad) {
                mList.clear();
            }
            if (Utils.isEmpty(info.refund_list)) {
                recyclerview.setVisibility(View.VISIBLE);
                emptyview.setVisibility(View.GONE);
                mList.addAll(info.refund_list);
            } else {
                recyclerview.setVisibility(View.GONE);
                emptyview.setVisibility(View.VISIBLE);
            }
            mAdapters.notifyDataSetChanged();
        }
    }
}
