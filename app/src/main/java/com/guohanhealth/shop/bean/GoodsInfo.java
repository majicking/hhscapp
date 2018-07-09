package com.guohanhealth.shop.bean;

import java.util.List;

public class GoodsInfo  extends BaseInfo{
    public String title;
    public List<Item> item;

    public static class Item {
        public String goods_id;
        public String goods_name;
        public String goods_promotion_price;
        public String goods_image;
    }

}
