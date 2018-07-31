package com.guohanhealth.shop.ui.main.fragment.mine.property.rechargecardbalance;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseActivity;
import com.guohanhealth.shop.bean.PreInfo;
import com.guohanhealth.shop.event.OnFragmentInteractionListener;
import com.guohanhealth.shop.ui.main.fragment.mine.property.PredepositModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.carbs.android.segmentcontrolview.library.SegmentControlView;

public class RechargeCardActivity extends BaseActivity<RechargeCardPersenter, PredepositModel> implements RechargeCardView, OnFragmentInteractionListener {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.rcb_view_menu)
    SegmentControlView rcbViewMenu;
    @BindView(R.id.fragment_group)
    FrameLayout fragmentGroup;
    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.text2)
    TextView text2;
    private int mLastFgIndex;
    private List<Fragment> mFragments;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_recharge_card;
    }

    RechargeCardAddFragment mAddFragment;
    RechargeCardListFragment mListFragment;
    String money = "0.00";

    @Override
    protected void initView(Bundle savedInstanceState) {
        back.setOnClickListener(v -> finish());
        mFragments = new ArrayList<>();
        mListFragment = RechargeCardListFragment.newInstance("", "");
        mAddFragment = RechargeCardAddFragment.newInstance("", "");
        mFragments.add(mListFragment);
        mFragments.add(mAddFragment);
        switchFragment(0);
        text1.setText("充值卡余额");
        text2.setText("￥" + money);
        rcbViewMenu.setOnSegmentChangedListener(index -> {
            switchFragment(index);
            if (index == 0) {
                text1.setText("充值卡余额");
                text2.setText("￥" + money);
            } else {
                text1.setText("请输入已知平台充值卡号码");
                text2.setText("充值后可以在购物结算时选取使用充值卡余额进行支付");

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getPre(App.getApp().getKey(), "available_rc_balance");
    }

    private void switchFragment(int position) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        Fragment targetFg = mFragments.get(position);
        Fragment lastFg = mFragments.get(mLastFgIndex);
        if (targetFg != null && lastFg != null && !targetFg.isAdded()) {
            //第一次使用switchFragment()时currentFragment为null，所以要判断一下

            if (lastFg != null) {
                if (targetFg != lastFg) {
                    transaction.remove(lastFg);
                }
            }
            transaction.add(R.id.fragment_group, targetFg, targetFg.getClass().getName());
        } else {
            transaction.remove(lastFg).show(targetFg);
        }
        mLastFgIndex = position;
        transaction.commit();
    }


    @Override
    public Object doSomeThing(String key, Object value) {
        if (key.equals(Constants.UPDATA)) {
            rcbViewMenu.setSelectedIndex(Integer.valueOf((String) value));
        } else if (key.equals(Constants.UPDATAMONEY)) {
            mPresenter.getPre(App.getApp().getKey(), "available_rc_balance");
        }
        return null;
    }

    @Override
    public void getData(Object info) {

    }

    @Override
    public void getPreData(PreInfo info) {
        money=info.available_rc_balance;
        text2.setText("￥" + info.available_rc_balance);
    }

    @Override
    public void getResult(String s) {

    }


}
