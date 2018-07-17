package com.guohanhealth.shop.ui.login;

import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.base.BasePresenter;
import com.guohanhealth.shop.http.ConsumerError;

public class LoginPersenter extends BasePresenter<LoginView, UserModel> {


    public void login(String name, String password) {
        mView.showLoadingDialog("登陆中...");
        mRxManager.add(mModel.login(name, password).flatMap(registerInfo -> {
                    App.getApp().setKey(registerInfo.key);
                    App.getApp().setUserid(registerInfo.userid);
                    App.getApp().setUsername(registerInfo.username);
                    return mModel.getUserInfo(registerInfo.key, registerInfo.userid);
                }).subscribe(userInfoResult -> {
                    App.getApp().setInfo(userInfoResult.member_info);
                    mView.hideLoadingDialog();
                    mView.sucess("登陆成功");
                }, new ConsumerError<Throwable>() {
                    @Override
                    public void onError(int errorCode, String message) {
                        mView.faild(message);
                        mView.hideLoadingDialog();
                    }
                })
        );
    }

}
