package com.guohanhealth.shop.bean;

import java.util.List;

public class EvaluationInfo extends BaseInfo {
    public StoreInfo store_info;

    public static class StoreInfo {
        public String store_id;
        public String store_name;
        public String is_own_shop;
    }

    public List<DataBean> order_goods;

    public static class DataBean {
        public String rec_id;
        public String order_id;
        public String goods_id;
        public String goods_name;
        public String goods_price;
        public String goods_num;
        public String goods_image;
        public String goods_pay_price;
        public String store_id;
        public String buyer_id;
        public String goods_type;
        public String promotions_id;
        public String commis_rate;
        public String gc_id;
        public String goods_contractid;
        public String goods_commonid;
        public String add_time;
        public String is_dis;
        public String dis_commis_rate;
        public String dis_member_id;
        public String goods_grade;
        public String goods_image_url;
    }

}
