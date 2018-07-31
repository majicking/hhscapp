package com.guohanhealth.shop.bean;

import java.util.List;

public class RedPagcketInfo  extends BaseInfo{
    public List<DataBean> redpacket_list;

    public static class DataBean {
        public String rpacket_id;
        public String rpacket_code;
        public String rpacket_t_id;
        public String rpacket_title;
        public String rpacket_desc;
        public String rpacket_start_date;
        public String rpacket_end_date;
        public String rpacket_price;
        public String rpacket_limit;
        public String rpacket_state;
        public String rpacket_active_date;
        public String rpacket_owner_id;
        public String rpacket_owner_name;
        public String rpacket_order_id;
        public String rpacket_pwd;
        public String rpacket_pwd2;
        public String rpacket_customimg;
        public String rpacket_customimg_url;
        public String rpacket_state_text;
        public String rpacket_state_key;
        public String rpacket_start_date_text;
        public String rpacket_end_date_text;
    }
}
