package com.majick.hhscapp.bean;

/**
 * 登录Bean
 */
public class LoginBean extends BaseInfo {
    public String key;
    public String username;
    public String userid;
//    public Sell sell;

    @Override
    public String toString() {
        return "LoginBean{" +
                "key='" + key + '\'' +
                ", username='" + username + '\'' +
                ", userid='" + userid + '\'' +
//                ", sell=" + sell +
                '}';
    }

    public class Sell {
        public String seller_name;
        public String store_name;
        public String key;

        @Override
        public String toString() {
            return "Sell{" +
                    "seller_name='" + seller_name + '\'' +
                    ", store_name='" + store_name + '\'' +
                    ", key='" + key + '\'' +
                    '}';
        }
    }
}
