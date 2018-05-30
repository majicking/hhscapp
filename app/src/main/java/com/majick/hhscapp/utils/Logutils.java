package com.majick.hhscapp.utils;

import android.text.TextUtils;
import android.util.Log;

import com.orhanobut.logger.Logger;

public class Logutils {


    public static void i(Object content) {
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.i(tag, "" + content+" ▆▅▃▂▁");
    }

    public static void i(String content) {
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag1(caller);
        Log.i(tag, content);
    }

    public static String customTagPrefix = "";

    private static String generateTag(StackTraceElement caller) {
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        tag = TextUtils.isEmpty(customTagPrefix) ? "▁▂▃▅▆ "+tag : customTagPrefix + ":" + tag;
        return tag;
    }
    private static String generateTag1(StackTraceElement caller) {
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        tag = TextUtils.isEmpty(customTagPrefix) ? "---▶►►"+tag : customTagPrefix + ":" + tag;
        return tag;
    }

    public static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }
}
