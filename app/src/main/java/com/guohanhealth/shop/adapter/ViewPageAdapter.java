package com.guohanhealth.shop.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 黄鹏 on 2017/4/1.
 * ClassWork
 */

public class ViewPageAdapter<T extends View> extends PagerAdapter {
    private Context context;
    private List<T> views;
    public List<String> title;


    public ViewPageAdapter(Context context, List<T> views, List<String> title) {
        this.context = context;
        this.views = views;
        this.title = title;
    }

    public void updataAdapter(List<T> views, List<String> title) {
        this.views.clear();
        if (views != null && views.size() > 0)
            this.views = views;
        this.title = null;
        if (title != null && title.size() > 0)
            this.title = title;
        notifyDataSetChanged();
    }

    public void updataAdapter(List<T> views) {
        this.views.clear();
        if (views != null && views.size() > 0)
            this.views = views;
        notifyDataSetChanged();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {   //这个一个定要有，返回也就这样也。
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    @Override
    public int getCount() {    //获得size
        // TODO Auto-generated method stub
        return views.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
// TODO Auto-generated method stub
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {          //销毁Item
        // TODO Auto-generated method stub
         container.removeView(views.get(position));
        //super.destroyItem(container, position, object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {    //实例化Item
        // TODO Auto-generated method stub
         container.addView(views.get(position));
        return views.get(position);
    }
}
