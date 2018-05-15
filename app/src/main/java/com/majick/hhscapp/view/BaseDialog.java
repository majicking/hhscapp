package com.majick.hhscapp.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.view.WindowManager;


import com.majick.hhscapp.R;

import butterknife.ButterKnife;

/**
 * Author:  andy.xwt
 * Date:    2018/1/16 14:49
 * Description:
 */


public abstract class BaseDialog extends Dialog {

    public BaseDialog(@NonNull Context context) {
        super(context, R.style.DialogTheme);
    }
    public BaseDialog(@NonNull Context context,int style) {
        super(context,style);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initDialogLayoutId());
        ButterKnife.bind(this);
        setWindowDisPlay(initDialogDisplay());
        initView();
    }


    /**
     * 初始化对话框id
     */
    protected abstract int initDialogLayoutId();

    /**
     * 初始化布局
     */
    protected abstract void initView();

    /**
     * 初始化Dialog大小与显示方式
     */
    protected abstract DialogConfig initDialogDisplay();

    /**
     * 设置window的高度与宽度
     */
    protected void setWindowDisPlay(DialogConfig dialogConfig) {
        Window window = getWindow();
        WindowManager.LayoutParams params = getWindow().getAttributes(); // 获取对话框当前的参数值
        if (dialogConfig.width == 0) {
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        } else if (dialogConfig.width > 0 && dialogConfig.width <= 1) {
            params.width = (int) (getContext().getResources().getDisplayMetrics().widthPixels * dialogConfig.width); // 宽度设置为屏幕的0.x
        } else {
            params.width = (int) dialogConfig.width;
        }
        if (dialogConfig.height == 0) {
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        } else if (dialogConfig.height > 0 && dialogConfig.height <= 1) {
            params.height = (int) (getContext().getResources().getDisplayMetrics().heightPixels * dialogConfig.height); // 高度设置为屏幕的0.x
        } else {
            params.height = (int) dialogConfig.height;
        }
        params.verticalMargin = -0.1f;
        window.setAttributes(params); // 设置生效
        getWindow().setGravity(dialogConfig.gravity);
        setCanceledOnTouchOutside(dialogConfig.closeOnTouchOutside);
        setCancelable(dialogConfig.cancelable);
    }


    /**
     * dialog配置参数
     * width   宽度百分比
     * height  高度百分比
     * gravity 相对于整个屏幕的位置
     */
    public class DialogConfig {

        float width;
        float height;
        int gravity;
        boolean closeOnTouchOutside;
        boolean cancelable;

        public DialogConfig(float width, float height, int gravity) {
            this(width, height, gravity, false, false);
        }

        public DialogConfig(float width, float height, int gravity, boolean closeOnTouchOutside, boolean cancelable) {
            this.width = width;
            this.height = height;
            this.gravity = gravity;
            this.closeOnTouchOutside = closeOnTouchOutside;
            this.cancelable = cancelable;
        }


    }


}
