package com.majick.guohanhealth.adapter;

import android.util.SparseArray;
import android.view.View;

/**
 * ViewHolder 通用类
 *
 * @author ZZQ
 * @date 2015-8-10
 */
public class ViewHolders {
    @SuppressWarnings("unchecked")
    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
}