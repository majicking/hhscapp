package com.majick.guohanhealth.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.majick.guohanhealth.utils.engine.GlideEngine;


/**
 * 每一行里面的所有控件      父类View
 * @author majick
 */
public class ViewHolder {
    private SparseArray<View> mViews;//稀疏数组    每一行里面的所有控件(虽然它的名字叫数组但是使用和map一样)
    private int mPosition;  //行号
    private View mConvertView;//每一行对应的布局
    private Context context;

    private ViewHolder(Context context, ViewGroup parent, int layoutId, int postion) {
        this.context = context;
        this.mPosition = postion;
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
    }

    /**
     * 获取一个Viewholder对象
     */

    public static ViewHolder getInstance(Context context, View convertView, ViewGroup parent, int layoutId, int postion) {
        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId, postion);
        }
        return (ViewHolder) convertView.getTag();
    }

    /**
     * 通过控件的id拿到相应的控件（注意：因为我不知道控件的类型，要用到泛型）
     *
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        //根据viewId作为key在mViews集合得到相应的view
        View view = mViews.get(viewId);
        //如果之前没有把相应的布局的里面的控件添加到mViews里面来。就要 mConvertView.findViewById(viewId)找到相应的控件并添加到mViews，方便下次使用
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 为TextView设置文字
     */

    public ViewHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * 为ImageView设置图片
     */
    //public ViewHolder setImageBitmap(int viewId,Bitmap bm){
    @TargetApi(Build.VERSION_CODES.M)
    public ViewHolder setImageBitmap(int viewId, final Object bm) {
        final ImageView view = getView(viewId);
        if (bm instanceof Bitmap) {
            view.setImageBitmap((Bitmap) bm);
        } else if (bm instanceof Integer) {
            view.setImageResource((Integer) bm);
        } else if (bm instanceof String) {
            GlideEngine.getInstance().loadImage(context,view,(String) bm);
        } else if (bm instanceof Icon) {
            view.setImageIcon((Icon) bm);
        } else if (bm instanceof Drawable) {
            view.setImageDrawable((Drawable) bm);
        }
        return this;
    }

    public int getPosition() {
        return mPosition;
    }

    public View getConvertView() {
        return mConvertView;
    }
}

