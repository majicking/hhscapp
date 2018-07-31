package com.guohanhealth.shop.app;

import android.graphics.Color;

import com.guohanhealth.shop.BuildConfig;

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
    //    String HOST = "test.shopnctest.com";
//    String HOST = BuildConfig.HOST;
    String HOST = "www.guohanhealth.com";
    String PORT = BuildConfig.PORT;
    String APP = BuildConfig.APP;
    String WEB = BuildConfig.WEB;
    String URLHEAD = HEAD + HOST + ":" + PORT + APP;
    String URLHEAD_WEB = HEAD + HOST + ":" + PORT + WEB;
    String INDEX = "index.php?";
    String URL = URLHEAD + INDEX;
    String IM_HOST = HEAD + HOST + ":33";

    String WX_APP_ID = BuildConfig.WX_APP_ID;
    String WX_APP_SECRET = BuildConfig.WX_APP_SECRET;

    //新浪key
    String WEIBO_APP_KEY = BuildConfig.WEIBO_APP_KEY;
    String WEIBO_APP_SECRET = BuildConfig.WEIBO_APP_SECRET;
    String WEIBO_REDIRECT_URL = BuildConfig.HOST + "/data/resource/api/sina/return_url.php";

    //QQ APPID
    String QQ_APP_ID = BuildConfig.QQ_APP_ID;
    String QQ_APP_KEY = BuildConfig.QQ_APP_KEY;
    /**
     * 相机权限请求
     */
    int REQUEST_CAMERA = 1;
    /**
     * 选择地址
     */
    int SELECTADDRESS = 1;
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

    /**
     * 显示验证图片
     */
    String KEYCODEURL = URL + "act=seccode&op=makecode&k=";
    /**
     * 用户注册协议
     */
    String REGISTERARGMENT = HEAD + HOST + ":" + PORT + "/wap/tmpl/member/document.html";

    String WAP_BRAND_ICON = URLHEAD_WEB + "/images/degault.png";
    /**
     * 常量 标识
     * 页面参数传递
     */
    String MAINNUMBER = "main_number";
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
    String KEYWORD = "keyword";
    String B_ID = "b_id";
    String GC_ID = "gc_id";
    String GOODS_ID = "goods_id";
    String GETGOODSID = "getGoods_id";
    String CARDNUMBER = "CardNumber";
    String SHOWORDER = "showOrder";
    String GETGOODSDETAILS = "getGoodsDetails";
    String CURRENTITEM = "currentitem";
    String ERROR = "error";
    String IFCART = "ifcart";
    String CART_ID = "cart_id";
    String GOODS_NUMBER = "goods_number";
    String IS_VIRTUAL = "is_virtual";
    String ORDERINDEX = "orderindex";
    String CART_COUNT = "cart_count";
    String ORDERTYPE = "ordertype";
    String ORDER_ID = "order_id";
    String DATA = "datas";
    String PROID = "proid";
    String BALANCENUMBER = "balancenumber";
    String UPDATAMONEY = "updatamoney";
    String UPDATA = "updata";

}
