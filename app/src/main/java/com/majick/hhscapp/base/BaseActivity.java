package com.majick.hhscapp.base;


import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.majick.hhscapp.http.RxManager;
import com.majick.hhscapp.utils.Utils;
import com.majick.hhscapp.view.LoadingDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.lang.reflect.ParameterizedType;

import butterknife.ButterKnife;


/**
 * Author:  andy.xwt
 * Date:    2016/12/21 11:23
 * Description: 创建的基础的activity，并在生命周期进行对view的绑定与销毁。
 */

public abstract class BaseActivity<T extends BasePresenter, E extends BaseModel> extends BaseAppCompatActivity implements BaseView {

    protected T mPresenter;
    protected E mModel;
    protected RxManager mRxManager;//之所以 有这个管理类是因为不实现presenter的类也可以进行请求的管理
    protected RxPermissions mRxPermissions;
    private LoadingDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = Utils.getGenericInstance(this, 0);
        mModel = Utils.getGenericInstance(this, 1);
        if (mPresenter != null) {
            mPresenter.attachView(this);
            if (mModel != null) {
                mPresenter.attachModel(mModel);
            }
        }
        mRxManager = new RxManager();
        mRxPermissions = new RxPermissions(this);
        ButterKnife.bind(this);
        initView(savedInstanceState);
    }


    /**
     * 在activity消除的时候解除把绑定
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detach();
        }
        if (mRxManager != null) {
            mRxManager.clear();
        }

        hideLoadingDialog();
    }

    /**
     * 初始化activity中的view
     */
    protected abstract void initView(Bundle savedInstanceState);


    @Override
    public void showLoading() {
        toggleShowLoading();
    }

    @Override
    public void showNetError(View.OnClickListener listener) {
        toggleShowNetError(listener);
    }

    @Override
    public void showNetError(String msg, View.OnClickListener listener) {
        toggleShowNetError(msg, listener);
    }

    @Override
    public void showNetError(int drawId, String msg, String errorButtonText, View.OnClickListener listener) {
        toggleShowNetError(drawId, msg, errorButtonText, listener);
    }

    @Override
    public void showEmpty() {
        toggleShowEmpty();
    }


    @Override
    public void showEmpty(String message) {
        toggleShowEmpty(message);
    }

    @Override
    public void showEmpty(int drawId, String message) {
        toggleShowEmpty(drawId, message);
    }

    @Override
    public void showContent() {
        toggleShowContent();
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
        hideLoadingView();
    }

    private Toast toast;

    public void showToast(Object msg) {
        if (toast == null) {
            toast = Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText("" + msg);
        }
        toast.show();
    }

}
