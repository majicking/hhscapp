package com.majick.guohanhealth.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 黄鹏 on 2017/4/1.
 * ClassWork
 */

public class ViewPageAdapter extends PagerAdapter {
    private Context context;
    private List<View> views;

    public ViewPageAdapter(Context context, List<View> views) {
        this.context = context;
        this.views = views;
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
    public void destroyItem(ViewGroup container, int position,
                            Object object) {          //销毁Item
        // TODO Auto-generated method stub
        ((ViewPager) container).removeView(views.get(position));
        //super.destroyItem(container, position, object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {    //实例化Item
        // TODO Auto-generated method stub
        ((ViewPager) container).addView(views.get(position));
        return views.get(position);
    }
}
