package com.guohanhealth.shop.ui.main.fragment.mine.property;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.base.BaseActivity;
import com.guohanhealth.shop.bean.MyAssectInfo;
import com.guohanhealth.shop.bean.PayWayInfo;
import com.guohanhealth.shop.bean.RechargeOrderInfo;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.ApiService;
import com.guohanhealth.shop.http.HttpErrorCode;
import com.guohanhealth.shop.http.Result;
import com.guohanhealth.shop.utils.JSONParser;
import com.guohanhealth.shop.utils.Logutils;
import com.guohanhealth.shop.utils.Utils;

import java.io.IOException;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class PropertyActivity extends BaseActivity<PredepositPersenter, PredepositModel> implements PredepositView {


    @BindView(R.id.common_toolbar_title_tv)
    TextView commonToolbarTitleTv;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.property_text_accountbalance)
    TextView propertyTextAccountbalance;
    @BindView(R.id.property_view_accountbalance)
    LinearLayout propertyViewAccountbalance;
    @BindView(R.id.property_text_rechargecardbalance)
    TextView propertyTextRechargecardbalance;
    @BindView(R.id.property_view_rechargecardbalance)
    LinearLayout propertyViewRechargecardbalance;
    @BindView(R.id.property_text_cashcoupon)
    TextView propertyTextCashcoupon;
    @BindView(R.id.property_view_cashcoupon)
    LinearLayout propertyViewCashcoupon;
    @BindView(R.id.property_text_redenvelopes)
    TextView propertyTextRedenvelopes;
    @BindView(R.id.property_view_redenvelopes)
    LinearLayout propertyViewRedenvelopes;
    @BindView(R.id.property_text_membershipintegral)
    TextView propertyTextMembershipintegral;
    @BindView(R.id.property_view_membershipintegral)
    LinearLayout propertyViewMembershipintegral;
    @BindView(R.id.property_text_healthybeans)
    TextView propertyTextHealthybeans;
    @BindView(R.id.property_view_healthybeans)
    LinearLayout propertyViewHealthybeans;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_property;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initToolBarNav(commonToolbar, commonToolbarTitleTv, "我的财产");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getMyAssect(App.getApp().getKey());
    }

    @Override
    public void getMyAssect(MyAssectInfo info) {
        propertyTextAccountbalance.setText(info.available_rc_balance + "元");
        propertyTextRedenvelopes.setText(info.redpacket + "个");
        propertyTextHealthybeans.setText(info.healthbean_value + "个");
        propertyTextMembershipintegral.setText(info.point + "分");
        propertyTextCashcoupon.setText(info.voucher + "张");
        propertyTextRechargecardbalance.setText(info.predepoit + "元");
    }

    @Override
    public void getPaymentList(PayWayInfo info) {

    }

    @Override
    public void getData(Object result) {

    }


    @Override
    public void faild(String msg) {
        showToast(msg);
    }
}
