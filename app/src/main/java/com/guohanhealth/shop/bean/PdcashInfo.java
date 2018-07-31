package com.guohanhealth.shop.bean;

import java.util.List;

public class PdcashInfo {
    public List<DataBean> list;

    public static class DataBean {
        public String pdc_id;
        public String pdc_sn;
        public String pdc_member_id;
        public String pdc_member_name;
        public String pdc_amount;
        public String pdc_bank_name;
        public String pdc_bank_no;
        public String pdc_bank_user;
        public String mobilenum;
        public String pdc_add_time;
        public String pdc_payment_time;
        public String pdc_payment_state;
        public String pdc_payment_admin;
        public String pdc_add_time_text;
        public String pdc_payment_time_text;
        public String pdc_payment_state_text;
    }
}
