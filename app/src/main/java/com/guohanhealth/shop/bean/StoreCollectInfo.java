package com.guohanhealth.shop.bean;

import java.util.List;

public class StoreCollectInfo {
   public List<DataBean> favorites_list;

    public static class DataBean {
        public String fav_time;
        public String fav_time_text;
        public String goods_count;
        public String store_avatar;
        public String store_avatar_url;
        public String store_collect;
        public String store_id;
        public String store_name;
    }
}
