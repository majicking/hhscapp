package com.guohanhealth.shop.bean;

public class MineInfo  extends BaseInfo{
    public Member_info member_info;

    public class Member_info {
        public String user_name;
        public String avatar;
        public String level_name;
        public String favorites_store;
        public String favorites_goods;
        public String order_nopay_count;
        public String order_noreceipt_count;
        public String order_notakes_count;
        public String order_noeval_count;
        public String returns;
        public String is_distri;
        public String is_movie;
        public String is_movie_msg;
    }
}
