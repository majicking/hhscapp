package com.guohanhealth.shop.bean;

import java.util.List;

public class OrderInfo extends BaseInfo {

    public List<OrderGroupListBean> order_group_list;


    public static class OrderGroupListBean {
        /**
         * order_list : [{"order_id":"181","order_sn":"1000000000017101","pay_sn":"240584587591641617","pay_sn1":null,"store_id":"35","store_name":"五粮液三杯爽品牌旗舰店","buyer_id":"617","buyer_name":"majick","buyer_email":"majick@aliyun.com","buyer_phone":"18888888888","add_time":"1531243591","payment_code":"online","payment_time":"0","finnshed_time":"0","goods_amount":"4422.00","order_amount":"4422.00","rcb_amount":"0.00","pd_amount":"0.00","shipping_fee":"0.00","evaluation_state":"0","evaluation_again_state":"0","order_state":"10","refund_state":"0","lock_state":"0","delete_state":"0","refund_amount":"0.00","delay_time":"0","order_from":"2","shipping_code":"","order_type":"1","api_pay_time":"1531243593","chain_id":"0","chain_code":"0","rpt_amount":"0.00","trade_no":null,"is_dis":"0","trade_in":"0","healthbean_amount":"0.00","again":"0","again_time":null,"state_desc":"待付款","payment_name":"在线付款","extend_order_goods":[{"goods_id":"102849","goods_name":"五粮液-三杯爽·珍爽白酒 38º 500ml双瓶装 赠送二瓶小光瓶45º*125ml","goods_price":"836.00","goods_num":"1","goods_type":"1","goods_spec":null,"goods_commonid":"101580","add_time":"1531243591","is_dis":"0","dis_commis_rate":"0","dis_member_id":"0","goods_grade":"3","goods_image_url":"http://img.guohanhealth.com/shop/store/goods/35/35_05824008140473005.jpg@!product-240","refund":false},{"goods_id":"102850","goods_name":"五粮液-三杯爽·精品白酒52º、45º、38º  500ml 双瓶装 500ml 52º 双瓶装","goods_price":"396.00","goods_num":"1","goods_type":"1","goods_spec":"容量：500ml，酒精度：52º，套装：双瓶装","goods_commonid":"101581","add_time":"1531243591","is_dis":"0","dis_commis_rate":"0","dis_member_id":"0","goods_grade":"3","goods_image_url":"http://img.guohanhealth.com/shop/store/goods/35/35_05824008111098411.jpg@!product-240","refund":false},{"goods_id":"102853","goods_name":"五粮液-三杯爽（全家福）促销装（窖藏52+珍爽52º+精品52º）*2（每人限购10套）","goods_price":"3190.00","goods_num":"1","goods_type":"1","goods_spec":null,"goods_commonid":"101582","add_time":"1531243591","is_dis":"0","dis_commis_rate":"0","dis_member_id":"0","goods_grade":"4","goods_image_url":"http://img.guohanhealth.com/shop/store/goods/35/35_05824008169131179.jpg@!product-240","refund":false}],"zengpin_list":[],"if_cancel":true,"if_refund_cancel":false,"if_receive":false,"if_lock":false,"if_deliver":false,"if_evaluation":false,"if_evaluation_again":false,"if_delete":false,"if_again":false,"ownshop":false}]
         * pay_amount : 4422
         * add_time : 1531243591
         * pay_sn : 240584587591641617
         */

        public double pay_amount;
        public String add_time;
        public String pay_sn;
        public List<Order_list> order_list;


        public static class Order_list {
            /**
             * order_id : 181
             * order_sn : 1000000000017101
             * pay_sn : 240584587591641617
             * pay_sn1 : null
             * store_id : 35
             * store_name : 五粮液三杯爽品牌旗舰店
             * buyer_id : 617
             * buyer_name : majick
             * buyer_email : majick@aliyun.com
             * buyer_phone : 18888888888
             * add_time : 1531243591
             * payment_code : online
             * payment_time : 0
             * finnshed_time : 0
             * goods_amount : 4422.00
             * order_amount : 4422.00
             * rcb_amount : 0.00
             * pd_amount : 0.00
             * shipping_fee : 0.00
             * evaluation_state : 0
             * evaluation_again_state : 0
             * order_state : 10
             * refund_state : 0
             * lock_state : 0
             * delete_state : 0
             * refund_amount : 0.00
             * delay_time : 0
             * order_from : 2
             * shipping_code :
             * order_type : 1
             * api_pay_time : 1531243593
             * chain_id : 0
             * chain_code : 0
             * rpt_amount : 0.00
             * trade_no : null
             * is_dis : 0
             * trade_in : 0
             * healthbean_amount : 0.00
             * again : 0
             * again_time : null
             * state_desc : 待付款
             * payment_name : 在线付款
             * extend_order_goods : [{"goods_id":"102849","goods_name":"五粮液-三杯爽·珍爽白酒 38º 500ml双瓶装 赠送二瓶小光瓶45º*125ml","goods_price":"836.00","goods_num":"1","goods_type":"1","goods_spec":null,"goods_commonid":"101580","add_time":"1531243591","is_dis":"0","dis_commis_rate":"0","dis_member_id":"0","goods_grade":"3","goods_image_url":"http://img.guohanhealth.com/shop/store/goods/35/35_05824008140473005.jpg@!product-240","refund":false},{"goods_id":"102850","goods_name":"五粮液-三杯爽·精品白酒52º、45º、38º  500ml 双瓶装 500ml 52º 双瓶装","goods_price":"396.00","goods_num":"1","goods_type":"1","goods_spec":"容量：500ml，酒精度：52º，套装：双瓶装","goods_commonid":"101581","add_time":"1531243591","is_dis":"0","dis_commis_rate":"0","dis_member_id":"0","goods_grade":"3","goods_image_url":"http://img.guohanhealth.com/shop/store/goods/35/35_05824008111098411.jpg@!product-240","refund":false},{"goods_id":"102853","goods_name":"五粮液-三杯爽（全家福）促销装（窖藏52+珍爽52º+精品52º）*2（每人限购10套）","goods_price":"3190.00","goods_num":"1","goods_type":"1","goods_spec":null,"goods_commonid":"101582","add_time":"1531243591","is_dis":"0","dis_commis_rate":"0","dis_member_id":"0","goods_grade":"4","goods_image_url":"http://img.guohanhealth.com/shop/store/goods/35/35_05824008169131179.jpg@!product-240","refund":false}]
             * zengpin_list : []
             * if_cancel : true
             * if_refund_cancel : false
             * if_receive : false
             * if_lock : false
             * if_deliver : false
             * if_evaluation : false
             * if_evaluation_again : false
             * if_delete : false
             * if_again : false
             * ownshop : false
             */

            public String order_id;
            public String order_sn;
            public String pay_sn;
            //            public Object pay_sn1;
            public String store_id;
            public String store_name;
            public String buyer_id;
            public String buyer_name;
            public String buyer_email;
            public String buyer_phone;
            public String add_time;
            public String payment_code;
            public String payment_time;
            public String finnshed_time;
            public String goods_amount;
            public String order_amount;
            public String rcb_amount;
            public String pd_amount;
            public String shipping_fee;
            public String evaluation_state;
            public String evaluation_again_state;
            public String order_state;
            public String refund_state;
            public String lock_state;
            public String delete_state;
            public String refund_amount;
            public String delay_time;
            public String order_from;
            public String shipping_code;
            public String order_type;
            public String api_pay_time;
            public String chain_id;
            public String chain_code;
            public String rpt_amount;
            //            public Object trade_no;
            public String is_dis;
            public String trade_in;
            public String healthbean_amount;
            public String again;
            public Object again_time;
            public String state_desc;
            public String payment_name;
            public boolean if_cancel;
            public boolean if_refund_cancel;
            public boolean if_receive;
            public boolean if_lock;
            public boolean if_deliver;
            public boolean if_evaluation;
            public boolean if_evaluation_again;
            public boolean if_delete;
            public boolean if_again;
            public boolean ownshop;
            public List<Extend_order_goods> extend_order_goods;
            public List<Zengpin_list> zengpin_list;

            public static class Zengpin_list {
                public String goods_id;
                public String goods_name;
                public String goods_price;
                public String goods_num;
                public String goods_type;
                public String goods_spec;
                public String goods_commonid;
                public String add_time;
                public String is_dis;
                public String dis_commis_rate;
                public String dis_member_id;
                public String goods_grade;
                public String goods_image_url;
                public boolean refund;
            }

            public static class Extend_order_goods {
                /**
                 * goods_id : 102849
                 * goods_name : 五粮液-三杯爽·珍爽白酒 38º 500ml双瓶装 赠送二瓶小光瓶45º*125ml
                 * goods_price : 836.00
                 * goods_num : 1
                 * goods_type : 1
                 * goods_spec : null
                 * goods_commonid : 101580
                 * add_time : 1531243591
                 * is_dis : 0
                 * dis_commis_rate : 0
                 * dis_member_id : 0
                 * goods_grade : 3
                 * goods_image_url : http://img.guohanhealth.com/shop/store/goods/35/35_05824008140473005.jpg@!product-240
                 * refund : false
                 */


                public String goods_id;
                public String goods_name;
                public String goods_price;
                public String goods_num;
                public String goods_type;
                public String goods_commonid;
                public String add_time;
                public String goods_spec;
                public String is_dis;
                public String dis_commis_rate;
                public String dis_member_id;
                public String goods_grade;
                public String goods_image_url;
                public boolean refund;

                @Override
                public String toString() {
                    return "Extend_order_goods{" +
                            "goods_id='" + goods_id + '\'' +
                            ", goods_name='" + goods_name + '\'' +
                            ", goods_price='" + goods_price + '\'' +
                            ", goods_num='" + goods_num + '\'' +
                            ", goods_type='" + goods_type + '\'' +
                            ", goods_commonid='" + goods_commonid + '\'' +
                            ", add_time='" + add_time + '\'' +
                            ", goods_spec='" + goods_spec + '\'' +
                            ", is_dis='" + is_dis + '\'' +
                            ", dis_commis_rate='" + dis_commis_rate + '\'' +
                            ", dis_member_id='" + dis_member_id + '\'' +
                            ", goods_grade='" + goods_grade + '\'' +
                            ", goods_image_url='" + goods_image_url + '\'' +
                            ", refund=" + refund +
                            '}';
                }
            }
        }
    }
}
