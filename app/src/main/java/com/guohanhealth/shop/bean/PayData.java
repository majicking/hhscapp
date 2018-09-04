package com.guohanhealth.shop.bean;

import java.util.List;

public class PayData {
    public PayInfo pay_info;

    public static class PayInfo {
        public String pay_amount;
        public String payed_healthbean_amount;
        public String member_available_pd;
        public String member_available_rcb;
        public String is_consume;
        public String member_available_healthbean;
        public String healthbean_allow;
        public String predeposit_allow;
        public String rc_balance_allow;
        public boolean member_paypwd;
        public String pay_sn;
        public String payed_amount;
        public List<DataBean> payment_list;

        public static class DataBean {
            public String payment_code;
            public String payment_name;
            public String payment_status;
        }


    }
}
