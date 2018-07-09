package com.guohanhealth.shop.bean;

import java.util.List;

public class UpDataAddressInfo  extends BaseInfo{
    public String state;
    public Content content;

    public static class Content {

    }

    public List<No_send_tpl_ids> no_send_tpl_ids;

    public static class No_send_tpl_ids {

    }

    public String allow_offpay;
    public List<Object> allow_offpay_batch;

    public static class Allow_offpay_batch {

    }

    public String offpay_hash;
    public String offpay_hash_batch;
}
