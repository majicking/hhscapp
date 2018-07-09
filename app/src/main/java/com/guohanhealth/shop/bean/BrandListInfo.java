package com.guohanhealth.shop.bean;

import java.util.List;

public class BrandListInfo extends BaseInfo{
    public List<Brand_list> brand_list;

    public class Brand_list {
        public String brand_id;
        public String brand_name;
        public String brand_pic;
    }
}
