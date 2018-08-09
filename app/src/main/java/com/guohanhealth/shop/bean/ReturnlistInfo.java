package com.guohanhealth.shop.bean;

import java.util.List;

public class ReturnlistInfo extends BaseInfo {
    public List<DataBean> return_list;

    public static class DataBean {
        public String refund_id;
        public String goods_id;
        public String goods_name;
        //        public String goods_spec;
        public String goods_num;
        public String goods_state_v;
        public String ship_state;
        public String delay_state;
        public String order_id;
        public String refund_amount;
        public String refund_sn;
        public String return_type;
        public String order_sn;
        public String add_time;
        public String goods_img_360;
        public String seller_state_v;
        public String seller_state;
        public String admin_state_v;
        public String admin_state;
        public String store_id;
        public String store_name;
    }
}
