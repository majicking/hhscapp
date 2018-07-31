package com.guohanhealth.shop.ui.cart;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.history)
    TextView history;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.smile)
    ImageView smile;
    @BindView(R.id.editmsg)
    EditText editmsg;
    @BindView(R.id.sendmsg)
    Button sendmsg;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_cart2;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
