package com.majick.guohanhealth.ui.goods.goodsdetailed.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.majick.guohanhealth.R;
import com.majick.guohanhealth.app.Constants;
import com.majick.guohanhealth.base.BaseActivity;
import com.majick.guohanhealth.event.OnFragmentInteractionListener;
import com.majick.guohanhealth.ui.main.fragment.cart.CartFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class GoodsDetailsActivity extends BaseActivity<GoodsDetailsPersenter, GoodsDetailsModel> implements GoodsDetailsView ,OnFragmentInteractionListener{

    String goods_id;
    @BindView(R.id.goodsdetail_back)
    ImageView goodsdetailBack;
    @BindView(R.id.goodsdetail_view_tablayout)
    TabLayout goodsdetailViewTablayout;
    @BindView(R.id.goodsdetail_more)
    ImageView goodsdetailMore;
    @BindView(R.id.goodsdetail_view_viewpager)
    ViewPager goodsdetailViewViewpager;
    List<String> titles = new ArrayList<>(Arrays.asList("商品", "详细", "评论"));
    private List<Fragment> fragmentList;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_goods_details;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        goods_id = getIntent().getStringExtra(Constants.GOODS_ID);
        goodsdetailBack.setOnClickListener(v -> finish());
        fragmentList = new ArrayList<>();
        fragmentList.add(CartFragment.newInstance("",""));
        fragmentList.add(CartFragment.newInstance("",""));
        fragmentList.add(CartFragment.newInstance("",""));
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        goodsdetailViewViewpager.setAdapter(adapter);
        goodsdetailViewViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(goodsdetailViewTablayout));
        goodsdetailViewTablayout.setupWithViewPager(goodsdetailViewViewpager);
    }

    @Override
    public void doSomeThing(String key, Object value) {

    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int arg0) {

            return fragmentList.get(arg0);
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();//hotIssuesList.size();
        }
    }

    @Override
    public void faild(String msg) {

    }
}
