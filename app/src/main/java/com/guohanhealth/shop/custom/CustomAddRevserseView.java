package com.guohanhealth.shop.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.guohanhealth.shop.R;

/**
 * Created by muhanxi on 17/11/15.
 */

public class CustomAddRevserseView extends LinearLayout {

    private EditText editText;
    private Button revserse;
    private Button add;
    private int mCount = 1;

    public CustomAddRevserseView(Context context) {
        super(context);
    }

    public CustomAddRevserseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);


        View view = LayoutInflater.from(context).inflate(R.layout.customview, null, false);

        revserse = (Button) view.findViewById(R.id.revserse);
        add = (Button) view.findViewById(R.id.add);
        editText = (EditText) view.findViewById(R.id.content);


        revserse.setOnClickListener(view12 -> {

            //减号
            try {
                String content = editText.getText().toString().trim();

                int count = Integer.valueOf(content) - 1;
                if (count > 0) {
                    editText.setText(count + "");
                    mCount = count - 1;

                    if (listener != null) {
                        listener.click(count);
                    }
                }


            } catch (NumberFormatException e) {
                e.printStackTrace();
            }


        });


        add.setOnClickListener(view1 -> {
            //加号
            try {
                String content = editText.getText().toString().trim();
                int count = Integer.valueOf(content) + 1;
                mCount = count;

                editText.setText(count + "");
                if (listener != null) {
                    listener.click(count);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });


        addView(view);


    }

    public void setEditText(int count) {
        editText.setText(count + "");
    }

    public int getCurrentCount() {

        return mCount;
    }

    public CustomAddRevserseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public ClickListener listener;

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    /**
     * 加减号 点击事件
     */
    public interface ClickListener {
        public void click(int count);
    }

}
