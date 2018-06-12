package com.majick.guohanhealth.bean;

import java.util.List;

public class Home3Info extends BaseInfo{
    public  String title;
    public List<Item> item;

    public class Item {
        public String image;
        public String type;
        public String data;
    }
}
