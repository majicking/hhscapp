package com.majick.guohanhealth.app;

import com.majick.guohanhealth.BuildConfig;

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
    String URLHEAD = HEAD + HOST + ":" + PORT + APP;
    String INDEX = "index.php?";
    String URL = URLHEAD + INDEX;
    String CHAT_SERVER_URL = HEAD + HOST + ":33/socket.io/socket.io.js?_=1524556674600";
    /**
     * 显示验证图片
     */
    String KEYCODEURL = URL + "act=seccode&op=makecode&k=";
    /**
     * 用户注册协议
     * http://www.guohanhealth.com/wap/tmpl/member/document.html
     */
    String REGISTERARGMENT = HEAD + HOST + ":" + PORT + "/wap/tmpl/member/document.html";
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
    /**
     * 注册页面参数传递
     */
    String PHONE = "phone";
    String CAPTCHA = "captcha";
    /**
     * 保存用户信息
     */
    String USERNAME = "userName";
    String USERID = "userId";
    String KEY = "key";
    String MEMBER_ID="member_id;";
    String MEMBER_NAME="member_name;";
    String MEMBER_AVATAR="member_avatar;";
    String STORE_NAME="store_name;";
    String GRADE_ID="grade_id;";
    String STORE_ID="store_id;";
    String SELLER_NAME="seller_name;";
}
