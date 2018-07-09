package com.guohanhealth.shop.base;


import android.support.annotation.DrawableRes;
import android.view.View;


public interface BaseView {



    void faild(String msg);

    /**
     * 显示正在加载
     */
    void showLoading();

    /**
     * 显示加载对话框
     */
    void showLoadingDialog(String notice);

    /**
     * 隐藏加载对话框
     */
    void hideLoadingDialog();

    /**
     * 显示网络异常
     */
    void showNetError(View.OnClickListener listener);

    /**
     * 显示网络异常
     *
     * @param msg      提示信息
     * @param listener 提示信息
     */
    void showNetError(String msg, View.OnClickListener listener);

    /**
     * 显示网络异常
     *
     * @param drawId          图片id
     * @param msg             提示信息
     * @param errorButtonText 按钮文字
     * @param listener        点击监听
     */
    void showNetError(int drawId, String msg, String errorButtonText, View.OnClickListener listener);


    /**
     * 显示没有数据
     */
    void showEmpty();


    /**
     * 显示没有数据
     *
     * @param message 提示信息
     */
    void showEmpty(String message);


    /**
     * 显示没有数据
     *
     * @param drawId  图片id
     * @param message 提示信息
     */
    void showEmpty(@DrawableRes int drawId, String message);


    /**
     * 显示内容
     */
    void showContent();
}
