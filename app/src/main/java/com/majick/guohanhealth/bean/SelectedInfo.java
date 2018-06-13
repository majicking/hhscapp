package com.majick.guohanhealth.bean;

import java.util.List;

public class SelectedInfo {
  public List<Area_list> area_list;
  public List<Contract_list> contract_list;

    public static class Area_list {
        public String area_id;
        public String area_name;
    }

    public static class Contract_list {
        public String id;
        public String name;
    }
}
