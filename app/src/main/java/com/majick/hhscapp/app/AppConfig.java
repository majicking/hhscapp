package com.majick.hhscapp.app;


import com.majick.hhscapp.bean.LoginBean;
import com.majick.hhscapp.bean.UserInfo;

/**
 * Description:应用配置
 */


public class AppConfig {


    private static String key;
    private static String userid;
    private static String username;

    private static UserInfo.Member_info info;

    public static UserInfo.Member_info getInfo() {
        return info;
    }

    public static void setInfo(UserInfo.Member_info info) {
        AppConfig.info = info;
    }

    public static String getKey() {
        return key;
    }

    public static void setKey(String key) {
        AppConfig.key = key;
    }

    public static String getUserid() {
        return userid;
    }

    public static void setUserid(String userid) {
        AppConfig.userid = userid;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        AppConfig.username = username;
    }
}
