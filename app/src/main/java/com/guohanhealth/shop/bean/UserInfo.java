package com.guohanhealth.shop.bean;

public class UserInfo extends BaseInfo {
    public Member_info member_info;

    public static class Member_info {
        public String member_id;
        public String member_name;
        public String member_avatar;
        public String store_name;
        public String grade_id;
        public String store_id;
        public String seller_name;

        public Member_info() {
        }

        public Member_info(String member_id, String member_name, String member_avatar, String store_name, String grade_id, String store_id, String seller_name) {
            this.member_id = member_id;
            this.member_name = member_name;
            this.member_avatar = member_avatar;
            this.store_name = store_name;
            this.grade_id = grade_id;
            this.store_id = store_id;
            this.seller_name = seller_name;
        }

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
