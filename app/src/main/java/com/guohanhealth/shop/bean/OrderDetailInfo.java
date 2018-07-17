package com.guohanhealth.shop.bean;

import java.util.List;

public class OrderDetailInfo extends BaseInfo {
    public OrderBean order_info;

    public static class OrderBean {

        public String order_id;
        public String order_sn;
        public String store_id;
        public String store_name;
        public String add_time;
        public String payment_time;
        public String shipping_time;
        public String finnshed_time;
        public String order_amount;
        public String shipping_fee;
        public String real_pay_amount;
        public String state_desc;
        public String payment_namel;
        public String payment_name;
        public String order_message;
        public String reciver_phone;
        public String reciver_name;
        public String reciver_addr;
        public String store_member_id;
        public String store_qq;
        public boolean node_chat;
        public String store_phone;
        public String order_tips;
        public String invoice;
        public boolean if_deliver;
        public boolean if_buyer_cancel;
        public boolean if_refund_cancel;
        public boolean if_receive;
        public boolean if_evaluation;
        public boolean if_again;
        public boolean if_lock;
        public boolean ownshop;
        public String again;
        public String again_time;
        public List<Goods_list> goods_list;
        public List<Promotion> promotion;
        public List<Zengpin_list> zengpin_list;

        public static class Promotion {
        }


        public static class Goods_list {
            public String rec_id;
            public String goods_id;
            public String goods_name;
            public String goods_price;
            public String goods_num;
            public String image_url;
            public int refund;
        }


        public static class Zengpin_list {

        }

    }
}
