package com.majick.hhscapp.ui.login;

import com.majick.hhscapp.app.AppConfig;
import com.majick.hhscapp.base.BasePresenter;
import com.majick.hhscapp.model.UserModel;
import com.majick.hhscapp.ui.login.LoginView;

public class LoginPersenter extends BasePresenter<LoginView, UserModel> {


    public void login(String name, String password) {

        mView.start();
        mRxManager.add(mModel.login(name, password).flatMap(loginBeanResult -> {
                    AppConfig.setLoginBean(loginBeanResult);
                    return mModel.getUserInfo(loginBeanResult.key, loginBeanResult.userid);
                }).subscribe(userInfoResult -> {
                    AppConfig.setUserInfo(userInfoResult);
                    mView.sucess("登陆成功");
                }, throwable -> {
                    mView.faild(throwable.getMessage());
                })
        );
    }

}
