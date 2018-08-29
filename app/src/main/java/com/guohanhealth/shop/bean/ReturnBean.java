package com.guohanhealth.shop.bean;

import java.util.List;

public class ReturnBean extends BaseInfo {

    /**
     * order : {"order_id":"473","order_type":"1","order_amount":"20.00","order_sn":"1000000000045701","store_name":"品牌专营特卖店","store_id":"8"}
     * goods : {"store_id":"8","order_goods_id":"492","goods_id":"110926","goods_name":"test1","goods_type":"","goods_img_360":"http://img.guohanhealth.com/shop/store/goods/8/8_05874878177554855.png@!product-360","goods_price":"20.00","goods_spec":null,"goods_num":"1","goods_pay_price":"20.00"}
     * reason_list : [{"reason_id":"99","reason_info":"不能按时发货"},{"reason_id":"98","reason_info":"认为是假货"},{"reason_id":"97","reason_info":"保质期不符"},{"reason_id":"96","reason_info":"商品破损、有污渍"},{"reason_id":"95","reason_info":"效果不好不喜欢"}]
     */

    public OrderBean order;
    public GoodsBean goods;
    public List<ReasonListBean> reason_list;


    public static class OrderBean {
        /**
         * order_id : 473
         * order_type : 1
         * order_amount : 20.00
         * order_sn : 1000000000045701
         * store_name : 品牌专营特卖店
         * store_id : 8
         */

        public String order_id;
        public String order_type;
        public String order_amount;
        public String order_sn;
        public String store_name;
        public String store_id;

    }

    public static class GoodsBean {
        /**
         * store_id : 8
         * order_goods_id : 492
         * goods_id : 110926
         * goods_name : test1
         * goods_type :
         * goods_img_360 : http://img.guohanhealth.com/shop/store/goods/8/8_05874878177554855.png@!product-360
         * goods_price : 20.00
         * goods_spec : null
         * goods_num : 1
         * goods_pay_price : 20.00
         */

        public String store_id;
        public String order_goods_id;
        public String goods_id;
        public String goods_name;
        public String goods_type;
        public String goods_img_360;
        public String goods_price;
        public String goods_num;
        public String goods_pay_price;

    }

    public static class ReasonListBean {
        /**
         * reason_id : 99
         * reason_info : 不能按时发货
         */

        public String reason_id;
        public String reason_info;

        @Override
        public String toString() {
            return reason_info;
        }
    }
}
