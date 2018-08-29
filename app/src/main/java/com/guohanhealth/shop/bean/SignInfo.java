package com.guohanhealth.shop.bean;

import java.util.List;

public class SignInfo extends BaseInfo {
    public List<DataBean> signin_list;

    public static class DataBean {
        public String sl_id;
        public String sl_memberid;
        public String sl_membername;
        public String sl_addtime;
        public String sl_points;
        public String sl_addtime_text;
    }
}
