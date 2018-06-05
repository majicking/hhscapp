package com.majick.hhscapp.base;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.majick.hhscapp.R;
import com.majick.hhscapp.utils.StatusBarUtils;
import com.majick.hhscapp.view.MultipleStatusLayout;

import butterknife.ButterKnife;


public abstract class BaseAppCompatActivity extends FragmentActivity {

    /**
     * log日志
     */
    protected static String TAG_LOG = null;

    /**
     * 当前上下文对象
     */
    protected Context mContext;
    private MultipleStatusLayout mMultipleStatusLayout;

    /**
     * 跳转其他activity启动或者退出的模式
     */
    public enum TransitionMode {
        LEFT, RIGHT, TOP, BOTTOM, SCALE, FADE
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        overrideTransitionAnimation();
        //获取传递过来的bundle数据
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            getBundleExtras(extras);
        }
        //获取上下文对象并设置log并添加activity到app管理类中
        mContext = this;
        TAG_LOG = this.getClass().getSimpleName();
        BaseAppManager.getInstance().addActivity(this);


        //添加相应的布局
        if (getContentViewLayoutID() != 0) {
            setContentView(getContentViewLayoutID());
        } else {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }
    }

    /**
     * 当home(bar上的home)被点击的时候结束掉当前activity
     *
     * @param item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 初始化toolbar (只有标题）
     *
     * @param title 标题
     */
    protected void initToolBarNav(Toolbar toolbar, String title) {
        toolbar.setNavigationOnClickListener((View v) -> {
            showKeyboard(false);
        });
        toolbar.setTitle(title);
    }

    protected void initToolBarNav(Toolbar toolbar, TextView textView, String title) {
        toolbar.setNavigationOnClickListener((View v) -> {
            showKeyboard(false);
//            getHandler().postDelayed(() -> finish(), 200);

        });
        textView.setText(title);
    }

    protected void initToolBarNav(Toolbar toolbar, @StringRes int title) {
        initToolBarNav(toolbar, getString(title));
    }


    /**
     * 初始化toolbar(只有标题）
     *
     * @param runnable 导航点击事件
     */
    protected void initToolBarNav(Toolbar toolbar, String title, final Runnable runnable) {
        toolbar.setNavigationOnClickListener(v -> {
            showKeyboard(false);
            getHandler().postDelayed(runnable, 200);

        });
        toolbar.setTitle(title);
    }

    /**
     * 设置contentView
     *
     * @param layoutResID
     */
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        mMultipleStatusLayout = new MultipleStatusLayout(this);
        View view = LayoutInflater.from(this).inflate(layoutResID, mMultipleStatusLayout);
        super.setContentView(view);
        ButterKnife.bind(this);
    }

    /**
     * 在activity退出的时候销毁相应的监听器
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseAppManager.getInstance().removeActivity(this);
    }

    /**
     * 加载activity进入动画
     */
    public void overrideTransitionAnimation() {
        if (toggleOverridePendingTransition()) { //判断是否有加载动画
            switch (getOverridePendingTransitionMode()) {//判断加载动画的方式
                case TOP:
                    overridePendingTransition(R.anim.top_in, R.anim.no_anim);
                    break;
                case BOTTOM:
                    overridePendingTransition(R.anim.bottom_in, R.anim.no_anim);
                    break;
                case LEFT:
                    overridePendingTransition(R.anim.left_in, R.anim.no_anim);
                    break;
                case RIGHT:
                    overridePendingTransition(R.anim.right_in, R.anim.no_anim);
                    break;
                case SCALE:
                    overridePendingTransition(R.anim.scale_in, R.anim.no_anim);
                    break;
                case FADE:
                    overridePendingTransition(R.anim.fade_in, R.anim.no_anim);
                    break;
            }
        }
    }

    /**
     * 设置退出动画
     */
    @Override
    public void finish() {
        super.finish();
        if (toggleOverridePendingTransition()) { //判断是否有加载动画
            switch (getOverridePendingTransitionMode()) {//判断加载动画的方式
                case TOP:
                    overridePendingTransition(R.anim.no_anim, R.anim.top_out);
                    break;
                case BOTTOM:
                    overridePendingTransition(R.anim.no_anim, R.anim.bottom_out);
                    break;
                case LEFT:
                    overridePendingTransition(R.anim.no_anim, R.anim.left_out);
                    break;
                case RIGHT:
                    overridePendingTransition(R.anim.no_anim, R.anim.right_out);
                    break;
                case SCALE:
                    overridePendingTransition(R.anim.no_anim, R.anim.scale_out);
                    break;
                case FADE:
                    overridePendingTransition(R.anim.no_anim, R.anim.fade_out);
                    break;
            }
        }

    }

    /**
     * 获得bundle data
     *
     * @param extras
     */
    protected void getBundleExtras(Bundle extras) {
    }

    /**
     * 与布局资源进行绑定
     *
     * @return 返回当前布局id
     */
    protected abstract int getContentViewLayoutID();


    /**
     * 是否有切换activity动画
     *
     * @return
     */
    protected boolean toggleOverridePendingTransition() {
        return false;
    }

    /**
     * 获得切换动画的模式
     *
     * @return
     */
    protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }


    public Handler getHandler() {
        return new Handler(getMainLooper());
    }


    /**
     * 跳转到相应的activity（没有带任何信息）
     *
     * @param clazz
     */
    protected void readyGo(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    /**
     * 跳转到相应的activity，携带bundle数据
     *
     * @param clazz
     * @param bundle
     */
    protected void readyGo(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    /**
     * 跳转到相应的activity，然后干掉自己
     *
     * @param clazz
     */
    protected void readyGoThenKill(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        finish();
    }

    /**
     * 跳转到相应的activity并携带bundle数据，然后干掉自己
     *
     * @param clazz
     * @param bundle
     */
    protected void readyGoThenKill(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        finish();
    }

    /**
     * 跳转到相应activity，并接受回传值
     *
     * @param clazz
     * @param requestCode
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(this, clazz);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转到相应activity，并带参数
     *
     * @param clazz
     * @param requestCode
     * @param bundle
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 显示内容
     */
    public void toggleShowContent() {
        mMultipleStatusLayout.showContent();
    }

    public void hideLoadingView() {
        mMultipleStatusLayout.hideLoadView();
    }

    /**
     * 显示没有数据
     */
    public void toggleShowEmpty() {
        toggleShowEmpty(R.drawable.ic_no_data, getString(R.string.empty_data_message));
    }

    /**
     * 显示没有数据
     *
     * @param message 提示信息
     */
    public void toggleShowEmpty(String message) {
        toggleShowEmpty(R.drawable.ic_no_data, message);
    }

    /**
     * 显示没有数据
     *
     * @param drawId  图片id
     * @param message 提示信息
     */
    public void toggleShowEmpty(@DrawableRes int drawId, String message) {
        mMultipleStatusLayout.showEmpty(drawId, message);
    }

    /**
     * 显示正在加载
     */
    public void toggleShowLoading() {
        mMultipleStatusLayout.showLoading();
    }

    /**
     * 显示网络异常
     */
    public void toggleShowNetError(View.OnClickListener listener) {
        toggleShowNetError(getString(R.string.no_network_message), listener);
    }

    /**
     * 显示网络异常
     *
     * @param msg      提示信息
     * @param listener 提示信息
     */
    public void toggleShowNetError(String msg, View.OnClickListener listener) {
        toggleShowNetError(R.drawable.ic_no_data, msg, getString(R.string.click_refresh), listener);
    }

    /**
     * 显示网络异常
     *
     * @param drawId          图片id
     * @param msg             提示信息
     * @param errorButtonText 按钮文字
     * @param listener        点击监听
     */
    public void toggleShowNetError(int drawId, String msg, String errorButtonText, View.OnClickListener listener) {
        mMultipleStatusLayout.showError(drawId, msg, errorButtonText, listener);
    }

    /**
     * 初始化状态栏颜色
     *
     * @param color
     * @param isDark
     */
    protected void initStatusColor(@ColorInt int color, boolean isDark) {
        StatusBarUtils.setStatusColor(getWindow(), color);
        StatusBarUtils.setStatusIconColor(getWindow(), isDark);
    }


    protected void showKeyboard(boolean isShow) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShow) {
            if (getCurrentFocus() == null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            } else {
                imm.showSoftInput(getCurrentFocus(), 0);
            }
        } else {
            if (getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
            getHandler().postDelayed(() -> finish(), 200);
        }
    }

}