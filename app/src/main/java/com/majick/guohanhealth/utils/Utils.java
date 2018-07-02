package com.majick.guohanhealth.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.majick.guohanhealth.app.App;
import com.majick.guohanhealth.app.Constants;
import com.majick.guohanhealth.custom.CustomPopuWindow;
import com.majick.guohanhealth.ui.WebViewActivity;
import com.majick.guohanhealth.ui.goods.goodsdetailed.activity.GoodsDetailsActivity;
import com.majick.guohanhealth.ui.login.LoginActivity;
import com.majick.guohanhealth.ui.search.SearchActivity;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * 2018/4/27 15:14
 * Created  by:
 * MailAddress：majick@aliyun.com
 */
public class Utils {
    /**
     * 获取当前类上的泛型参数，并实例化
     *
     * @param object 当前类
     * @param index  类泛型参数角标
     * @param <T>    泛型实例化对象
     * @return 泛型实例化对象
     */
    public static <T> T getGenericInstance(Object object, int index) {
        try {
            ParameterizedType type = (ParameterizedType) (object.getClass().getGenericSuperclass());//获取当前类的父类泛型参数
            Class<T> clazz = (Class) (type.getActualTypeArguments()[index]);//获取泛型class
            return clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {

        }
        return null;
    }


    public static boolean isEmpty(EditText editText) {
        if (editText != null) {
            String string = editText.getText().toString().trim();
            return isEmpty(string);
        }
        return false;
    }

    public static boolean isEmpty(List<?> list) {
        if (list != null && list.size() > 0)
            return true;
        return false;
    }

    public static boolean isEmpty(Object[] list) {
        if (list != null && list.length > 0)
            return true;
        return false;
    }

    public static boolean isEmpty(TextView textView) {
        if (textView != null) {
            String string = textView.getText().toString().trim();
            return isEmpty(string);
        }
        return false;
    }


    public static boolean isEmpty(String string) {
        if (string != null && string.length() > 0 && !"".equals(string) && !"[]".equals(string) && !"null".equals(string)) {
            return true;
        }
        return false;
    }

    public static String getEditViewText(EditText editText) {
        if (editText != null) {
            return isEmpty(editText.getText().toString().trim()) ? editText.getText().toString().trim() : "";
        }
        return "";
    }

    public static String getTextViewText(TextView textView) {
        if (textView != null) {
            return isEmpty(textView.getText().toString().trim()) ? textView.getText().toString().trim() : "";
        }
        return "";
    }

    /**
     * 相当于三元运算
     */
    public static String getString(String string) {
        if (isEmpty(string))
            return string;
        return "";
    }

    /**
     * 检查是否已登录，如果未登录直接弹出登录页面
     */
    public static boolean isLogin(Context context) {
        if (isEmpty(App.getApp().getKey()) && App.getApp().isLogin())
            return true;
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        return false;
    }


    public static void GoToType(Context context, String type, String data) {
        switch (type) {
            case Constants.KEYWORD:
                Intent intent = new Intent(context, SearchActivity.class);
                intent.putExtra(Constants.KEYWORD, data);
                intent.putExtra("gc_name", data);
                context.startActivity(intent);
                break;
            case "special"://专题编号
                Intent intent1 = new Intent(context, WebViewActivity.class);
                intent1.putExtra("url", Constants.URL + "act=index&op=special&special_id=" + data + "&type=html");
                context.startActivity(intent1);
                break;
            case "goods":
                //商品编号
                Intent intent2 = new Intent(context, GoodsDetailsActivity.class);
                intent2.putExtra(Constants.GOODS_ID, data);
                context.startActivity(intent2);
                break;
            case "url":
                //地址
                Intent intent3 = new Intent(context, WebViewActivity.class);
                intent3.putExtra("url", data);
                context.startActivity(intent3);
                break;
        }
    }


    public static CustomPopuWindow getPopuWindown(Context mContext, View view, int point) {
        CustomPopuWindow popuWindow = new CustomPopuWindow.PopupWindowBuilder(mContext)
                .setView(view)
                .size(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                .setFocusable(true).setOnDissmissListener(() -> {
                    WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
                    lp.alpha = 1; //0.0-1.0
                    ((Activity) mContext).getWindow().setAttributes(lp);
                }).create().setBackgroundDrawable((Drawable) null)
                .showAtLocation(view, point, 0, 0);
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = 0.4f; //0.0-1.0
        ((Activity) mContext).getWindow().setAttributes(lp);
        return popuWindow;
    }

}
