package com.guohanhealth.shop.bean;

public class SpecBean  extends BaseInfo{
    public String key;
    public String value;

    public SpecBean(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public SpecBean() {
    }

    @Override
    public String toString() {
        return "CommontBean{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
