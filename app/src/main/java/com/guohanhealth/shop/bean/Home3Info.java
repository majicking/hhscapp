package com.guohanhealth.shop.bean;

import java.util.List;

public class Home3Info extends BaseInfo{
    public  String title;
    public List<Item> item;

    public static class Item {
        public String image;
        public String type;
        public String data;
    }
}
