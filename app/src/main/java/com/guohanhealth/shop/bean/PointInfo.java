package com.guohanhealth.shop.bean;

import java.util.List;

public class PointInfo extends BaseInfo {
    public List<DataBean> log_list;

    public static class DataBean {
        public String pl_id;
        public String pl_memberid;
        public String pl_membername;
        public String pl_adminid;
        public String pl_adminname;
        public String pl_points;
        public String pl_addtimel;
        public String pl_desc;
        public String pl_stage;
        public String stagetext;
        public String addtimetext;
    }
}
