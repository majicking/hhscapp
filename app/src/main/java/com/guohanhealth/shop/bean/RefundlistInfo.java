package com.guohanhealth.shop.bean;

import java.util.List;

public class RefundlistInfo extends BaseInfo {
    public List<DataBean> refund_list;

    public static class DataBean {
        public String refund_id;
        public String order_id;
        public String refund_amount;
        public String refund_sn;
        public String order_sn;
        public String add_time;
        public String seller_state_v;
        public String seller_state;
        public String admin_state_v;
        public String admin_state;
        public String store_id;
        public String store_name;
        public List<DataInfo> goods_list;

        public static class DataInfo {
            public String goods_id;
            public String goods_name;
            //            public Object goods_spec;
            public String goods_img_360;
        }
    }
}
