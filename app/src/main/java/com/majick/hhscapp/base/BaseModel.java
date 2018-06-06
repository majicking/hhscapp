package com.majick.hhscapp.base;

import com.majick.hhscapp.app.App;
import com.majick.hhscapp.bean.ImgCodeKey;
import com.majick.hhscapp.bean.LoginBean;
import com.majick.hhscapp.bean.SMSCode;
import com.majick.hhscapp.bean.UserInfo;
import com.majick.hhscapp.http.Api;
import com.majick.hhscapp.http.RxHelper;

import io.reactivex.Observable;

public class BaseModel {
    public String  key= App.getApp().getKey();

    /**
     * 获取用户信息
     */
    public Observable<UserInfo> getUserInfo(String key, String id) {
        return Api.getDefault().getUserInfo(key, id, "member_id").compose(RxHelper.handleResult());
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


    /**
     * 登录
     */
    public Observable<LoginBean> login(String userName, String passWord) {
        return Api.getDefault().login(userName, passWord, "android").compose(RxHelper.handleResult());
    }


}
