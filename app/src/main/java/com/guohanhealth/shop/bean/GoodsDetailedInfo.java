package com.guohanhealth.shop.bean;

import java.util.List;

public class GoodsDetailedInfo extends BaseInfo {
    public Goods_hair_info goods_hair_info;

    public int IsHaveBuy;

    public List<Goods_commend_list> goods_commend_list;

    public static class Goods_commend_list {
        public String goods_id;
        public String goods_name;
        public String goods_price;
        public String goods_promotion_price;
        public String goods_image_url;
    }

    public Store_info store_info;

    public static class Store_info {
        public String store_id;
        public String store_name;
        public String member_id;
        public String member_name;
        public String is_own_shop;
        public String store_qq;
        public boolean node_chat;
        public int goods_count;
        public Store_credit store_credit;

        public static class Store_credit {
            public Store_desccredit store_desccredit;

            public static class Store_desccredit {
                public String text;
                public String credit;
                public String percent;
                public String percent_class;
                public String percent_text;
            }

            public Store_servicecredit store_servicecredit;

            public static class Store_servicecredit {
                public String text;
                public String credit;
                public String percent;
                public String percent_class;
                public String percent_text;
            }

            public Store_deliverycredit store_deliverycredit;

            public static class Store_deliverycredit {
                public String text;
                public String credit;
                public String percent;
                public String percent_class;
                public String percent_text;
            }
        }


    }

    public List<String> spec_image;

    public Goods_info goods_info;

    public static class Goods_info {
        public String goods_name;
        public String goods_jingle;
        public String gc_id_1;
        public String gc_id_2;
        public String gc_id_3;
        public String spec_name;
        public String spec_value;
        public String goods_attr;
        public String goods_custom;
        public String mobile_body;
        public String goods_price;
        public String goods_marketprice;
        public String goods_costprice;
        public String goods_discount;
        public String goods_serial;
        public String goods_storage_alarm;
        public String goods_barcode;
        public String transport_id;
        public String transport_title;
        public String goods_freight;
        public String goods_vat;
        public String areaid_1;
        public String areaid_2;
        public String areaid_3;
        public String goods_stcids;
        public String plateid_top;
        public String plateid_bottom;
        public String is_virtual;
        public String virtual_indate;
        public String virtual_limit;
        public String virtual_invalid_refund;
        public String sup_id;
        public String is_own_shop;
        public String goods_trans_v;
        public String is_dis;
        public String dis_add_time;
        public String dis_commis_rate;
        public String goods_video;
        public String sale_count;
        public String click_count;
        public String commis_rate;
        public String goods_grade;
        public String goods_id;
        public String goods_promotion_price;
        public String goods_promotion_type;
        public String goods_click;
        public String goods_salenum;
        public String goods_collect;
        //        public Object goods_spec;
        public String goods_storage;
        public String color_id;
        public String evaluation_good_star;
        public String evaluation_count;
        public String is_fcode;
        public String is_presell;
        public String presell_deliverdate;
        public String is_book;
        public String book_down_payment;
        public String book_final_payment;
        public String book_down_time;
        public String book_buyers;
        public String have_gift;
        public String contract_1;
        public String contract_2;
        public String contract_3;
        public String contract_4;
        public String contract_5;
        public String contract_6;
        public String contract_7;
        public String contract_8;
        public String contract_9;
        public String contract_10;
        public String is_chain;
        public List<Sole_info> sole_info;

        public static class Sole_info {

        }

        public String groupbuy_info;
        public String xianshi_info;
        public String jjg_info;
        public String cart;
        public String buynow;
        public String pintuan_promotion;

        public String goods_url;
    }


    public String video_path;
    public String mansong_info;
    public List<Gift_array> gift_array;

    public static class Gift_array {
        public String gift_id;
        public String goods_id;
        public String goods_commonid;
        public String gift_goodsid;
        public String gift_goodsname;
        public String gift_goodsimage;
        public String gift_amount;
    }

//    public Spec_list spec_list;
//
//    public static class Spec_list {
//        public Object o;
//    }

    public String goods_image;
    public boolean is_favorate;
    public List<Goods_eval_list> goods_eval_list;

    public static class Goods_eval_list {

    }

    public Goods_evaluate_info goods_evaluate_info;

    public static class Goods_evaluate_info {
        public String all;
        public String bad;
        public String bad_percent;
        public String good;
        public String good_percent;
        public String good_star;
        public String img;
        public String normal;
        public String normal_percent;
        public String star_average;

    }


}
