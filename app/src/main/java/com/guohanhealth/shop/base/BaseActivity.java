package com.guohanhealth.shop.base;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.event.PermissionListener;
import com.guohanhealth.shop.http.RxManager;
import com.guohanhealth.shop.utils.Logutils;
import com.guohanhealth.shop.utils.Utils;
import com.guohanhealth.shop.view.LoadingDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

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
        if (!mDialog.isShowing())
            mDialog.show();
    }

    public void showSnackBar(View view, String s) {
        Snackbar.make(((Activity) mContext).getCurrentFocus(), s, Snackbar.LENGTH_LONG)
                .setAction("关闭", v -> showToast(s)).show();
    }


    public View getView(int layoutid) {
        View v = LayoutInflater.from(mContext).inflate(layoutid, null);
        return v;
    }

    /**
     * 获取控件
     */
    public <T extends View> T getView(View view, int viewId) {
        if (view != null) {
            view = view.findViewById(viewId);
        }
        return (T) view;
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


    public void showToast(Object msg) {
        Utils.showToast(this, "" + msg);
    }

    @Override
    public void faild(String msg) {
        Logutils.i(msg);
    }

    @Override
    public void getData(Object data) {
        Logutils.i(data);
    }

    /**
     * 申请运行时权限
     */
    public void requestRuntimePermission(String[] permissions, PermissionListener permissionListener) {
        mPermissionListener = permissionListener;
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), 1);
        } else {
            permissionListener.onGranted();
        }
    }

    public PermissionListener mPermissionListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null) {
                    fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
            }
        }
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    List<String> deniedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        String permission = permissions[i];
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            deniedPermissions.add(permission);
                        }
                    }
                    if (deniedPermissions.isEmpty()) {
                        if (mPermissionListener != null)
                            mPermissionListener.onGranted();
                    } else {
                        if (mPermissionListener != null)
                            mPermissionListener.onDenied(deniedPermissions);
                    }
                }
                break;
        }
    }
}
