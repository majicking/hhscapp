package com.guohanhealth.shop.bean;

import android.graphics.Color;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.Constants;

import java.util.ArrayList;
import java.util.List;

public class HomeMenuBtn extends BaseInfo {
    public String title;
    public String icon;

    public HomeMenuBtn(String title, String icon) {
        this.title = title;
        this.icon = icon;
    }

//    public HomeMenuBtn(String title, int icon) {
//        this.title = title;
//        this.icon = icon;
//    }

    public static List<HomeMenuBtn> getHomeBtn() {
        List<HomeMenuBtn> list = new ArrayList<>();
        list.add(new HomeMenuBtn("分类", Constants.URLHEAD_WEB + "images/browse_list_w_1.png"));
        list.add(new HomeMenuBtn("购物车", Constants.URLHEAD_WEB + "images/cart_w_1.png"));
        list.add(new HomeMenuBtn("店铺街", Constants.URLHEAD_WEB + "images/mcc_01a_1.png"));
        list.add(new HomeMenuBtn("每日签到", Constants.URLHEAD_WEB + "images/mcc_04_w_1.png"));
        list.add(new HomeMenuBtn("我的商城", Constants.URLHEAD_WEB + "images/member_w_1.png"));
        list.add(new HomeMenuBtn("我的订单", Constants.URLHEAD_WEB + "images/mcc_07_w_1.png"));
        list.add(new HomeMenuBtn("我的财产", Constants.URLHEAD_WEB + "images/mcc_06_w_1.png"));
        list.add(new HomeMenuBtn("我的足迹", Constants.URLHEAD_WEB + "images/goods-browse_w_1.png"));
//        list.add(new HomeMenuBtn("分类", R.mipmap.index_circle_category));
//        list.add(new HomeMenuBtn("积分兑换", R.mipmap.index_circle_bonuspoint));
//        list.add(new HomeMenuBtn("店铺街", R.mipmap.index_circle_store));
//        list.add(new HomeMenuBtn("每日签到", R.mipmap.index_circle_signeveryday));
//        list.add(new HomeMenuBtn("我的商城", R.mipmap.index_circle_mine));
//        list.add(new HomeMenuBtn("我的订单", R.mipmap.index_circle_order));
//        list.add(new HomeMenuBtn("我的财产", R.mipmap.index_circle_money));
//        list.add(new HomeMenuBtn("我的足迹", R.mipmap.index_circle_history));
        return list;
    }
}
