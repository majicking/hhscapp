package com.majick.hhscapp.model;

import com.majick.hhscapp.base.BaseModel;
import com.majick.hhscapp.bean.LoginBean;
import com.majick.hhscapp.bean.UserInfo;
import com.majick.hhscapp.http.Api;
import com.majick.hhscapp.http.Result;
import com.majick.hhscapp.http.RxHelper;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserModel extends BaseModel {
    /**
     * 登录
     */
    public Observable<LoginBean> login(String userName, String passWord) {
        return Api.getDefault().login(userName, passWord, "android").compose(RxHelper.handleResult());
    }

    /**
     * 获取用户信息
     */
    public Observable<UserInfo> getUserInfo(String key, String id) {
        return Api.getDefault().getUserInfo(key, id, "member_id").compose(RxHelper.handleResult());
    }
}
