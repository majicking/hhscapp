package com.majick.guohanhealth.ui.register;


import com.majick.guohanhealth.base.BaseModel;
import com.majick.guohanhealth.bean.ImgCodeKey;
import com.majick.guohanhealth.bean.LoginBean;
import com.majick.guohanhealth.bean.SMSCode;
import com.majick.guohanhealth.http.Api;
import com.majick.guohanhealth.http.RxHelper;

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
