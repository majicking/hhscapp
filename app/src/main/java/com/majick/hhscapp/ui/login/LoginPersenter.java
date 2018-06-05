package com.majick.hhscapp.ui.login;

import com.majick.hhscapp.app.AppConfig;
import com.majick.hhscapp.base.BaseModel;
import com.majick.hhscapp.base.BasePresenter;

public class LoginPersenter extends BasePresenter<LoginView, BaseModel> {


    public void login(String name, String password) {
        mView.showLoadingDialog("登陆中...");
        mRxManager.add(mModel.login(name, password).flatMap(registerInfo -> {
                    AppConfig.setKey(registerInfo.key);
                    AppConfig.setKey(registerInfo.userid);
                    AppConfig.setKey(registerInfo.username);
                    return mModel.getUserInfo(registerInfo.key, registerInfo.userid);
                }).subscribe(userInfoResult -> {
                    mView.hideLoadingDialog();
                    AppConfig.setInfo(userInfoResult.member_info);
                    mView.sucess("登陆成功");
                }, throwable -> {
                    mView.hideLoadingDialog();
                    mView.faild(throwable.getMessage());
                })
        );
    }

}
