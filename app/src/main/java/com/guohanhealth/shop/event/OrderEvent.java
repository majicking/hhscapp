package com.guohanhealth.shop.event;

public class OrderEvent {
    public int number;//当前栏
    public boolean select;//虚实
    public String search;//搜索字段

    public OrderEvent(int number, boolean select, String search) {
        this.number = number;
        this.select = select;
        this.search = search;
    }
}
