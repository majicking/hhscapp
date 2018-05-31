package com.majick.hhscapp.model;


import com.majick.hhscapp.base.BaseModel;
import com.majick.hhscapp.bean.ImgCodeKey;
import com.majick.hhscapp.bean.RegisterInfo;
import com.majick.hhscapp.bean.SMSCode;
import com.majick.hhscapp.http.Api;
import com.majick.hhscapp.http.RxHelper;

import io.reactivex.Observable;


public class RegisterModel extends BaseModel {
    /**
     * 普通注册
     */
    public Observable<RegisterInfo> register(String userName, String passWord, String referral_code, String password_confirm, String email) {
        return Api.getDefault().register(userName, passWord, password_confirm, email, "android", referral_code).compose(RxHelper.handleResult());
    }

    /**
     * 手机注册
     */
    public Observable<RegisterInfo> registerMobile(String userName, String passWord, String referral_code, String password_confirm, String email) {
        return Api.getDefault().registerMobile(userName, passWord, password_confirm, email, "android", referral_code).compose(RxHelper.handleResult());
    }

    /**
     * 获取手机验证码
     */
    public Observable<SMSCode> getSMSCode(String phone, String keycode, String imgcode) {
        return Api.getDefault().getSMSCode(phone, keycode, imgcode).compose(RxHelper.handleResult());
    }

    /**
     * 获取图片验证码
     */
    public Observable<ImgCodeKey> getImgCode() {
        return Api.getDefault().getImgCodeKey().compose(RxHelper.handleResult());
    }



}
