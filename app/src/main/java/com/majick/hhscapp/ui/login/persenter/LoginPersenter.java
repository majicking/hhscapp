package com.majick.hhscapp.ui.login.persenter;

import com.majick.hhscapp.app.AppConfig;
import com.majick.hhscapp.base.BasePresenter;
import com.majick.hhscapp.bean.UserInfo;
import com.majick.hhscapp.model.UserModel;
import com.majick.hhscapp.ui.login.view.LoginView;
import com.majick.hhscapp.utils.Logutils;

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
