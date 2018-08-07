package com.guohanhealth.shop.bean;


import java.util.List;

/**
 * 抢购商品
 * */
public class Goods2Bean {
    public String title;
    public List<Item> item;
    public static class Item {
        public String goods_image;
        public String goods_name;
        public String goods_id;
        public String goods_promotion_price;
    }
}
