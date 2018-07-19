package com.guohanhealth.shop.ui.main.fragment.mine.property;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.adapter.ViewPagerAdapter;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseActivity;
import com.guohanhealth.shop.base.BasePresenter;
import com.guohanhealth.shop.bean.MyAssectInfo;
import com.guohanhealth.shop.bean.PayWayInfo;
import com.guohanhealth.shop.bean.RechargeOrderInfo;
import com.guohanhealth.shop.event.OnFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PredepositActivity extends BaseActivity<PredepositPersenter, PredepositModel> implements PredepositView, OnFragmentInteractionListener {

    @BindView(R.id.common_toolbar_title_tv)
    TextView commonToolbarTitleTv;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.predeposit_text_balance)
    TextView predepositTextBalance;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private ViewPagerAdapter adapter;
    private int mCurrentPosition;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_predeposit;
    }

    private int num = 0;//选中默认项
    private List<Fragment> fragmentlist;
    private String[] titles = {"账户充值", "账户余额", "充值明细", "提现明细", "余额提现"};

    public String predepoit = "0.00";

    @Override
    protected void initView(Bundle savedInstanceState) {
        initToolBarNav(commonToolbar, commonToolbarTitleTv, "预存款");
        fragmentlist = new ArrayList<>();
        fragmentlist.add(PredAddFragment.newInstance("0", ""));
        fragmentlist.add(PredepositFragment.newInstance("1", ""));
        fragmentlist.add(PredepositFragment.newInstance("2", ""));
        fragmentlist.add(PredepositFragment.newInstance("3", ""));
        fragmentlist.add(PredPutforwardFragment.newInstance("4", ""));
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));
        adapter = new ViewPagerAdapter(this, getSupportFragmentManager(), fragmentlist, titles);
        viewpager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewpager);
        num = getIntent().getIntExtra(Constants.PROID, 0);
        viewpager.setCurrentItem(num);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
                initToolBarNav(commonToolbar, commonToolbarTitleTv, titles[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPresenter.getMyAssect(App.getApp().getKey());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public Object doSomeThing(String key, Object value) {
        if (key.equals(Constants.BALANCENUMBER)) {
            viewpager.setCurrentItem(Integer.valueOf((String) value));
        } else if (key.equals(Constants.UPDATAMONEY)) {
            mPresenter.getMyAssect(App.getApp().getKey());
        }
        return null;
    }

    @Override
    public void getMyAssect(MyAssectInfo info) {
        predepositTextBalance.setText("￥" + info.predepoit);
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
