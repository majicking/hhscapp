package com.guohanhealth.shop.utils;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Description: 系统状态栏帮助类
 */


public class StatusBarUtils {

    /**
     * 设置状态栏透明
     *
     * @param window                 window对象
     * @param isTransparentStatusBar
     */
    public static void setStatusTransparent(Window window, boolean isTransparentStatusBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {//4.4 - 5.0
            processKitKatTrans(window, isTransparentStatusBar);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            processLollipopTrans(window);
        }
    }

    /**
     * 处理4.4版本-5.0版本的透明状态栏
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void processKitKatTrans(Window window, boolean isTransparentStatusBar) {
        if (isTransparentStatusBar) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 处理5.0以上的透明状态栏
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void processLollipopTrans(Window window) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);

    }

    /**
     * 设置状态栏颜色
     *
     * @param window         window 对象
     * @param statusBarColor 颜色
     */
    public static void setStatusColor(Window window, @ColorInt int statusBarColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {//4.4 - 5.0
            processKitKatTrans(window, true);
            processStatusWithCover(window, statusBarColor);//4.4版本设置透明状态栏，加的遮罩层，遮罩层最好颜色透明
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            processLollipopStatusColor(window, statusBarColor);
        }
    }

    /**
     * 给状态栏加上遮罩层(前提是状态栏是透明的)
     * 我们认为的沉浸式 一般情况下是先让状态栏透明然后设置遮罩层 一般情况下需要和fitsWindowSystem 一起使用
     *
     * @param window
     * @param tintStatusBarColor 遮罩颜色
     */
    private static void processStatusWithCover(Window window, int tintStatusBarColor) {
        View statusBarTintView = new View(window.getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                ScreenUtils.getStatusHeight(window.getContext()));
        params.gravity = Gravity.TOP;
        statusBarTintView.setLayoutParams(params);
        if (tintStatusBarColor != -1) {
            statusBarTintView.setBackgroundColor(tintStatusBarColor);
        }

        ViewGroup decorView = (ViewGroup) window.getDecorView();
        decorView.addView(statusBarTintView);
        decorView.setClipToPadding(true);
        ViewCompat.setFitsSystemWindows(decorView, true);
    }

    /**
     * 处理5.0以上的状态栏颜色
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void processLollipopStatusColor(Window window, @ColorInt int statusBarColor) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(statusBarColor);
    }

    /**
     * 设置状态栏中的图标与文字的颜色
     *
     * @param isDark 是否是深色
     */
    public static void setStatusIconColor(Window window, boolean isDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (setFlymeStatusBarLightMode(window, isDark)) {
                return;
            } else if (setMIUIStatusBarLightMode(window, isDark)) {
                return;
            } else {
                View decor = window.getDecorView();
                int ui = decor.getSystemUiVisibility();
                if (isDark) {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    ui |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    ui &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                decor.setSystemUiVisibility(ui);
            }
        } else {//6.0以下的只有变颜色了
            if (isDark) {
                processLollipopStatusColor(window, Color.parseColor("#4d000000"));
            }
        }
    }


    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param isDark 是否把状态栏字体及图标颜色设置为深色
     */
    public static boolean setFlymeStatusBarLightMode(Window window, boolean isDark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (isDark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception ignored) {

            }
        }
        return result;
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param isDrak 是否把状态栏字体及图标颜色设置为深色
     */
    public static boolean setMIUIStatusBarLightMode(Window window, boolean isDrak) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (isDrak) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception ignored) {

            }
        }
        return result;
    }


    /**
     * 处理状态栏，通过设置padding的方法
     */
    public static void processStatusImmersion(final View actionBarView) {
        if (actionBarView == null) {
            throw new IllegalArgumentException("'actionBarView' cannot be null.");
        }
        actionBarView.post(new Runnable() {
            @Override
            public void run() {
                actionBarView.setPadding(actionBarView.getPaddingLeft(),
                        actionBarView.getPaddingTop() + ScreenUtils.getStatusHeight(actionBarView.getContext()),
                        actionBarView.getPaddingRight(),
                        actionBarView.getPaddingBottom());
                actionBarView.getLayoutParams().height += ScreenUtils.getStatusHeight(actionBarView.getContext());

            }
        });
    }


}
