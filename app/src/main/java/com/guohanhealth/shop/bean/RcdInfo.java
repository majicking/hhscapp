package com.guohanhealth.shop.bean;

import java.util.List;

public class RcdInfo extends BaseInfo {

  public  List<DataBean> log_list;

    public static class DataBean {
        public String add_time;
        public String add_time_text;
        public String available_amount;
        public String description;
        public String freeze_amount;
        public String id;
        public String member_id;
        public String member_name;
        public String type;
    }
}
