package com.majick.guohanhealth.bean;

import java.util.List;

public class GoodsOrderInfo {
    public String freight_hash;
    public AddressInfo address_info;
    public String ifshow_offpay;
    public String ifchain;
    public String ifshow_chainpay;
    public String chain_store_id;
    public String ifshow_inv;
    public String vat_hash;
    public Inv_info inv_info;
    public List<String> order_date_list;
    public String order_date_msg;
    public Object available_predeposit;
    public Object available_rc_balance;
    public List<Object> rpt_list;
    public List<Object> rpt_info;
    public String order_amount;
    public Address_api address_api;
    public Object store_final_total_list;

    public static class Inv_info {
        public String content;
    }

    public static class Address_api {
        public String state;
        public Object content;
        public List<Object> no_send_tpl_ids;
        public String allow_offpay;
        public List<Object> allow_offpay_batch;
        public String offpay_hash;
        public String offpay_hash_batch;
    }



}
