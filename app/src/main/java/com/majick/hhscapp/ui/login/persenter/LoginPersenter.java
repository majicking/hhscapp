package com.majick.hhscapp.ui.login.persenter;

import android.widget.Toast;

import com.majick.hhscapp.R;
import com.majick.hhscapp.app.AppConfig;
import com.majick.hhscapp.base.BasePresenter;
import com.majick.hhscapp.bean.LoginBean;
import com.majick.hhscapp.http.ConsumerError;
import com.majick.hhscapp.http.Result;
import com.majick.hhscapp.model.UserModel;
import com.majick.hhscapp.ui.login.view.LoginView;
import com.majick.hhscapp.utils.Logutils;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class LoginPersenter extends BasePresenter<LoginView, UserModel> {


    public void login(String name, String password) {

        mView.start();
        mRxManager.add(mModel.login(name, password).flatMap(userLoginInfo -> {
            Logutils.i(userLoginInfo.datas.key);
            return mModel.getUserInfo(userLoginInfo.datas.key, userLoginInfo.datas.userid);
        }).subscribe(userInfo -> {
            Logutils.i(userInfo.datas.member_name);
            AppConfig.setUserInfo(userInfo.datas);//保存用户信息
            mView.sucess("登陆成功");
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                mView.faild(message);
            }
        }));
    }

}
