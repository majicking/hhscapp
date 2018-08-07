package com.guohanhealth.shop.bean;

import java.util.List;

public class GoodsCollectInfo {
   public List<DataBean> favorites_list;

    public static class DataBean {
        public String fav_id;
        public String goods_id;
        public String goods_image;
        public String goods_image_url;
        public String goods_name;
        public String goods_price;
        public String store_id;
    }
}
