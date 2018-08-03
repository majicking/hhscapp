package com.guohanhealth.shop.bean;

import java.util.List;

public class ChatMessageInfo {
    public String f_id;
    public String f_name;
    public String t_id;
    public String t_name;
    public String t_msg;
    public String f_ip;
    public String add_time;
    public String m_id;
    public String goods_id;
    public List<Goods_info> goods_info;
    public User user;
    public boolean isSend=false;
    public int online;

    public static class User {
        public String u_id;
        public String u_name;
        public String s_id;
        public String update_time;
        public int connected;
        public String s_name;
        public String avatar;
        public String s_avatar;
        public int online;
        public String disconnect_time;
    }

    public static class Goods_info {

    }
}
