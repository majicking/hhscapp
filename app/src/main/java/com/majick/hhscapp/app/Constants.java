package com.majick.hhscapp.app;

import com.majick.hhscapp.BuildConfig;

public interface Constants {
    /**
     * 接口
     *
     * */
    /**
     * 服務器地址
     */

    String HEAD = BuildConfig.HEAD;
    String HOST = BuildConfig.HOST;
    String PORT = BuildConfig.PORT;
    String APP = BuildConfig.APP;
    String URLHEAD = HEAD + HOST + ":"+PORT + APP;
    String INDEX = "index.php?";
    String URL = URLHEAD + INDEX;
    String CHAT_SERVER_URL =  HEAD + HOST+":33/socket.io/socket.io.js?_=1524556674600";

    /**
     *
     * 常量 标识
     *
     * */

    /**
     * 首页跳转
     */
    String MAINNUMBER = "main_number";
    /**
     * 底部栏购物车数量
     */
    String BROADCAST_CARTNUMBER = "1";


}
