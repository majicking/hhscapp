package com.guohanhealth.shop.ui.main.fragment.mine.collect;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseActivity;
import com.guohanhealth.shop.event.OnFragmentInteractionListener;
import com.guohanhealth.shop.ui.main.fragment.mine.property.redpack.RedPackGetFragment;
import com.guohanhealth.shop.ui.main.fragment.mine.property.redpack.RedPackListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.carbs.android.segmentcontrolview.library.SegmentControlView;

public class CollectActivity extends BaseActivity implements OnFragmentInteractionListener {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.segmentcontrolview)
    SegmentControlView segmentcontrolview;
    private int mLastFgIndex;
    private List<Fragment> mFragments;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_goods_collect;
    }

    GoodsCollectFragment mGoodsCollectFragment;
    StoreCollectFragment mStoreCollectFragment;

    @Override
    protected void initView(Bundle savedInstanceState) {
        back.setOnClickListener(v -> finish());

        mFragments = new ArrayList<>();
        mGoodsCollectFragment = GoodsCollectFragment.newInstance("", "");
        mStoreCollectFragment = StoreCollectFragment.newInstance("", "");
        mFragments.add(mGoodsCollectFragment);
        mFragments.add(mStoreCollectFragment);
        segmentcontrolview.setSelectedIndex(getIntent().getIntExtra(Constants.TYPE, 0));
        switchFragment(getIntent().getIntExtra(Constants.TYPE, 0));
        segmentcontrolview.setOnSegmentChangedListener(index -> {
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
    public Object doSomeThing(String key, Object value) {
        if (key.equals(Constants.UPDATA)) {
            segmentcontrolview.setSelectedIndex(Integer.valueOf((String) value));
        }
        return null;
    }


}
