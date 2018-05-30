package com.majick.hhscapp.bean;

import com.majick.hhscapp.base.BaseModel;

public class UserInfo extends BaseModel {
    public Member_info member_info;

    public class Member_info {
        public String member_id;
        public String member_name;
        public String member_avatar;
        public String store_name;
        public String grade_id;
        public String store_id;
        public String seller_name;
    }
}
