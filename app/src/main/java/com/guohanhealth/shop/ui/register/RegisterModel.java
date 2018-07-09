package com.guohanhealth.shop.ui.register;


import com.guohanhealth.shop.base.BaseModel;
import com.guohanhealth.shop.bean.ImgCodeKey;
import com.guohanhealth.shop.bean.LoginBean;
import com.guohanhealth.shop.bean.SMSCode;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.RxHelper;

import io.reactivex.Observable;


public class RegisterModel extends BaseModel {

    /**
     * 手机注册
     */
    public Observable<LoginBean> registerMobile(String phone, String userName, String passWord, String captcha, String referral_code) {
        return Api.getDefault().registerMobile(phone, userName, passWord, captcha, "android", referral_code).compose(RxHelper.handleResult());
    }

    /**
     * 普通注册
     */
    public Observable<LoginBean> register(String userName, String passWord, String password_confirm, String email, String referral_code) {
        return Api.getDefault().register(userName, passWord, password_confirm, email, "android", referral_code).compose(RxHelper.handleResult());
    }


}
