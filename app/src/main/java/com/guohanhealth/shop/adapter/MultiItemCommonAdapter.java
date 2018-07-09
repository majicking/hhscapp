package com.guohanhealth.shop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2017/11/6 0006.
 * huangjialin
 * 如果列表的布局item有多种类型，则需要继承该适配器
 */

public abstract class MultiItemCommonAdapter<T> extends BaseRecyclerAdapter<T, RecyclerView.ViewHolder> {
    private ConmonItemType<T> mConmonItemType;
    private List<T> mDatas;
    private Context mContext;


    public MultiItemCommonAdapter(Context mContext, List<T> mData, ConmonItemType<T> conmonItemType) {
        super(mContext, -1, mData);
        this.mConmonItemType = conmonItemType;
        mDatas = mData;
        this.mContext = mContext;
    }

    @Override
    public int getItemViewType(int position) {
        return mConmonItemType.getItemViewType(position, mDatas.get(position));
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = mConmonItemType.getLayoutId(viewType);
        BaseViewHolder holder = BaseViewHolder.getRecyclerHolder(mContext, parent, layoutId);
        return holder;
    }


    /**
     * 添加头部header
     */
    public interface HeaderSectiopnSupport<T> {

        int sectionHeaderLayoutId(); //header布局文件

        int sectionTitleTextViewId(); //显示头部标题的TextView控件id

        String getTitle(T t); //显示的标题内容

    }


}
