package com.guohanhealth.shop.ui.order;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.adapter.ViewPagerAdapter;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseActivity;
import com.guohanhealth.shop.event.OnFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.carbs.android.segmentcontrolview.library.SegmentControlView;

public class OrderActivity extends BaseActivity implements OnFragmentInteractionListener {
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.order_view_menu)
    SegmentControlView orderViewMenu;

    public String[] reatitle = {"全部", "待付款", "待收货", "待自提", "待评价"};
    public String[] viltitle = {"全部", "待付款", "待使用"};
    boolean selecttype;//当前是虚拟订单还是实物订单  虚拟订单true      实物订单false
    int num = 0;//选中默认项
    private ViewPagerAdapter adapter;
    private List<OrderFragment> list;
    private OrderFragment orderFragment;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_order;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        selecttype = getIntent().getBooleanExtra(Constants.ORDERTYPE, false);
        num = getIntent().getIntExtra(Constants.ORDERINDEX, 0);
        list = new ArrayList<>();

        back.setOnClickListener(v -> finish());

        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
        tab.setupWithViewPager(viewpager);

        selectType(selecttype);
        viewpager.setCurrentItem(num);
        orderViewMenu.setOnSegmentChangedListener(index -> {
            switch (index) {
                case 0:
                    selecttype = false;
                    break;
                case 1:
                    selecttype = true;
                    break;
            }
            selectType(selecttype);
        });
    }

    private void selectType(boolean selecttype) {
        list.clear();
        list = setItemNumber(selecttype);
        adapter = new ViewPagerAdapter(mContext, getSupportFragmentManager(), list, selecttype ? viltitle : reatitle);
        viewpager.setAdapter(adapter);
    }

    private List<OrderFragment> setItemNumber(boolean selecttype) {
        List<OrderFragment> list = new ArrayList<>();
        for (int i = 0; i < (selecttype ? viltitle.length : reatitle.length); i++) {
            orderFragment = OrderFragment.newInstance(i, selecttype);
            list.add(orderFragment);
        }
        return list;
    }


    @Override
    public Object doSomeThing(String key, Object value) {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


}
