package com.majick.hhscapp.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.majick.hhscapp.http.RxManager;
import com.majick.hhscapp.utils.Utils;
import com.majick.hhscapp.view.LoadingDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;


public abstract class BaseFragment<T extends BasePresenter, E extends BaseModel> extends BaseAppCompatFragment implements BaseView {


    protected T mPresenter;
    protected E mModel;
    protected RxManager mRxManager;//之所以有这个管理类，是因为不实现presenter的类也可以进行请求的管理
    private boolean isLoaded;//是否已经加载
    protected RxPermissions mRxPermissions;
    private LoadingDialog mDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = Utils.getGenericInstance(this, 0);
        mModel = Utils.getGenericInstance(this, 1);
        if (mModel != null) {
            mPresenter.attachModel(mModel);
        }
        mRxManager = new RxManager();
        mRxPermissions = new RxPermissions(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(savedInstanceState);
    }

    /**
     * 初始化相应布局
     */
    protected abstract void initView(Bundle savedInstanceState);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.detach();
        }
        if (mRxManager != null) {
            mRxManager.clear();
        }
        isLoaded = false;
    }


    /**
     * 显示加载对话框
     */
    @Override
    public void showLoadingDialog(String notice) {
        if (mDialog == null) {
            mDialog = new LoadingDialog(mContext, notice);
        }
        mDialog.show();
    }

    /**
     * 隐藏对话框
     */
    @Override
    public void hideLoadingDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.hide();
        }
    }


}
