package com.guohanhealth.shop.ui.main.fragment.mine.property.accountbalance;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseFragment;
import com.guohanhealth.shop.bean.MyAssectInfo;
import com.guohanhealth.shop.bean.PayWayInfo;
import com.guohanhealth.shop.bean.PdcashInfo;
import com.guohanhealth.shop.bean.PdrechargeInfo;
import com.guohanhealth.shop.bean.PredepositLogInfo;
import com.guohanhealth.shop.custom.EmptyView;
import com.guohanhealth.shop.event.ObjectEvent;
import com.guohanhealth.shop.event.OnFragmentInteractionListener;
import com.guohanhealth.shop.event.RxBus;
import com.guohanhealth.shop.ui.main.fragment.mine.property.PredepositModel;
import com.guohanhealth.shop.ui.main.fragment.mine.property.PredepositPersenter;
import com.guohanhealth.shop.ui.main.fragment.mine.property.PredepositView;
import com.guohanhealth.shop.utils.Logutils;
import com.guohanhealth.shop.utils.PayResult;
import com.guohanhealth.shop.utils.Utils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PredepositFragment extends BaseFragment<PredepositPersenter, PredepositModel> implements PredepositView {
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
    private ArrayList<PredepositLogInfo.DataBean> mList1;
    private ArrayList<PdrechargeInfo.DataBean> mList2;
    private ArrayList<PdcashInfo.DataBean> mList3;
    private Adapter1 mAdapter1;
    private Adapter2 mAdapter2;
    private Adapter3 mAdapter3;

    public PredepositFragment() {
        mList1 = new ArrayList<>();
        mList2 = new ArrayList<>();
        mList3 = new ArrayList<>();
    }

    public static PredepositFragment newInstance(String param1, String param2) {
        PredepositFragment fragment = new PredepositFragment();
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

    private String url;
    private int carpage = 1;
    private boolean isLoad;
    @Override
    protected void initView(Bundle savedInstanceState) {
        smartrefreshlayout.setRefreshHeader(new ClassicsHeader(mContext));
        smartrefreshlayout.setOnRefreshListener(refreshLayout -> {
            carpage = 1;
            getData();
            isLoad = false;
            onButtonPressed(Constants.UPDATAMONEY,"");
        });
        smartrefreshlayout.setOnLoadMoreListener(refreshLayout -> {
            if (hasmore && !(carpage >= pagetotal)) {
                isLoad = true;
                carpage++;
                getData();
            } else {
                smartrefreshlayout.finishLoadMore();
            }
        });
        mAdapter1 = new Adapter1(R.layout.property_item, mList1);
        mAdapter2 = new Adapter2(R.layout.property_item, mList2);
        mAdapter3 = new Adapter3(R.layout.property_item, mList3);
        mAdapter2.setOnItemClickListener((adapter, view, position) -> {
            mPresenter.getPayList(App.getApp().getKey());
            App.getApp().setPay_sn(mList2.get(position).pdr_sn);
        });
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
    }

    class Adapter1 extends BaseQuickAdapter<PredepositLogInfo.DataBean, BaseViewHolder> {

        public Adapter1(int layoutResId, @Nullable List<PredepositLogInfo.DataBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, PredepositLogInfo.DataBean item) {
            try {
                helper.setText(R.id.text1, item.lg_desc);
                helper.getView(R.id.text2).setVisibility(View.GONE);
                TextView textView = helper.getView(R.id.text3);
                if (Utils.getString(item.lg_av_amount).charAt(0) == '-') {
                    textView.setTextColor(mContext.getResources().getColor(R.color.green));
                    textView.setText(item.lg_av_amount);
                } else {
                    textView.setTextColor(mContext.getResources().getColor(R.color.appColor));
                    textView.setText("+" + item.lg_av_amount);
                }
                helper.setText(R.id.text4, item.lg_add_time_text);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class Adapter2 extends BaseQuickAdapter<PdrechargeInfo.DataBean, BaseViewHolder> {

        public Adapter2(int layoutResId, @Nullable List<PdrechargeInfo.DataBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, PdrechargeInfo.DataBean item) {
            try {

                helper.setText(R.id.text2, "单号:" + item.pdr_sn);
                TextView textView = helper.getView(R.id.text3);
                if (Utils.getString(item.pdr_payment_state).equals("0")) {
                    helper.setText(R.id.text1, item.pdr_payment_state_text + "[点击支付]");
                    textView.setTextColor(mContext.getResources().getColor(R.color.green));
                    textView.setText(item.pdr_amount);
                } else {
                    helper.setText(R.id.text1, item.pdr_payment_state_text);
                    textView.setTextColor(mContext.getResources().getColor(R.color.appColor));
                    textView.setText("+" + item.pdr_amount);
                }
                helper.setText(R.id.text4, item.pdr_add_time_text);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class Adapter3 extends BaseQuickAdapter<PdcashInfo.DataBean, BaseViewHolder> {

        public Adapter3(int layoutResId, @Nullable List<PdcashInfo.DataBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, PdcashInfo.DataBean item) {
            try {
                helper.setText(R.id.text1, "提现审核[" + item.pdc_payment_state_text + "]");
                helper.setText(R.id.text2, "单号:" + item.pdc_sn);
                TextView textView = helper.getView(R.id.text3);
                if (Utils.getString(item.pdc_payment_state).equals("0")) {
                    textView.setTextColor(mContext.getResources().getColor(R.color.green));
                    textView.setText(item.pdc_amount);
                } else {
                    textView.setTextColor(mContext.getResources().getColor(R.color.appColor));
                    textView.setText("+" + item.pdc_amount);
                }
                helper.setText(R.id.text4, item.pdc_add_time_text);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }


    private void getData() {
        switch (mParam1) {
            case "1":
                if (emptyview != null)
                    emptyview.setEmpty("您尚无预存款收支信息", "使用商城预存款结算更方便");
                url = "predepositlog";
                mPresenter.predeposit(App.getApp().getKey(), url, carpage + "");
                break;
            case "2":
                url = "pdrechargelist";
                if (emptyview != null)
                    emptyview.setEmpty("您尚未充值过预存款", "使用商城预存款结算更方便");
                mPresenter.predeposit(App.getApp().getKey(), url, carpage + "");
                break;
            case "3":
                url = "pdcashlist";
                if (emptyview != null)
                    emptyview.setEmpty("您尚未提现过预存款", "使用商城预存款结算更方便");
                mPresenter.predeposit(App.getApp().getKey(), url, carpage + "");
                break;
        }
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


    @Override
    public void getMyAssect(MyAssectInfo info) {

    }

    private PopupWindow popuWindow;

    @Override
    public void getPaymentList(PayWayInfo info) {
        Logutils.i(info.payment_list.size());

        popuWindow = Utils.shopPayWindown(mContext, info.payment_list, App.getApp().getPay_sn(), "2", v -> {
            popuWindow.dismiss();
        });
        RxBus.getDefault().register(this, PayResult.class, payResult -> {
            Logutils.i(payResult.getResult());
            popuWindow.dismiss();
            if (payResult.getResultStatus().equals("9000")) {
                showToast("支付成功");
                onButtonPressed(Constants.UPDATAMONEY,"");
                mPresenter.predeposit(App.getApp().getKey(), url, carpage + "");
                return;
            } else if (payResult.getResultStatus().equals("8000")) {
                showToast("支付结果确认中");
            } else if (payResult.getResultStatus().equals("6001")) {
                showToast("支付取消");
            } else {
                showToast("订单支付失败");
            }
            RxBus.getDefault().unRegister(this);
        });
        RxBus.getDefault().register(this, BaseResp.class, resp -> {
            if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
                popuWindow.dismiss();
                if (resp.errCode == 0) {
                    showToast("支付成功");
                    onButtonPressed(Constants.UPDATAMONEY,"");
                    mPresenter.predeposit(App.getApp().getKey(), url, carpage + "");
                    return;
                } else if (resp.errCode == -2) {
                    showToast("取消交易");
                    showToast("支付失败");
                }
            }
            RxBus.getDefault().unRegister(this);
        });

        RxBus.getDefault().register(this, ObjectEvent.class, error -> {
            showToast(error.msg);
            popuWindow.dismiss();
            RxBus.getDefault().unRegister(this);
        });
    }

    boolean hasmore;
    int pagetotal = 1;

    @Override
    public void getData(Object data) {
        stopReOrLoad();
        String result = (String) data;
        switch (mParam1) {
            case "1":
                if (!isLoad) {
                    mList1.clear();
                }
                PredepositLogInfo info1 = Utils.getObject(Utils.getDatasString(result), PredepositLogInfo.class);
                pagetotal = Utils.getPageTotal(result);
                hasmore = Utils.getHasMore(result);
                List<PredepositLogInfo.DataBean> list1 = info1.list;
                if (Utils.isEmpty(list1)) {
                    mList1.addAll(list1);
                    recyclerview.setAdapter(mAdapter1);
                    recyclerview.setVisibility(View.VISIBLE);
                    emptyview.setVisibility(View.GONE);
                } else {
                    recyclerview.setVisibility(View.VISIBLE);
                    emptyview.setVisibility(View.GONE);
                }
                mAdapter1.notifyDataSetChanged();

                break;
            case "2":
                if (!isLoad) {
                    mList2.clear();
                }
                PdrechargeInfo info2 = Utils.getObject(Utils.getDatasString(result), PdrechargeInfo.class);
                pagetotal = Utils.getPageTotal(result);
                hasmore = Utils.getHasMore(result);
                List<PdrechargeInfo.DataBean> list2 = info2.list;
                recyclerview.setAdapter(mAdapter2);

                if (Utils.isEmpty(list2)) {

                    mList2.addAll(list2);
                    recyclerview.setAdapter(mAdapter2);
                    recyclerview.setVisibility(View.VISIBLE);
                    emptyview.setVisibility(View.GONE);
                } else {
                    recyclerview.setVisibility(View.GONE);
                    emptyview.setVisibility(View.VISIBLE);
                }
                mAdapter2.notifyDataSetChanged();
                break;
            case "3":
                if (!isLoad) {
                    mList3.clear();
                }
                PdcashInfo info3 = Utils.getObject(Utils.getDatasString(result), PdcashInfo.class);
                pagetotal = Utils.getPageTotal(result);
                hasmore = Utils.getHasMore(result);
                List<PdcashInfo.DataBean> list3 = info3.list;
                recyclerview.setAdapter(mAdapter2);
                if (Utils.isEmpty(list3)) {
                    mList3.addAll(list3);
                    recyclerview.setAdapter(mAdapter3);
                    recyclerview.setVisibility(View.VISIBLE);
                    emptyview.setVisibility(View.GONE);
                } else {
                    recyclerview.setVisibility(View.GONE);
                    emptyview.setVisibility(View.VISIBLE);
                }
                mAdapter3.notifyDataSetChanged();
                break;
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
}
