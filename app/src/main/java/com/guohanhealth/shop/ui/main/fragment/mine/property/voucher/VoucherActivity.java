package com.guohanhealth.shop.ui.main.fragment.mine.property.voucher;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseActivity;
import com.guohanhealth.shop.event.OnFragmentInteractionListener;
import com.guohanhealth.shop.ui.main.fragment.mine.property.rechargecardbalance.RechargeCardAddFragment;
import com.guohanhealth.shop.ui.main.fragment.mine.property.rechargecardbalance.RechargeCardListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.carbs.android.segmentcontrolview.library.SegmentControlView;

public class VoucherActivity extends BaseActivity implements OnFragmentInteractionListener {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.voucher_view_menu)
    SegmentControlView voucherViewMenu;
    @BindView(R.id.fragment_group)
    FrameLayout fragmentGroup;

    VoucherGetFragment mGetFragment;
    VoucherListFragment mListFragment;
    private int mLastFgIndex;
    private List<Fragment> mFragments;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_voucher;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        back.setOnClickListener(v -> finish());
        mFragments = new ArrayList<>();
        mGetFragment = VoucherGetFragment.newInstance("", "");
        mListFragment = VoucherListFragment.newInstance("", "");
        mFragments.add(mListFragment);
        mFragments.add(mGetFragment);
        switchFragment(0);
        voucherViewMenu.setOnSegmentChangedListener(index -> {
            switchFragment(index);
        });
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public Object doSomeThing(String key, Object value) {
        if (key.equals(Constants.UPDATA)) {
            voucherViewMenu.setSelectedIndex(Integer.valueOf((String) value));
        }
        return null;
    }
}
