package com.majick.guohanhealth.bean;

import java.util.List;

public class GoodsClassChildInfo {
    public List<Class_list> class_list;

    public class Class_list {
        public String gc_id;
        public String gc_name;
        public Child child;

        public class Child {
            public String gc_id;
            public String gc_name;
        }
    }
}
