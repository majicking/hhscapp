package com.guohanhealth.shop.base;


import android.content.Context;

import com.guohanhealth.shop.http.RxManager;

/**
 * Author:  andy.xwt
 * Date:    2016/7/20 17:03
 * Description: 最基础presenter 包含view 与model
 */


public class BasePresenter<V, E> {

    protected V mView;
    protected E mModel;
    protected Context mContext;
    protected RxManager mRxManager = new RxManager();

    /**
     * 与view进行关联
     *
     * @param view
     */
    void attachView(V view) {
        this.mView = view;
        if (view instanceof BaseActivity) {
            this.mContext = (Context) view;
        }
        if (view instanceof BaseFragment) {
            this.mContext = ((BaseFragment) view).getActivity();
        }
    }

    /**
     * 与model进行关联
     *
     * @param model
     */
    void attachModel(E model) {
        this.mModel = model;
    }

    /**
     * 与view解除绑定 RxJava取消订阅
     */
    void detach() {
        mView = null;
        mRxManager.clear();
    }

    /**
     * 判断当前view是否存活
     */
    public boolean isViewActive() {
        return mView != null;
    }


}
