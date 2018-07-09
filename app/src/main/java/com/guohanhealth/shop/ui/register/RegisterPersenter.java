package com.guohanhealth.shop.ui.register;

import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.base.BasePresenter;
import com.guohanhealth.shop.http.ConsumerError;
import com.guohanhealth.shop.utils.Logutils;

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
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
                mView.hideLoadingDialog();
                mView.smsfail(message);
            }

        }));
    }

    public void register(String userName, String passWord, String password_confirm, String email, String referral_code) {
        mView.showLoadingDialog("注册中...");
        mRxManager.add(mModel.register(userName, passWord, password_confirm, email, referral_code).subscribe(registerInfo -> {
            Logutils.i(registerInfo);
            App.getApp().setKey(registerInfo.key);
            App.getApp().setUserid(registerInfo.userid);
            App.getApp().setUsername(registerInfo.username);
            mView.success("注册成功");
            mView.hideLoadingDialog();
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.hideLoadingDialog();
                mView.faild(message);
            }

        }));
    }


    public void getUserInfo(String key, String uid) {
        mRxManager.add(mModel.getUserInfo(key, uid).subscribe(
                userInfo -> {
                    App.getApp().setInfo(userInfo.member_info);
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
                    mView.faild(throwable.getMessage());
                    Logutils.i(throwable.getMessage());
                }));
    }
}
