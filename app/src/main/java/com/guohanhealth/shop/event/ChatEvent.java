package com.guohanhealth.shop.event;

public class ChatEvent {
    public String type;
    public Object obj;

    public ChatEvent(String type, Object obj) {
        this.type = type;
        this.obj = obj;
    }
}
