package com.guohanhealth.shop.ui.main.fragment.mine.property;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.internal.LinkedTreeMap;
import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.base.BaseFragment;
import com.guohanhealth.shop.bean.MyAssectInfo;
import com.guohanhealth.shop.bean.PayWayInfo;
import com.guohanhealth.shop.bean.PdcashInfo;
import com.guohanhealth.shop.bean.PdrechargeInfo;
import com.guohanhealth.shop.bean.PredepositLogInfo;
import com.guohanhealth.shop.custom.EmptyView;
import com.guohanhealth.shop.event.OnFragmentInteractionListener;
import com.guohanhealth.shop.http.Result;
import com.guohanhealth.shop.utils.Utils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

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
    Unbinder unbinder;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PredepositFragment() {
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
        return R.layout.fragment_predeposit;
    }

    private String url;
    private int carpage = 1;

    @Override
    protected void initView(Bundle savedInstanceState) {


    }


    @Override
    public void onResume() {
        super.onResume();
        switch (mParam1) {
            case "1":
                if (emptyview != null)
                    emptyview.setEmpty("您尚无预存款收支信息", "使用商城预存款结算更方便");
                url = "predepositlog";
                mPresenter.predeposit(App.getApp().getKey(), url, carpage + "", PredepositLogInfo.class);
                break;
            case "2":
                url = "pdrechargelist";
                if (emptyview != null)
                    emptyview.setEmpty("您尚未充值过预存款", "使用商城预存款结算更方便");
                mPresenter.predeposit(App.getApp().getKey(), url, carpage + "", PdrechargeInfo.class);
                break;
            case "3":
                url = "pdcashlist";
                if (emptyview != null)
                    emptyview.setEmpty("您尚未提现过预存款", "使用商城预存款结算更方便");
                mPresenter.predeposit(App.getApp().getKey(), url, carpage + "", PdcashInfo.class);
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
    public void getMyAssect(MyAssectInfo info) {

    }

    @Override
    public void getPaymentList(PayWayInfo info) {

    }

    @Override
    public void getData(Object result) {
        switch (mParam1) {
            case "1":
                break;
            case "2":
                List<PdrechargeInfo.DataBean> list = ((PdrechargeInfo) result).list;
                if (Utils.isEmpty(list)) {
                    showToast(list.size());
                }
                break;
            case "3":
                break;
        }
    }
}
