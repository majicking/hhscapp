package com.majick.guohanhealth.ui.goods.goodsdetailed.activity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.majick.guohanhealth.R;
import com.majick.guohanhealth.app.App;
import com.majick.guohanhealth.app.Constants;
import com.majick.guohanhealth.base.BaseActivity;
import com.majick.guohanhealth.event.OnFragmentInteractionListener;
import com.majick.guohanhealth.ui.goods.goodsdetailed.fragment.comment.CommentFragment;
import com.majick.guohanhealth.ui.goods.goodsdetailed.fragment.goods.GoodsFragment;
import com.majick.guohanhealth.ui.goods.goodsdetailed.fragment.goodsdetail.GoodsDetailFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodsDetailsActivity extends BaseActivity<GoodsDetailsPersenter, GoodsDetailsModel> implements GoodsDetailsView, OnFragmentInteractionListener {

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
    @BindView(R.id.goodsdetail_view_title)
    LinearLayout goodsdetailViewTitle;
    @BindView(R.id.line)
    View line;
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
        fragmentList.add(GoodsFragment.newInstance(goods_id, ""));
        fragmentList.add(GoodsDetailFragment.newInstance(goods_id, ""));
        fragmentList.add(CommentFragment.newInstance(goods_id, ""));
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        goodsdetailViewViewpager.setAdapter(adapter);
        goodsdetailViewViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(goodsdetailViewTablayout));
        goodsdetailViewTablayout.setupWithViewPager(goodsdetailViewViewpager);
        goodsdetailViewViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    line.setVisibility(View.GONE);
                    goodsdetailViewTablayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.translucent));
                    goodsdetailViewTablayout.setTabTextColors(getResources().getColor(R.color.white), getResources().getColor(R.color.appColor));
                    goodsdetailViewTitle.setBackgroundColor(getResources().getColor(R.color.translucent));
                    goodsdetailViewTitle.setSelected(false);
                    goodsdetailBack.setSelected(false);
                    goodsdetailMore.setSelected(false);
                } else {
                    goodsdetailViewTablayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.appColor));
                    goodsdetailViewTitle.setBackgroundColor(getResources().getColor(R.color.white));
                    goodsdetailViewTablayout.setTabTextColors(getResources().getColor(R.color.nc_text), getResources().getColor(R.color.appColor));
                    line.setVisibility(View.VISIBLE);
                    goodsdetailViewTablayout.setSelected(true);
                    goodsdetailMore.setSelected(true);
                    goodsdetailBack.setSelected(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
