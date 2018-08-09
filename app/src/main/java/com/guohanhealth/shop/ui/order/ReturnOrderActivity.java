package com.guohanhealth.shop.ui.order;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReturnOrderActivity extends BaseActivity {


    @BindView(R.id.common_toolbar_title_tv)
    TextView commonToolbarTitleTv;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_return_order;
    }

    int type;

    @Override
    protected void initView(Bundle savedInstanceState) {
        type=getIntent().getIntExtra(Constants.TYPE,0);
        if (type == 0)
            initToolBarNav(commonToolbar, commonToolbarTitleTv, "商品退款");
        else
            initToolBarNav(commonToolbar, commonToolbarTitleTv, "商品货");
    }


}
