package com.majick.hhscapp.bean;

public class UserInfo extends BaseInfo {
    public Member_info member_info;

    public class Member_info {
        public String member_id;
        public String member_name;
        public String member_avatar;
        public String store_name;
        public String grade_id;
        public String store_id;
        public String seller_name;

        @Override
        public String toString() {
            return "Member_info{" +
                    "member_id='" + member_id + '\'' +
                    ", member_name='" + member_name + '\'' +
                    ", member_avatar='" + member_avatar + '\'' +
                    ", store_name='" + store_name + '\'' +
                    ", grade_id='" + grade_id + '\'' +
                    ", store_id='" + store_id + '\'' +
                    ", seller_name='" + seller_name + '\'' +
                    '}';
        }
    }
}
