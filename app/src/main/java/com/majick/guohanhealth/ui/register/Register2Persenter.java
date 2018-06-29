package com.majick.guohanhealth.ui.register;

import com.majick.guohanhealth.app.App;
import com.majick.guohanhealth.base.BasePresenter;

public class Register2Persenter extends BasePresenter<RegisterView, RegisterModel> {
    public void registerMobile(String phone, String userName, String passWord, String captcha, String referral_code) {
        mView.showLoadingDialog("注册中...");
        mRxManager.add(mModel.registerMobile(phone, userName, passWord, captcha, referral_code).subscribe(
                registerInfo -> {
                    App.getApp().setKey(registerInfo.key);
                    App.getApp().setUserid(registerInfo.userid);
                    App.getApp().setUsername(registerInfo.username);
                    mView.success("注册成功");
                    mView.hideLoadingDialog();
                }, throwable -> {
                    mView.faild(throwable.getMessage());
                    mView.hideLoadingDialog();
                }
        ));
    }


    public void getUserInfo() {
        mRxManager.add(mModel.getUserInfo(App.getApp().getKey(), App.getApp().getUserid()).subscribe(
                userInfo -> {
                    App.getApp().setInfo(userInfo.member_info);
                    mView.loginSuccess();
                }, throwable -> {
                    mView.loginFail(throwable.getMessage());
                }));
    }


}
