package com.guohanhealth.shop.bean;

import java.util.List;

public class SearchWordsInfo extends BaseInfo{
    public List<HotInfoBean> hot_info;

    public static class HotInfoBean {
        public String name;
        public String value;

        @Override
        public String toString() {
            return "HotInfoBean{" +
                    "name='" + name + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SearchWordsInfo{" +
                "hot_info=" + hot_info.toString() +
                '}';
    }
}
