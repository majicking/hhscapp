package com.guohanhealth.shop.bean;

import java.util.List;

public class ReturnInfo extends BaseInfo {
    public List<String> detail_array;
    public List<String> pic_list;
    public Refund refund;
    public static class Refund {
        public String refund_id;
        public String goods_id;
        public String goods_name;
        public String order_id;
        public String refund_amount;
        public String refund_sn;
        public String order_sn;
        public String add_time;
        public String goods_img_360;
        public String seller_state;
        public String admin_state;
        public String store_id;
        public String store_name;
        public String reason_info;
        public String buyer_message;
        public String seller_message;
        public String admin_message;
    }
}
