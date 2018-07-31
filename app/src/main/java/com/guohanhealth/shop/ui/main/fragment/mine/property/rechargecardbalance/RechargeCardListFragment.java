package com.guohanhealth.shop.ui.main.fragment.mine.property.rechargecardbalance;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseFragment;
import com.guohanhealth.shop.bean.PreInfo;
import com.guohanhealth.shop.bean.RcdInfo;
import com.guohanhealth.shop.custom.EmptyView;
import com.guohanhealth.shop.event.OnFragmentInteractionListener;
import com.guohanhealth.shop.ui.main.fragment.mine.property.PredepositModel;
import com.guohanhealth.shop.utils.Utils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;

public class RechargeCardListFragment extends BaseFragment<RechargeCardPersenter, PredepositModel> implements RechargeCardView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.emptyview)
    EmptyView emptyview;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private int curpage = 1;
    private final ArrayList<RcdInfo.DataBean> mList;
    private Adapters mAdapters;

    public RechargeCardListFragment() {
        mList = new ArrayList<>();
    }

    public static RechargeCardListFragment newInstance(String param1, String param2) {
        RechargeCardListFragment fragment = new RechargeCardListFragment();
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

    boolean isLoad = false;

    @Override
    protected void initView(Bundle savedInstanceState) {
        smartrefreshlayout.setRefreshHeader(new ClassicsHeader(mContext));
        smartrefreshlayout.setOnRefreshListener(refreshLayout -> {
            curpage = 1;
            isLoad = false;
            onButtonPressed(Constants.UPDATAMONEY,"");
            mPresenter.predeposit(App.getApp().getKey(), "rcblog", curpage + "");
        });
        smartrefreshlayout.setOnLoadMoreListener(refreshLayout -> {
            if (Boolean.valueOf(App.getApp().Hasmore()) && App.getApp().getPage_total() > curpage) {
                isLoad = true;
                curpage++;
                mPresenter.predeposit(App.getApp().getKey(), "rcblog", curpage + "");
            } else {
                stopReOrLoad();
            }
        });
        emptyview.setEmpty("暂无充值卡记录", "赶紧充值去吧");

        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapters = new Adapters(R.layout.property_item, mList);
        recyclerview.setAdapter(mAdapters);

    }

    class Adapters extends BaseQuickAdapter<RcdInfo.DataBean, BaseViewHolder> {

        public Adapters(int layoutResId, @Nullable List<RcdInfo.DataBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, RcdInfo.DataBean item) {
            try {

                helper.setText(R.id.text1, item.description);
                helper.getView(R.id.text2).setVisibility(View.GONE);
                TextView textView = helper.getView(R.id.text3);
                if (Utils.getString(item.available_amount).charAt(0) == '-') {
                    textView.setTextColor(mContext.getResources().getColor(R.color.green));
                    textView.setText(item.available_amount);
                } else {
                    textView.setTextColor(mContext.getResources().getColor(R.color.appColor));
                    textView.setText("+" + item.available_amount);
                }
                helper.setText(R.id.text4, item.add_time_text);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onButtonPressed(String key, String value) {
        if (mListener != null) {
            mListener.doSomeThing(key, value);
        }
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
    public void onResume() {
        super.onResume();
        mPresenter.predeposit(App.getApp().getKey(), "rcblog", curpage + "");
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
    public void getData(Object info) {
        stopReOrLoad();
        if ((RcdInfo) info != null) {
            if (!isLoad) {
                mList.clear();
            }
            if (Utils.isEmpty(((RcdInfo) info).log_list)) {
                mList.addAll(((RcdInfo) info).log_list);
                recyclerview.setVisibility(View.VISIBLE);
                emptyview.setVisibility(View.GONE);
            }else{
                recyclerview.setVisibility(View.GONE);
                emptyview.setVisibility(View.VISIBLE);
            }
            mAdapters.notifyDataSetChanged();
        }
    }

    @Override
    public void getPreData(PreInfo info) {

    }

    @Override
    public void getResult(String s) {

    }


}
