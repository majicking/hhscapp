package com.guohanhealth.shop.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.List;

public class ViewPagerAdapter<T> extends FragmentStatePagerAdapter {
    private List<T> fragments = null;
    private Context context;
    private String[] titles;

    public ViewPagerAdapter(Context context, FragmentManager fm, List<T> fragments, String[] titles) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
        this.titles = titles;
        notifyDataSetChanged();
    }

    public ViewPagerAdapter(Context context, FragmentManager fm, String[] titles) {
        super(fm);
        this.context = context;
        this.titles = titles;
        notifyDataSetChanged();
    }

    public void updataAdapter(List<T> fragments, String[] titles) {

        if (fragments != null && fragments.size() > 0)
            this.fragments = fragments;
        else {
            this.fragments.clear();
        }
        if (titles != null && titles.length > 0)
            this.titles = titles;
        else
            this.titles = null;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int arg0) {

        return (Fragment) fragments.get(arg0);
    }

    @Override
    public int getItemPosition(Object object) {
// TODO Auto-generated method stub
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return fragments.size();//hotIssuesList.size();
    }
}
