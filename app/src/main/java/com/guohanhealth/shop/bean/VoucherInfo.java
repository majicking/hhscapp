package com.guohanhealth.shop.bean;

import java.util.List;

public class VoucherInfo extends BaseInfo {
    public List<DataBean> voucher_list;

    public static class DataBean {
        public String voucher_id;
        public String voucher_code;
        public String voucher_title;
        public String voucher_desc;
        public String voucher_start_date;
        public String voucher_end_date;
        public String voucher_price;
        public String voucher_limit;
        public String voucher_state;
        public String voucher_order_id;
        public String voucher_store_id;
        public String store_name;
        public String store_id;
        public String store_domain;
        public String store_label;
        public String store_avatar;
        public String is_own_shop;
        public String voucher_t_customimg;
        public String store_logo_url;
        public String store_avatar_url;
        public String voucher_state_text;
        public String voucher_start_date_text;
        public String voucher_end_date_text;
    }
}
