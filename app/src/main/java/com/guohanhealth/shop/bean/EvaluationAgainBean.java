package com.guohanhealth.shop.bean;

import java.util.List;

public class EvaluationAgainBean {


    /**
     * store_info : {"store_id":"8","store_name":"品牌专营特卖店","is_own_shop":"0"}
     * evaluate_goods : [{"geval_id":"25","geval_orderid":"477","geval_orderno":"1000000000046101","geval_ordergoodsid":"497","geval_goodsid":"110926","geval_goodsname":"test1","geval_goodsprice":"20.00","geval_goodsimage":"http://img.guohanhealth.com/shop/store/goods/8/8_05874878177554855.png@!product-240","geval_scores":"5","geval_content":"聚聚不啊透露噜噜噜不不不不粗啊不不","geval_isanonymous":"0","geval_addtime":"1535454873","geval_storeid":"8","geval_storename":"品牌专营特卖店","geval_frommemberid":"617","geval_frommembername":"majick","geval_explain":null,"geval_image":"617_05887988516688196.jpg","geval_content_again":"","geval_addtime_again":"0","geval_image_again":"","geval_explain_again":""}]
     */

    public StoreInfoBean store_info;
    public List<EvaluateGoodsBean> evaluate_goods;


    public static class StoreInfoBean {
        /**
         * store_id : 8
         * store_name : 品牌专营特卖店
         * is_own_shop : 0
         */

        public String store_id;
        public String store_name;
        public String is_own_shop;


    }

    public static class EvaluateGoodsBean {
        /**
         * geval_id : 25
         * geval_orderid : 477
         * geval_orderno : 1000000000046101
         * geval_ordergoodsid : 497
         * geval_goodsid : 110926
         * geval_goodsname : test1
         * geval_goodsprice : 20.00
         * geval_goodsimage : http://img.guohanhealth.com/shop/store/goods/8/8_05874878177554855.png@!product-240
         * geval_scores : 5
         * geval_content : 聚聚不啊透露噜噜噜不不不不粗啊不不
         * geval_isanonymous : 0
         * geval_addtime : 1535454873
         * geval_storeid : 8
         * geval_storename : 品牌专营特卖店
         * geval_frommemberid : 617
         * geval_frommembername : majick
         * geval_explain : null
         * geval_image : 617_05887988516688196.jpg
         * geval_content_again :
         * geval_addtime_again : 0
         * geval_image_again :
         * geval_explain_again :
         */

        public String geval_id;
        public String geval_orderid;
        public String geval_orderno;
        public String geval_ordergoodsid;
        public String geval_goodsid;
        public String geval_goodsname;
        public String geval_goodsprice;
        public String geval_goodsimage;
        public String geval_scores;
        public String geval_content;
        public String geval_isanonymous;
        public String geval_addtime;
        public String geval_storeid;
        public String geval_storename;
        public String geval_frommemberid;
        public String geval_frommembername;
        public Object geval_explain;
        public String geval_image;
        public String geval_content_again;
        public String geval_addtime_again;
        public String geval_image_again;
        public String geval_explain_again;

    }
}
