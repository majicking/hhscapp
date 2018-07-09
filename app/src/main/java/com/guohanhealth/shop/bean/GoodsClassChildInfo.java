package com.guohanhealth.shop.bean;

import java.util.List;

public class GoodsClassChildInfo extends BaseInfo{
    public List<Class_list> class_list;

    public class Class_list {
        public String gc_id;
        public String gc_name;
        public List<Child> child;

        public class Child {
            public String gc_id;
            public String gc_name;
        }
    }
}
