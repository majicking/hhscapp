package com.majick.guohanhealth.ui.login;

import com.majick.guohanhealth.app.App;
import com.majick.guohanhealth.base.BasePresenter;

public class LoginPersenter extends BasePresenter<LoginView, UserModel> {


    public void login(String name, String password) {
        mView.showLoadingDialog("登陆中...");
        mRxManager.add(mModel.login(name, password).flatMap(registerInfo -> {
                    App.getApp().setKey(registerInfo.key);
                    App.getApp().setUserid(registerInfo.userid);
                    App.getApp().setUsername(registerInfo.username);
                    return mModel.getUserInfo(registerInfo.key, registerInfo.userid);
                }).subscribe(userInfoResult -> {
                    mView.hideLoadingDialog();
                    App.getApp().setInfo(userInfoResult.member_info);
                    mView.sucess("登陆成功");
                }, throwable -> {
                    mView.hideLoadingDialog();
                    mView.faild(throwable.getMessage());
                })
        );
    }

}
