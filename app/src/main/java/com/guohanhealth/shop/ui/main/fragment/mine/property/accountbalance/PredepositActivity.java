package com.guohanhealth.shop.ui.main.fragment.mine.property.accountbalance;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.adapter.ViewPagerAdapter;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseActivity;
import com.guohanhealth.shop.bean.MyAssectInfo;
import com.guohanhealth.shop.bean.PayWayInfo;
import com.guohanhealth.shop.bean.PreInfo;
import com.guohanhealth.shop.event.ObjectEvent;
import com.guohanhealth.shop.event.OnFragmentInteractionListener;
import com.guohanhealth.shop.event.RxBus;
import com.guohanhealth.shop.ui.main.fragment.mine.property.PredepositModel;
import com.guohanhealth.shop.ui.main.fragment.mine.property.PredepositPersenter;
import com.guohanhealth.shop.ui.main.fragment.mine.property.PredepositView;
import com.guohanhealth.shop.ui.main.fragment.mine.property.accountbalance.PredAddFragment;
import com.guohanhealth.shop.ui.main.fragment.mine.property.accountbalance.PredPutforwardFragment;
import com.guohanhealth.shop.ui.main.fragment.mine.property.accountbalance.PredepositFragment;
import com.guohanhealth.shop.utils.Logutils;
import com.guohanhealth.shop.utils.PayResult;
import com.guohanhealth.shop.utils.Utils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;

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
        mPresenter.getPre(App.getApp().getKey(),"predepoit");
    }


    @Override
    public Object doSomeThing(String key, Object value) {
        if (key.equals(Constants.BALANCENUMBER)) {
            viewpager.setCurrentItem(Integer.valueOf((String) value));
        } else if (key.equals(Constants.UPDATAMONEY)) {
            mPresenter.getPre(App.getApp().getKey(),"predepoit");
        }
        return null;
    }

    @Override
    public void getMyAssect(MyAssectInfo info) {
    }

    @Override
    public void getPaymentList(PayWayInfo info) {

    }

    @Override
    public void getData(Object result) {
        PreInfo info = (PreInfo) result;
        predepoit = info.predepoit;
        predepositTextBalance.setText("￥" + info.predepoit);
    }


    @Override
    public void faild(String msg) {
        showToast(msg);
    }
}
