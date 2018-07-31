package com.guohanhealth.shop.bean;

import java.util.List;

public class HealthInfo extends BaseInfo {
    public List<DataBean> data;

    public static class DataBean {
        public String created_time;
        public String id;
        public String member_id;
        public String order_sn;
        public String value;
        public String way;
    }

}
