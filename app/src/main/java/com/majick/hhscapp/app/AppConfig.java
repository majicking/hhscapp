package com.majick.hhscapp.app;


import com.majick.hhscapp.bean.LoginBean;
import com.majick.hhscapp.bean.UserInfo;

/**
 * Description:应用配置
 */


public class AppConfig {

    private static LoginBean loginBean;
    private static UserInfo UserInfo;

    public static LoginBean getLoginBean() {
        return loginBean;
    }

    public static void setLoginBean(LoginBean loginBean) {
        AppConfig.loginBean = loginBean;
    }

    public static UserInfo getUserInfo() {
        return UserInfo;
    }

    public static void setUserInfo(UserInfo UserInfo) {
        AppConfig.UserInfo = UserInfo;
    }
}
