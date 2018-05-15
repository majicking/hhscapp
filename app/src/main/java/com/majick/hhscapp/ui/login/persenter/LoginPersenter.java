package com.majick.hhscapp.ui.login.persenter;

import com.majick.hhscapp.base.BasePresenter;
import com.majick.hhscapp.http.ConsumerError;
import com.majick.hhscapp.model.UserModel;
import com.majick.hhscapp.ui.login.view.LoginView;
import com.majick.hhscapp.utils.Logutils;

public class LoginPersenter extends BasePresenter<LoginView, UserModel> {


    public void login(String name, String password) {

        mView.start();
        mRxManager.add(mModel.login(name, password).subscribe(
                loginInfo -> {
//            AppConfig.setLoginBean(loginInfo);//保存用户登录信息
                    Logutils.i(loginInfo.datas.key);
                }, new ConsumerError<Throwable>() {
                    @Override
                    public void onError(int errorCode, String message) {
                        mView.faild(message);
                    }
                }));
    }
//                    return mModel.getUserInfo(loginInfo.key, loginInfo.userid);
//                }).subscribe(userInfo -> {
////            AppConfig.setUserInfo(userInfo);//保存用户信息
//            mView.sucess("登陆成功！");
//        }, new ConsumerError<Throwable>() {
//            @Override
//            public void onError(int errorCode, String message) {
//                mView.faild(message);
//            }
//        });


}
