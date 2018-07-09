package com.guohanhealth.shop.bean;

import java.util.List;

public class SelectedInfo  extends BaseInfo{
    public List<Area_list> area_list;
    public List<Contract_list> contract_list;

    public static class Contract_list {
        public String id;
        public String name;
    }
}
