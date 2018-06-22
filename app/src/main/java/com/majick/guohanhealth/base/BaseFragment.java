package com.majick.guohanhealth.base;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.majick.guohanhealth.R;
import com.majick.guohanhealth.http.RxManager;
import com.majick.guohanhealth.utils.Utils;
import com.majick.guohanhealth.view.LoadingDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;


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
    }

    private Toast toast;

    public void showToast(Object msg) {
//        if (toast == null) {
//            toast = Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT);
//        } else {
//            toast.setText("" + msg);
//        }
//        toast.show();
        showToast(mContext, "" + msg);
    }

    /**
     * 短暂显示Toast消息
     *
     * @param context
     * @param message
     */
    public static void showToast(Context context, String message) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_toast, null);
        TextView text = (TextView) view.findViewById(R.id.toast_message);
        text.setText(message);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 300);
        toast.setView(view);
        toast.show();
    }

    public View getView(int layoutid) {
        View v = LayoutInflater.from(mContext).inflate(layoutid, null);
        return v;
    }

}
