package com.majick.guohanhealth.app;

import android.graphics.Color;

import com.majick.guohanhealth.BuildConfig;
import com.majick.guohanhealth.R;

import java.util.Random;

public interface Constants {
    /**
     * 接口
     *
     * */
    /**
     * 服務器地址
     */

    String HEAD = BuildConfig.HEAD;
    String HOST = "test.shopnctest.com";
    //    String HOST = BuildConfig.HOST;
    String PORT = BuildConfig.PORT;
    String APP = BuildConfig.APP;
    String WEB = BuildConfig.WEB;
    String URLHEAD = HEAD + HOST + ":" + PORT + APP;
    String URLHEAD_WEB = HEAD + HOST + ":" + PORT + WEB;
    String INDEX = "index.php?";
    String URL = URLHEAD + INDEX;
    String CHAT_SERVER_URL = HEAD + HOST + ":33/socket.io/socket.io.js?_=1524556674600";
    /**
     * 显示验证图片
     */
    String KEYCODEURL = URL + "act=seccode&op=makecode&k=";
    /**
     * 用户注册协议
     */
    String REGISTERARGMENT = HEAD + HOST + ":" + PORT + "/wap/tmpl/member/document.html";

    String WAP_BRAND_ICON = HEAD + HOST + ":" + PORT + "/wap/images/degault.png";
    /**
     * 常量 标识
     * 页面参数传递
     */
    String MAINNUMBER = "main_number";
    String BROADCAST_CARTNUMBER = "1";
    String PHONE = "phone";
    String CAPTCHA = "captcha";
    String USERNAME = "userName";
    String USERID = "userId";
    String KEY = "key";
    String MEMBER_ID = "member_id;";
    String MEMBER_NAME = "member_name;";
    String MEMBER_AVATAR = "member_avatar;";
    String STORE_NAME = "store_name;";
    String GRADE_ID = "grade_id;";
    String STORE_ID = "store_id;";
    String SELLER_NAME = "seller_name;";
    String HOTNAME = "hotname;";
    String HOTVALUE = "hotvalue;";
    String KEYWORD = "keyword;";
    String GOODS_ID = "goods_id";
    String GETGOODSID = "getGoods_id";
    String CARDNUMBER = "CardNumber";
    String SHOWORDER = "showOrder";
    String GETGOODSDETAILS = "getGoodsDetails";
    String CURRENTITEM = "currentitem";
    /**
     * 相机权限请求
     */
    int REQUEST_CAMERA = 1;
    int[] BGCOLORS = new int[]{
            Color.parseColor("#ED5564"),
            Color.parseColor("#FB6E52"),
            Color.parseColor("#FFCE55"),
            Color.parseColor("#A0D468"),
            Color.parseColor("#48CFAE"),
            Color.parseColor("#4FC0E8"),
            Color.parseColor("#5D9CEC"),
            Color.parseColor("#AC92ED"),
            Color.parseColor("#EC87BF"),
            Color.parseColor("#ED5564")
    };

    int RANDOMCOLOR = BGCOLORS[new Random().nextInt(10)];

}
