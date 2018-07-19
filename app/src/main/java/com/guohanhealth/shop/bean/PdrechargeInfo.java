package com.guohanhealth.shop.bean;

import java.util.List;

public class PdrechargeInfo  extends  BaseInfo {
    public List<DataBean> list;

    public static class DataBean {
        public String pdr_id;
        public String pdr_sn;
        public String pdr_member_id;
        public String pdr_member_name;
        public String pdr_amount;
        public String pdr_payment_code;
        public String pdr_payment_name;
        public String pdr_trade_sn;
        public String pdr_add_time;
        public String pdr_payment_state;
        public String pdr_payment_time;
        public String pdr_admin;
        public String pdr_add_time_text;
        public String pdr_payment_state_text;
    }
}
