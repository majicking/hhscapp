package com.majick.guohanhealth.bean;

import android.graphics.Color;

import com.majick.guohanhealth.app.Constants;

import java.util.ArrayList;
import java.util.List;

public class HomeMenuBtn extends BaseInfo{
    public String title;
    public String icon;
    public int backgroundCoror;

    public HomeMenuBtn(String title, String icon, int backgroundCoror) {
        this.title = title;
        this.icon = icon;
        this.backgroundCoror = backgroundCoror;
    }

    public static List<HomeMenuBtn> getHomeBtn() {
        List<HomeMenuBtn> list = new ArrayList<>();
        list.add(new HomeMenuBtn("分类", Constants.URLHEAD_WEB + "images/browse_list_w.png", Color.parseColor("#FB6E52")));
        list.add(new HomeMenuBtn("购物车", Constants.URLHEAD_WEB + "images/cart_w.png", Color.parseColor("#48CFAE")));
        list.add(new HomeMenuBtn("店铺街", Constants.URLHEAD_WEB + "images/mcc_01a.png", Color.parseColor("#4FC0E8")));
        list.add(new HomeMenuBtn("每日签到", Constants.URLHEAD_WEB + "images/mcc_04_w.png", Color.parseColor("#AC92ED")));
        list.add(new HomeMenuBtn("我的商城", Constants.URLHEAD_WEB + "images/member_w.png", Color.parseColor("#FF9300")));
        list.add(new HomeMenuBtn("我的订单", Constants.URLHEAD_WEB + "images/mcc_07_w.png", Color.parseColor("#62BA1E")));
        list.add(new HomeMenuBtn("我的财产", Constants.URLHEAD_WEB + "images/mcc_06_w.png", Color.parseColor("#1A8DE5")));
        list.add(new HomeMenuBtn("我的足迹", Constants.URLHEAD_WEB + "images/goods-browse_w.png", Color.parseColor("#EC87BF")));
        return list;
    }
}
