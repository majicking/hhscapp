package com.guohanhealth.shop.event;

public class GoodsDetailsEvent {
    public String data;

    public GoodsDetailsEvent(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
