package com.guohanhealth.shop.bean;

import java.util.List;

public class RechargeOrderInfo  extends  BaseInfo{
    /**
     * payment_list : [{"payment_code":"alipay","payment_name":"支付宝"},{"payment_code":"wxpay","payment_name":"微信H5支付"}]
     * pdinfo : {"pdr_id":"15","pdr_sn":"460585323556400000","pdr_member_id":"617","pdr_member_name":"majick","pdr_amount":"1111.00","pdr_add_time":"1531979556"}
     */
    public PdinfoBean pdinfo;
    public List<PaymentListBean> payment_list;

    public static class PdinfoBean {
        /**
         * pdr_id : 15
         * pdr_sn : 460585323556400000
         * pdr_member_id : 617
         * pdr_member_name : majick
         * pdr_amount : 1111.00
         * pdr_add_time : 1531979556
         */

        public String pdr_id;
        public String pdr_sn;
        public String pdr_member_id;
        public String pdr_member_name;
        public String pdr_amount;
        public String pdr_add_time;

    }

    public static class PaymentListBean {
        /**
         * payment_code : alipay
         * payment_name : 支付宝
         */
        public String payment_code;
        public String payment_name;

    }
}
