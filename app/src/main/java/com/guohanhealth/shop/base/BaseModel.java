package com.guohanhealth.shop.base;

import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.bean.CartNumberInfo;
import com.guohanhealth.shop.bean.ImgCodeKey;
import com.guohanhealth.shop.bean.LoginBean;
import com.guohanhealth.shop.bean.PayWayInfo;
import com.guohanhealth.shop.bean.SMSCode;
import com.guohanhealth.shop.bean.SearchWordsInfo;
import com.guohanhealth.shop.bean.UserInfo;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.RxHelper;

import io.reactivex.Observable;

public class BaseModel {
    public String key = App.getApp().getKey();

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

    /**
     * 获取热门搜索词自动填写
     */
    public Observable<SearchWordsInfo> getSearchDataWords() {
        return Api.getDefault().getSearchDataWords().compose(RxHelper.handleResult());
    }

    public Observable<PayWayInfo> getPayList(String key){
        return Api.getDefault().getPaymentList(key).compose(RxHelper.handleResult());
    }

    public Observable<CartNumberInfo> getCardNumber(String key) {
        return Api.getDefault().getCardNumber(key).compose(RxHelper.handleResult());
    }

}
