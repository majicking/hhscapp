package com.guohanhealth.shop.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.utils.engine.GlideEngine;


/**
 * 空列表背景
 */
public class EmptyView extends FrameLayout {
    private ImageView ivListEmpty;
    private TextView tvListEmptyTitle;
    private TextView tvListEmptySubTitle;
    private View view;
    Context context;

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_empty, this);
        ivListEmpty = (ImageView) findViewById(R.id.img);
        tvListEmptyTitle = (TextView) findViewById(R.id.text);
        tvListEmptySubTitle = (TextView) findViewById(R.id.text1);
        view = findViewById(R.id.view);
    }

    public EmptyView setEmpty(int resId, String title, String subTitle) {
        ivListEmpty.setImageResource(resId);
        tvListEmptyTitle.setText(title);
        tvListEmptySubTitle.setText(subTitle);
        return this;
    }

    public EmptyView setEmpty(String title, String subTitle) {
        tvListEmptyTitle.setText(title);
        tvListEmptySubTitle.setText(subTitle);
        return this;
    }

    public EmptyView setClickListener(View.OnClickListener listener) {
        view.setOnClickListener(listener);
        return this;
    }

    public EmptyView setEmptyImg(Object img) {
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


    public EmptyView setEmptyText(String text) {
        tvListEmptyTitle.setText("" + text);
        return this;
    }


    public EmptyView setEmptyText1(String text1) {
        tvListEmptySubTitle.setText("" + text1);
        return this;
    }

}
