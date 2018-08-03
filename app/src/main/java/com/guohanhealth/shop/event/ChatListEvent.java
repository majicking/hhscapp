package com.guohanhealth.shop.event;

public class ChatListEvent {
    public String type;
    public Object obj;

    public ChatListEvent(String type, Object obj) {
        this.type = type;
        this.obj = obj;
    }
}
