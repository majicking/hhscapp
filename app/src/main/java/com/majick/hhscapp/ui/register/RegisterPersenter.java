package com.majick.hhscapp.ui.register;

import com.majick.hhscapp.app.AppConfig;
import com.majick.hhscapp.base.BasePresenter;
import com.majick.hhscapp.model.RegisterModel;
import com.majick.hhscapp.utils.Logutils;

public class RegisterPersenter extends BasePresenter<RegisterView, RegisterModel> {
    public void getImgCode() {
        mRxManager.add(mModel.getImgCode().subscribe(imgCodeKey -> {
            Logutils.i(imgCodeKey.codekey);
            mView.setKeyCode(imgCodeKey.codekey);
        }));
    }

    public void getSMSCode(String mobile, String code, String imgcode) {
        mView.showLoadingDialog("获取短信...");
        mRxManager.add(mModel.getSMSCode(mobile, code, imgcode).subscribe(smsCode -> {
            mView.hideLoadingDialog();
            mView.smsSuccess(smsCode.sms_time);
        }, throwable -> {
            mView.hideLoadingDialog();
            mView.smsfail(throwable.getMessage());
        }));
    }

    public void register(String userName, String passWord, String password_confirm, String email, String referral_code) {
        mView.showLoadingDialog("注册中...");
        mRxManager.add(mModel.register(userName, passWord, password_confirm, email, referral_code).subscribe(registerInfo -> {
            Logutils.i(registerInfo);
            mView.hideLoadingDialog();
            mView.success("注册成功");
            AppConfig.setKey(registerInfo.key);
            AppConfig.setUserid(registerInfo.userid);
            AppConfig.setUsername(registerInfo.username);
        }, throwable -> {
            mView.hideLoadingDialog();
            mView.fail(throwable.getMessage());
        }));
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

    public void registerMobile(String userName, String passWord, String referral_code, String password_confirm, String email) {
        mView.showLoadingDialog("注册中...");
        mRxManager.add(mModel.registerMobile(userName, passWord, password_confirm, email, referral_code).subscribe(
                loginBean -> {
                    mView.hideLoadingDialog();
                    Logutils.i(loginBean.userid);
                    mView.success("注册成功");
                }, throwable -> {
                    mView.hideLoadingDialog();
                    mView.fail(throwable.getMessage());
                    Logutils.i(throwable.getMessage());
                }));
    }
}
