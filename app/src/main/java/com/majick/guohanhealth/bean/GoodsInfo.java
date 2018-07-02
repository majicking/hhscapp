package com.majick.guohanhealth.bean;

import java.util.List;

public class GoodsInfo {
    public String title;
    public List<Item> item;

    public static class Item {
        public String goods_id;
        public String goods_name;
        public String goods_promotion_price;
        public String goods_image;
    }

}
