package com.majick.guohanhealth.bean;

import java.util.List;

public class GoodsListInfo extends BaseInfo {

    public List<GoodsListBean> goods_list;

    public static class GoodsListBean {
        /**
         * goods_id : 101430
         * goods_marketprice : 198.00
         * contractlist : []
         * evaluation_good_star : 5
         * is_virtual : 0
         * is_fcode : 0
         * is_presell : 0
         * evaluation_count : 0
         * member_id : 624
         * store_domain : null
         * store_id : 19
         * goods_storage : 500
         * goods_image : 19_05810183604717431.jpg
         * store_name : 旺小满精品店
         * image : ["19_05810183604717431.jpg","19_05810183844567448.jpg","19_05810183882043745.jpg","19_05810183936599445.jpg"]
         * goods_name : 5千克精品布袋小米
         * goods_jingle : 国家地理标志产品
         * gc_name : 食品饮料 地方特产 华北
         * brand_name : null
         * brand_id : 0
         * is_book : 0
         * attr_id : null
         * area_id : 0
         * have_gift : 0
         * goods_click : 310
         * goods_salenum : 0
         * goods_barcode : null
         * sole_flag : false
         * group_flag : false
         * xianshi_flag : false
         * goods_price : 178
         * goods_image_url : http://img.guohanhealth.com/shop/store/goods/19/19_05810183604717431.jpg@!product-360
         */

        public String goods_id;
        public String goods_marketprice;
        public String evaluation_good_star;
        public String is_virtual;
        public String is_fcode;
        public String is_presell;
        public String evaluation_count;
        public String member_id;
        public String store_domain;
        public String store_id;
        public String goods_storage;
        public String goods_image;
        public String store_name;
        public String goods_name;
        public String goods_jingle;
        public String gc_name;
        public String brand_name;
        public String brand_id;
        public String is_book;
        public String is_appoint;
        public String is_own_shop;
        public String attr_id;
        public String area_id;
        public String have_gift;
        public String goods_click;
        public String goods_salenum;
        public String goods_barcode;
        public String goods_grade;
        public boolean sole_flag;
        public boolean group_flag;
        public boolean xianshi_flag;
        public String goods_price;
        public String goods_image_url;
        public List<?> contractlist;
        public List<String> image;
    }
}
