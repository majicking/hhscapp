package com.majick.guohanhealth.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.majick.guohanhealth.R;
import com.majick.guohanhealth.utils.engine.GlideEngine;


/**
 * 空列表背景
 * <p>
 * dqw
 * 2015/8/25
 */
public class EmptyView extends FrameLayout {
    private ImageView ivListEmpty;
    private TextView tvListEmptyTitle;
    private TextView tvListEmptySubTitle;
    Context context;

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_empty, this);
        ivListEmpty = (ImageView) findViewById(R.id.img);
        tvListEmptyTitle = (TextView) findViewById(R.id.text);
        tvListEmptySubTitle = (TextView) findViewById(R.id.text1);
    }

    public View setEmpty(int resId, String title, String subTitle) {
        ivListEmpty.setImageResource(resId);
        tvListEmptyTitle.setText(title);
        tvListEmptySubTitle.setText(subTitle);
        return this;
    }

    public View setEmptyImg(Object img) {
        if (img instanceof Integer) {
            ivListEmpty.setImageResource((Integer) img);
        } else if (img instanceof Drawable) {
            ivListEmpty.setImageDrawable((Drawable) img);
        } else if (img instanceof Bitmap) {
            ivListEmpty.setImageBitmap((Bitmap) img);
        } else if (img instanceof String) {
            GlideEngine.getInstance().loadImage(context, ivListEmpty, (String) img);
        }
        return this;
    }

    public View setEmptyText(String text) {
        tvListEmptyTitle.setText("" + text);
        return this;
    }

    public View setEmptyText1(String text1) {
        tvListEmptySubTitle.setText("" + text1);
        return this;
    }

}
