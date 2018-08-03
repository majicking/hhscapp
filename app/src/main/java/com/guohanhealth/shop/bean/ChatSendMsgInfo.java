package com.guohanhealth.shop.bean;

import java.util.List;

public class ChatSendMsgInfo {
    public String f_id;
    public String f_name;
    public String t_id;
    public String t_name;
    public String t_msg;
    public String f_ip;
    public String add_time;
    public String m_id;
    public String goods_id;
    public List<ChatMessageInfo.Goods_info> goods_info;

    public static class Goods_info {

    }
}
