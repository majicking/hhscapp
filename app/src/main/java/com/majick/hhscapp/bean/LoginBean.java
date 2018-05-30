package com.majick.hhscapp.bean;

import com.majick.hhscapp.base.BaseModel;

/**
 * 登录Bean
 */
public class LoginBean extends BaseModel{
    public String key;
    public String username;
    public String userid;
    public Sell sell;

    public class Sell {
        public String seller_name;
        public String store_name;
        public String key;
    }
}
