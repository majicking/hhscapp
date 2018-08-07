package com.guohanhealth.shop.bean;

import java.util.List;

public class Goods1Bean {
    public String title;
    public List<Item> item;
    public static class Item {
        public String xianshi_id;
        public String lower_limit;
        public String state;
        public String store_id;
        public String goods_name;
        public String xianshi_price;
        public String goods_image;
        public String xianshi_name;
        public String xianshi_title;
        public String gc_id_1;
        public long time;
        public String goods_price;
        public String end_time;
        public String goods_id;
        public String start_time;
        public String xianshi_goods_id;
        public String xianshi_explain;
        public String xianshi_recommend;
        public String activtime = "0天0时0分0秒";

        public String getActivtime() {
            return activtime;
        }

        public void setActivtime(String activtime) {
            this.activtime = activtime;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }
}
