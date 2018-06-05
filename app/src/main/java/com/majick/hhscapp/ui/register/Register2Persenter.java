package com.majick.hhscapp.ui.register;

import com.majick.hhscapp.app.AppConfig;
import com.majick.hhscapp.base.BasePresenter;
import com.majick.hhscapp.model.RegisterModel;

public class Register2Persenter extends BasePresenter<RegisterView, RegisterModel> {
    public void registerMobile(String phone, String userName, String passWord, String captcha, String referral_code) {
        mView.showLoadingDialog("注册中...");
        mRxManager.add(mModel.registerMobile(phone, userName, passWord, captcha, referral_code).subscribe(
                registerInfo -> {
                    AppConfig.setKey(registerInfo.key);
                    AppConfig.setUserid(registerInfo.userid);
                    AppConfig.setUsername(registerInfo.username);
                    mView.hideLoadingDialog();
                    mView.success("注册成功");
                }, throwable -> {
                    mView.hideLoadingDialog();
                    mView.fail(throwable.getMessage());
                }
        ));
    }


    public void getUserInfo() {
        mRxManager.add(mModel.getUserInfo(AppConfig.getKey(), AppConfig.getUserid()).subscribe(
                userInfo -> {
                    AppConfig.setInfo(userInfo.member_info);
                    mView.loginSuccess();
                }, throwable -> {
                    mView.loginFail(throwable.getMessage());
                }));
    }


}
