package com.majick.guohanhealth.utils;

import android.text.TextUtils;
import android.widget.EditText;

import java.lang.reflect.ParameterizedType;

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


    public static boolean isEmpty(String string) {
        if (string != null && string.length() > 0 && !"".equals(string) && !"[]".equals(string) && !"null".equals(string)) {
            return true;
        }
        return false;
    }

    public static String getEditText(EditText editText) {
        if (editText != null) {
            return TextUtils.isEmpty(editText.getText().toString().trim())? "" : editText.getText().toString().trim();
        }
        return "";
    }
}
