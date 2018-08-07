package com.guohanhealth.shop.bean;

import java.util.List;

public class HistoryInfo {
    public List<DataBean> goodsbrowse_list;

    public static class DataBean {
        public String browsetime;
        public String browsetime_day;
        public String browsetime_text;
        public String gc_id;
        public String gc_id_1;
        public String gc_id_2;
        public String gc_id_3;
        public String goods_id;
        public String goods_image;
        public String goods_image_url;
        public String goods_marketprice;
        public String goods_name;
        public String goods_promotion_price;
        public String goods_promotion_type;
        public String store_id;
    }
}
