package com.guohanhealth.shop.bean;

import java.util.List;

public class PredepositLogInfo extends BaseInfo {
    public List<DataBean> list;

    public static class DataBean {
        public String lg_add_time;
        public String lg_add_time_text;
        public String lg_admin_name;
        public String lg_av_amount;
        public String lg_desc;
        public String lg_freeze_amount;
        public String lg_id;
        public String lg_invite_member_id;
        public String lg_member_id;
        public String lg_member_name;
        public String lg_type;
    }
}
