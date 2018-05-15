package com.majick.hhscapp.app;


import com.majick.hhscapp.bean.LoginBean;
import com.majick.hhscapp.bean.UserInfo;

/**
 * Description:应用配置
 */


public class AppConfig {

    private static LoginBean loginBean;
    private static UserInfo userInfo;

    public static LoginBean getLoginBean() {
        return loginBean;
    }

    public static void setLoginBean(LoginBean loginBean) {
        AppConfig.loginBean = loginBean;
    }

    public static UserInfo getUserInfo() {
        return userInfo;
    }

    public static void setUserInfo(UserInfo userInfo) {
        AppConfig.userInfo = userInfo;
    }
}
