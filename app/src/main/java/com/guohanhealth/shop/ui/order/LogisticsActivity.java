package com.guohanhealth.shop.ui.order;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LogisticsActivity extends BaseActivity<logisticsPersenter, OrderModel> implements LogisticsView {

    @BindView(R.id.common_toolbar_title_tv)
    TextView commonToolbarTitleTv;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    private String order_id;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_logistics;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        order_id = getIntent().getStringExtra(Constants.ORDER_ID);
        initToolBarNav(commonToolbar, commonToolbarTitleTv, "物流详情");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.searchLogistics(App.getApp().getKey(), order_id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
