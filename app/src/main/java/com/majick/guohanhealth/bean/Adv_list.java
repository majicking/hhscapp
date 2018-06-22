package com.majick.guohanhealth.bean;

import java.util.List;

public class Adv_list extends BaseInfo{
    public List<Item> item;
    public List<Area_list>area_list;
    public class Item {
        public String image;
        public String type;
        public String data;
    }
}
