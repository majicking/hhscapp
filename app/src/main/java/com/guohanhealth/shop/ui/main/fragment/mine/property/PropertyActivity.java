package com.guohanhealth.shop.ui.main.fragment.mine.property;

import android.os.Bundle;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.base.BaseActivity;

public class PropertyActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_property;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }
}
