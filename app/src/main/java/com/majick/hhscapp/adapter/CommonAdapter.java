package com.majick.hhscapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {
    private Context mContext;
    private List<T> mData;// 数据源（因为我不知道每一行里面具体放的是什么，所以用泛型）

    private int mItemLayoutId;

    public CommonAdapter(Context context, List<T> mData2, int itemLayoutId) {
        mData=new ArrayList<>();
        this.mContext = context;
        this.mData = mData2;
        this.mItemLayoutId = itemLayoutId;
    }

    public void updataAdapter(List<T> mData2) {
        if (mData2!=null&&mData2.size()>0)
        this.mData=mData2;
        else
            mData.clear();
        notifyDataSetChanged();
    }

    public int getCount() {
        return mData.size();
    }

    public T getItem(int position) {
        return mData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parentViewGroup) {
        ViewHolder viewHolder = getViewHolder(position, convertView, parentViewGroup);
        //填充数据
        convert(viewHolder, getItem(position), position, convertView, parentViewGroup);
        return viewHolder.getConvertView();
    }

    // 填充数据
    public abstract void convert(ViewHolder viewHolder, T item, int position, View convertView, ViewGroup parentViewGroup);

    private ViewHolder getViewHolder(int position, View convertView, ViewGroup parent) {
        return ViewHolder.getInstance(mContext, convertView, parent, mItemLayoutId, position);
    }

}








