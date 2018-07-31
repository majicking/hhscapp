package com.guohanhealth.shop.ui.main.fragment.mine.property.voucher;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.base.BaseFragment;
import com.guohanhealth.shop.bean.VoucherInfo;
import com.guohanhealth.shop.custom.EmptyView;
import com.guohanhealth.shop.event.OnFragmentInteractionListener;
import com.guohanhealth.shop.ui.main.fragment.mine.property.PredepositModel;
import com.guohanhealth.shop.utils.Utils;
import com.guohanhealth.shop.utils.engine.GlideEngine;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class VoucherListFragment extends BaseFragment<VoucherPersenter, PredepositModel> implements VoucherView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.emptyview)
    EmptyView emptyview;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;
    List<VoucherInfo.DataBean> mList;
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Adapters mAdapters;

    public VoucherListFragment() {
        mList = new ArrayList<>();
    }

    public static VoucherListFragment newInstance(String param1, String param2) {
        VoucherListFragment fragment = new VoucherListFragment();
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
    int curpage = 1;
    @Override
    protected void initView(Bundle savedInstanceState) {

        smartrefreshlayout.setRefreshHeader(new ClassicsHeader(mContext));
        smartrefreshlayout.setOnRefreshListener(refreshLayout -> {
            curpage = 1;
            isLoad = false;
            mPresenter.voucherList(App.getApp().getKey(), curpage + "");
        });
        smartrefreshlayout.setOnLoadMoreListener(refreshLayout -> {
            if (Boolean.valueOf(App.getApp().Hasmore()) && App.getApp().getPage_total() > curpage) {
                isLoad = true;
                curpage++;
                mPresenter.voucherList(App.getApp().getKey(), curpage + "");
            } else {
                stopReOrLoad();
            }
        });
        emptyview.setEmpty("暂无代金卷信息", "赶紧去领取代金卷吧");
        mAdapters = new Adapters(R.layout.vouter_item, mList);
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerview.setAdapter(mAdapters);
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
    public void getData(Object data) {
        stopReOrLoad();
        VoucherInfo info = (VoucherInfo) data;
        if (info != null) {
            if (!isLoad) {
                mList.clear();
            }
            if (Utils.isEmpty(info.voucher_list)) {
                recyclerview.setVisibility(View.VISIBLE);
                emptyview.setVisibility(View.GONE);
                mList.addAll(info.voucher_list);
            } else {
                recyclerview.setVisibility(View.GONE);
                emptyview.setVisibility(View.VISIBLE);
            }
            mAdapters.notifyDataSetChanged();
        }
    }

    @Override
    public void getResult(String s) {

    }

    class Adapters extends BaseQuickAdapter<VoucherInfo.DataBean, BaseViewHolder> {

        public Adapters(int layoutResId, @Nullable List<VoucherInfo.DataBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, VoucherInfo.DataBean item) {
            GlideEngine.getInstance().loadImage(mContext, helper.getView(R.id.img), item.store_avatar_url);
            helper.setText(R.id.text1, item.voucher_title);
            helper.setText(R.id.text2, "有效期:" + item.voucher_end_date_text);
            helper.setText(R.id.text3, "￥" + item.voucher_price);
            helper.setText(R.id.text4, "满" + item.voucher_limit + "使用");
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        mPresenter.voucherList(App.getApp().getKey(), curpage + "");
    }

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
        stopReOrLoad();
        showToast(msg);
        recyclerview.setVisibility(View.GONE);
        emptyview.setVisibility(View.VISIBLE);
    }


}
