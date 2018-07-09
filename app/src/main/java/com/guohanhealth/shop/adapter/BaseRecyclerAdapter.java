package com.guohanhealth.shop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2017/11/6 0006.
 * huangjialin
 * 普通类型的适配器
 */

public abstract class BaseRecyclerAdapter<T, B extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<BaseViewHolder> {
    private Context mContext;
    private int mLayoutId;
    private List<T> mData;


    public BaseRecyclerAdapter(Context mContext, int mLayoutId, List<T> mData) {
        this.mContext = mContext;
        this.mLayoutId = mLayoutId;
        this.mData = mData;
    }

    public void upDataAdapter(List<T> mData) {
        if (mData != null && mData.size() > 0) {
            this.mData = mData;
        } else {
            this.mData.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = BaseViewHolder.getRecyclerHolder(mContext, parent, mLayoutId);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        convert(holder, mData.get(position));

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 对外提供的方法
     */
    public abstract void convert(BaseViewHolder holder, T t);


    /**
     * 针对多种类型的itemView
     *
     * @param <T>
     */
    public interface ConmonItemType<T> {
        int getLayoutId(int itemType); //不同的Item的布局

        int getItemViewType(int position, T t); //type
    }

}
