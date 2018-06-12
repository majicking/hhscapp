package com.majick.guohanhealth.bean;

import java.util.List;

public class GoodsListInfo extends BaseInfo {
    public Goods_list goods_list;

    class Goods_list {


        /**
         * goods_id : 102181
         * goods_marketprice : 6000.00
         * contractlist : []
         * evaluation_good_star : 5
         * is_virtual : 0
         * is_fcode : 0
         * is_presell : 0
         * evaluation_count : 0
         * member_id : 478
         * store_domain : null
         * store_id : 12
         * goods_storage : 13
         * goods_image : 12_05821328200500820.jpg
         * store_name : 控季几几, 掌控敌银
         * image : ["12_05821328200500820.jpg","12_05821328463508542.jpg","12_05821328521731941.jpg"]
         * goods_name : thinkpad 银色 14.1
         * goods_jingle : null
         * gc_name : 数码办公 电脑整机 笔记本
         * brand_name : null
         * brand_id : 0
         * is_book : 0
         * attr_id : null
         * area_id : 0
         * have_gift : 0
         * goods_click : 0
         * goods_salenum : 0
         * goods_barcode : null
         * sole_flag : false
         * group_flag : false
         * xianshi_flag : false
         * goods_grade : http://img.guohanhealth.com/shop/goods_grade/05820478283529928.png
         * goods_price : 5000
         * goods_image_url : http://img.guohanhealth.com/shop/store/goods/12/12_05821328200500820.jpg@!product-360
         */

        private String goods_id;
        private String goods_marketprice;
        private String evaluation_good_star;
        private String is_virtual;
        private String is_fcode;
        private String is_presell;
        private String evaluation_count;
        private String member_id;
        private String store_domain;
        private String store_id;
        private String goods_storage;
        private String goods_image;
        private String store_name;
        private String goods_name;
        private String goods_jingle;
        private String gc_name;
        private String brand_name;
        private String brand_id;
        private String is_book;
        private String attr_id;
        private String area_id;
        private String have_gift;
        private String goods_click;
        private String goods_salenum;
        private String goods_barcode;
        private boolean sole_flag;
        private boolean group_flag;
        private boolean xianshi_flag;
        private String goods_grade;
        private String goods_price;
        private String goods_image_url;
        private List<Contractlist> contractlist;
        private List<String> image;


        public class Contractlist {

        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getGoods_marketprice() {
            return goods_marketprice;
        }

        public void setGoods_marketprice(String goods_marketprice) {
            this.goods_marketprice = goods_marketprice;
        }

        public String getEvaluation_good_star() {
            return evaluation_good_star;
        }

        public void setEvaluation_good_star(String evaluation_good_star) {
            this.evaluation_good_star = evaluation_good_star;
        }

        public String getIs_virtual() {
            return is_virtual;
        }

        public void setIs_virtual(String is_virtual) {
            this.is_virtual = is_virtual;
        }

        public String getIs_fcode() {
            return is_fcode;
        }

        public void setIs_fcode(String is_fcode) {
            this.is_fcode = is_fcode;
        }

        public String getIs_presell() {
            return is_presell;
        }

        public void setIs_presell(String is_presell) {
            this.is_presell = is_presell;
        }

        public String getEvaluation_count() {
            return evaluation_count;
        }

        public void setEvaluation_count(String evaluation_count) {
            this.evaluation_count = evaluation_count;
        }

        public String getMember_id() {
            return member_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public String getStore_domain() {
            return store_domain;
        }

        public void setStore_domain(String store_domain) {
            this.store_domain = store_domain;
        }

        public String getStore_id() {
            return store_id;
        }

        public void setStore_id(String store_id) {
            this.store_id = store_id;
        }

        public String getGoods_storage() {
            return goods_storage;
        }

        public void setGoods_storage(String goods_storage) {
            this.goods_storage = goods_storage;
        }

        public String getGoods_image() {
            return goods_image;
        }

        public void setGoods_image(String goods_image) {
            this.goods_image = goods_image;
        }

        public String getStore_name() {
            return store_name;
        }

        public void setStore_name(String store_name) {
            this.store_name = store_name;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getGoods_jingle() {
            return goods_jingle;
        }

        public void setGoods_jingle(String goods_jingle) {
            this.goods_jingle = goods_jingle;
        }

        public String getGc_name() {
            return gc_name;
        }

        public void setGc_name(String gc_name) {
            this.gc_name = gc_name;
        }

        public String getBrand_name() {
            return brand_name;
        }

        public void setBrand_name(String brand_name) {
            this.brand_name = brand_name;
        }

        public String getBrand_id() {
            return brand_id;
        }

        public void setBrand_id(String brand_id) {
            this.brand_id = brand_id;
        }

        public String getIs_book() {
            return is_book;
        }

        public void setIs_book(String is_book) {
            this.is_book = is_book;
        }

        public String getAttr_id() {
            return attr_id;
        }

        public void setAttr_id(String attr_id) {
            this.attr_id = attr_id;
        }

        public String getArea_id() {
            return area_id;
        }

        public void setArea_id(String area_id) {
            this.area_id = area_id;
        }

        public String getHave_gift() {
            return have_gift;
        }

        public void setHave_gift(String have_gift) {
            this.have_gift = have_gift;
        }

        public String getGoods_click() {
            return goods_click;
        }

        public void setGoods_click(String goods_click) {
            this.goods_click = goods_click;
        }

        public String getGoods_salenum() {
            return goods_salenum;
        }

        public void setGoods_salenum(String goods_salenum) {
            this.goods_salenum = goods_salenum;
        }

        public String getGoods_barcode() {
            return goods_barcode;
        }

        public void setGoods_barcode(String goods_barcode) {
            this.goods_barcode = goods_barcode;
        }

        public boolean isSole_flag() {
            return sole_flag;
        }

        public void setSole_flag(boolean sole_flag) {
            this.sole_flag = sole_flag;
        }

        public boolean isGroup_flag() {
            return group_flag;
        }

        public void setGroup_flag(boolean group_flag) {
            this.group_flag = group_flag;
        }

        public boolean isXianshi_flag() {
            return xianshi_flag;
        }

        public void setXianshi_flag(boolean xianshi_flag) {
            this.xianshi_flag = xianshi_flag;
        }

        public String getGoods_grade() {
            return goods_grade;
        }

        public void setGoods_grade(String goods_grade) {
            this.goods_grade = goods_grade;
        }

        public String getGoods_price() {
            return goods_price;
        }

        public void setGoods_price(String goods_price) {
            this.goods_price = goods_price;
        }

        public String getGoods_image_url() {
            return goods_image_url;
        }

        public void setGoods_image_url(String goods_image_url) {
            this.goods_image_url = goods_image_url;
        }

        public List<Contractlist> getContractlist() {
            return contractlist;
        }

        public void setContractlist(List<Contractlist> contractlist) {
            this.contractlist = contractlist;
        }

        public List<String> getImage() {
            return image;
        }

        public void setImage(List<String> image) {
            this.image = image;
        }
    }
}
