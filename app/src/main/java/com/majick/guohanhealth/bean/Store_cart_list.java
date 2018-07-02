package com.majick.guohanhealth.bean;

import java.util.List;

public class Store_cart_list {

    /**
     * goods_list : [{"goods_num":1,"goods_id":100605,"goods_commonid":"100325","gc_id":"276","store_id":"20","goods_name":"Sony/索尼 ILCE-7K标准单镜套（28-70mm）全画幅微单数码相机","goods_price":"4000.00","store_name":"tao的分销测试店","goods_image":"1_04758452672719519.jpg","transport_id":"0","goods_freight":"10.00","goods_trans_v":"0.00","goods_inv":"1","goods_vat":"0","goods_voucher":"1","goods_storage":"987","goods_storage_alarm":"0","is_fcode":"0","have_gift":"0","state":true,"storage_state":true,"groupbuy_info":null,"xianshi_info":null,"promotion_info":[],"is_member_price":0,"is_chain":"0","is_dis":"0","is_book":"0","book_down_payment":"0.00","book_final_payment":"0.00","book_down_time":"0","cart_id":100605,"bl_id":0,"sole_info":[],"contractlist":[],"goods_total":"4000.00","goods_image_url":"http://test.shopnctest.com/data/upload/shop/store/goods/1/1_04758452672719519_240.jpg"}]
     * store_goods_total : 4000.00
     * store_mansong_rule_list : null
     * store_voucher_info : []
     * store_voucher_list : []
     * freight : 1
     * store_name : tao的分销测试店
     */

    private String store_goods_total;
//    private Object store_mansong_rule_list;
//    private String freight;
    private String store_name;
    private List<GoodsListBean> goods_list;
//    private List<?> store_voucher_info;
//    private List<?> store_voucher_list;

    public String getStore_goods_total() {
        return store_goods_total;
    }

    public void setStore_goods_total(String store_goods_total) {
        this.store_goods_total = store_goods_total;
    }

//    public Object getStore_mansong_rule_list() {
//        return store_mansong_rule_list;
//    }
//
//    public void setStore_mansong_rule_list(Object store_mansong_rule_list) {
//        this.store_mansong_rule_list = store_mansong_rule_list;
//    }
//
//    public String getFreight() {
//        return freight;
//    }
//
//    public void setFreight(String freight) {
//        this.freight = freight;
//    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public List<GoodsListBean> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<GoodsListBean> goods_list) {
        this.goods_list = goods_list;
    }

//    public List<?> getStore_voucher_info() {
//        return store_voucher_info;
//    }
//
//    public void setStore_voucher_info(List<?> store_voucher_info) {
//        this.store_voucher_info = store_voucher_info;
//    }
//
//    public List<?> getStore_voucher_list() {
//        return store_voucher_list;
//    }
//
//    public void setStore_voucher_list(List<?> store_voucher_list) {
//        this.store_voucher_list = store_voucher_list;
//    }

    public static class GoodsListBean {
        /**
         * goods_num : 1
         * goods_id : 100605
         * goods_commonid : 100325
         * gc_id : 276
         * store_id : 20
         * goods_name : Sony/索尼 ILCE-7K标准单镜套（28-70mm）全画幅微单数码相机
         * goods_price : 4000.00
         * store_name : tao的分销测试店
         * goods_image : 1_04758452672719519.jpg
         * transport_id : 0
         * goods_freight : 10.00
         * goods_trans_v : 0.00
         * goods_inv : 1
         * goods_vat : 0
         * goods_voucher : 1
         * goods_storage : 987
         * goods_storage_alarm : 0
         * is_fcode : 0
         * have_gift : 0
         * state : true
         * storage_state : true
         * groupbuy_info : null
         * xianshi_info : null
         * promotion_info : []
         * is_member_price : 0
         * is_chain : 0
         * is_dis : 0
         * is_book : 0
         * book_down_payment : 0.00
         * book_final_payment : 0.00
         * book_down_time : 0
         * cart_id : 100605
         * bl_id : 0
         * sole_info : []
         * contractlist : []
         * goods_total : 4000.00
         * goods_image_url : http://test.shopnctest.com/data/upload/shop/store/goods/1/1_04758452672719519_240.jpg
         */

        private int goods_num;
        private int goods_id;
        private String goods_commonid;
        private String gc_id;
        private String store_id;
        private String goods_name;
        private String goods_price;
        private String store_name;
        private String goods_image;
        private String transport_id;
        private String goods_freight;
        private String goods_trans_v;
        private String goods_inv;
        private String goods_vat;
        private String goods_voucher;
        private String goods_storage;
        private String goods_storage_alarm;
        private String is_fcode;
        private String have_gift;
        private boolean state;
        private boolean storage_state;
        private Object groupbuy_info;
        private Object xianshi_info;
        private int is_member_price;
        private String is_chain;
        private String is_dis;
        private String is_book;
        private String book_down_payment;
        private String book_final_payment;
        private String book_down_time;
        private int cart_id;
        private int bl_id;
        private String goods_total;
        private String goods_image_url;
        private List<?> promotion_info;
        private List<?> sole_info;
        private List<?> contractlist;

        public int getGoods_num() {
            return goods_num;
        }

        public void setGoods_num(int goods_num) {
            this.goods_num = goods_num;
        }

        public int getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(int goods_id) {
            this.goods_id = goods_id;
        }

        public String getGoods_commonid() {
            return goods_commonid;
        }

        public void setGoods_commonid(String goods_commonid) {
            this.goods_commonid = goods_commonid;
        }

        public String getGc_id() {
            return gc_id;
        }

        public void setGc_id(String gc_id) {
            this.gc_id = gc_id;
        }

        public String getStore_id() {
            return store_id;
        }

        public void setStore_id(String store_id) {
            this.store_id = store_id;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getGoods_price() {
            return goods_price;
        }

        public void setGoods_price(String goods_price) {
            this.goods_price = goods_price;
        }

        public String getStore_name() {
            return store_name;
        }

        public void setStore_name(String store_name) {
            this.store_name = store_name;
        }

        public String getGoods_image() {
            return goods_image;
        }

        public void setGoods_image(String goods_image) {
            this.goods_image = goods_image;
        }

        public String getTransport_id() {
            return transport_id;
        }

        public void setTransport_id(String transport_id) {
            this.transport_id = transport_id;
        }

        public String getGoods_freight() {
            return goods_freight;
        }

        public void setGoods_freight(String goods_freight) {
            this.goods_freight = goods_freight;
        }

        public String getGoods_trans_v() {
            return goods_trans_v;
        }

        public void setGoods_trans_v(String goods_trans_v) {
            this.goods_trans_v = goods_trans_v;
        }

        public String getGoods_inv() {
            return goods_inv;
        }

        public void setGoods_inv(String goods_inv) {
            this.goods_inv = goods_inv;
        }

        public String getGoods_vat() {
            return goods_vat;
        }

        public void setGoods_vat(String goods_vat) {
            this.goods_vat = goods_vat;
        }

        public String getGoods_voucher() {
            return goods_voucher;
        }

        public void setGoods_voucher(String goods_voucher) {
            this.goods_voucher = goods_voucher;
        }

        public String getGoods_storage() {
            return goods_storage;
        }

        public void setGoods_storage(String goods_storage) {
            this.goods_storage = goods_storage;
        }

        public String getGoods_storage_alarm() {
            return goods_storage_alarm;
        }

        public void setGoods_storage_alarm(String goods_storage_alarm) {
            this.goods_storage_alarm = goods_storage_alarm;
        }

        public String getIs_fcode() {
            return is_fcode;
        }

        public void setIs_fcode(String is_fcode) {
            this.is_fcode = is_fcode;
        }

        public String getHave_gift() {
            return have_gift;
        }

        public void setHave_gift(String have_gift) {
            this.have_gift = have_gift;
        }

        public boolean isState() {
            return state;
        }

        public void setState(boolean state) {
            this.state = state;
        }

        public boolean isStorage_state() {
            return storage_state;
        }

        public void setStorage_state(boolean storage_state) {
            this.storage_state = storage_state;
        }

        public Object getGroupbuy_info() {
            return groupbuy_info;
        }

        public void setGroupbuy_info(Object groupbuy_info) {
            this.groupbuy_info = groupbuy_info;
        }

        public Object getXianshi_info() {
            return xianshi_info;
        }

        public void setXianshi_info(Object xianshi_info) {
            this.xianshi_info = xianshi_info;
        }

        public int getIs_member_price() {
            return is_member_price;
        }

        public void setIs_member_price(int is_member_price) {
            this.is_member_price = is_member_price;
        }

        public String getIs_chain() {
            return is_chain;
        }

        public void setIs_chain(String is_chain) {
            this.is_chain = is_chain;
        }

        public String getIs_dis() {
            return is_dis;
        }

        public void setIs_dis(String is_dis) {
            this.is_dis = is_dis;
        }

        public String getIs_book() {
            return is_book;
        }

        public void setIs_book(String is_book) {
            this.is_book = is_book;
        }

        public String getBook_down_payment() {
            return book_down_payment;
        }

        public void setBook_down_payment(String book_down_payment) {
            this.book_down_payment = book_down_payment;
        }

        public String getBook_final_payment() {
            return book_final_payment;
        }

        public void setBook_final_payment(String book_final_payment) {
            this.book_final_payment = book_final_payment;
        }

        public String getBook_down_time() {
            return book_down_time;
        }

        public void setBook_down_time(String book_down_time) {
            this.book_down_time = book_down_time;
        }

        public int getCart_id() {
            return cart_id;
        }

        public void setCart_id(int cart_id) {
            this.cart_id = cart_id;
        }

        public int getBl_id() {
            return bl_id;
        }

        public void setBl_id(int bl_id) {
            this.bl_id = bl_id;
        }

        public String getGoods_total() {
            return goods_total;
        }

        public void setGoods_total(String goods_total) {
            this.goods_total = goods_total;
        }

        public String getGoods_image_url() {
            return goods_image_url;
        }

        public void setGoods_image_url(String goods_image_url) {
            this.goods_image_url = goods_image_url;
        }

        public List<?> getPromotion_info() {
            return promotion_info;
        }

        public void setPromotion_info(List<?> promotion_info) {
            this.promotion_info = promotion_info;
        }

        public List<?> getSole_info() {
            return sole_info;
        }

        public void setSole_info(List<?> sole_info) {
            this.sole_info = sole_info;
        }

        public List<?> getContractlist() {
            return contractlist;
        }

        public void setContractlist(List<?> contractlist) {
            this.contractlist = contractlist;
        }
    }
}
