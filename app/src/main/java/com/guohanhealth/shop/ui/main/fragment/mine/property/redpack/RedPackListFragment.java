package com.guohanhealth.shop.ui.main.fragment.mine.property.redpack;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.base.BaseFragment;
import com.guohanhealth.shop.bean.RedPagcketInfo;
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

public class RedPackListFragment extends BaseFragment<RedPackPersenter, PredepositModel> implements RedPackView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.emptyview)
    EmptyView emptyview;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Adapters mAdapters;

    public RedPackListFragment() {
        mList = new ArrayList<>();
    }

    public static RedPackListFragment newInstance(String param1, String param2) {
        RedPackListFragment fragment = new RedPackListFragment();
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
    public List<RedPagcketInfo.DataBean> mList;

    @Override
    protected void initView(Bundle savedInstanceState) {

        smartrefreshlayout.setRefreshHeader(new ClassicsHeader(mContext));
        smartrefreshlayout.setOnRefreshListener(refreshLayout -> {
            curpage = 1;
            isLoad = false;
            mPresenter.redpacketList(App.getApp().getKey(), curpage + "");
        });
        smartrefreshlayout.setOnLoadMoreListener(refreshLayout -> {
            if (Boolean.valueOf(App.getApp().Hasmore()) && App.getApp().getPage_total() > curpage) {
                isLoad = true;
                curpage++;
                mPresenter.redpacketList(App.getApp().getKey(), curpage + "");
            } else {
                stopReOrLoad();
            }
        });
        emptyview.setEmpty("暂无红包记录", "赶紧去领取红包吧");
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
        RedPagcketInfo info = (RedPagcketInfo) data;
        if (info != null) {
            if (!isLoad) {
                mList.clear();
            }
            if (Utils.isEmpty(info.redpacket_list)) {
                recyclerview.setVisibility(View.VISIBLE);
                emptyview.setVisibility(View.GONE);
                mList.addAll(info.redpacket_list);
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

    class Adapters extends BaseQuickAdapter<RedPagcketInfo.DataBean, BaseViewHolder> {

        public Adapters(int layoutResId, @Nullable List<RedPagcketInfo.DataBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, RedPagcketInfo.DataBean item) {
            GlideEngine.getInstance().loadImage(mContext, helper.getView(R.id.img), item.rpacket_customimg_url);
            helper.setText(R.id.text1, item.rpacket_title);
            helper.setText(R.id.text2, "有效期:" + item.rpacket_end_date_text);
            helper.setText(R.id.text3, "￥" + item.rpacket_price);
            helper.setText(R.id.text4, "满" + item.rpacket_limit + "使用");
        }
    }

    int curpage = 1;

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.redpacketList(App.getApp().getKey(), curpage + "");
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
        showToast(msg);
        stopReOrLoad();
        recyclerview.setVisibility(View.GONE);
        emptyview.setVisibility(View.VISIBLE);
    }

}
