package com.majick.guohanhealth.app;

import android.app.Application;
import android.content.SharedPreferences;

import com.majick.guohanhealth.BuildConfig;
import com.majick.guohanhealth.bean.LoginBean;
import com.majick.guohanhealth.bean.UserInfo;
import com.majick.guohanhealth.bean.UserInfo.*;
import com.majick.guohanhealth.http.Api;
import com.majick.guohanhealth.http.RxHelper;
import com.majick.guohanhealth.http.RxManager;
import com.majick.guohanhealth.utils.Logutils;
import com.majick.guohanhealth.utils.Utils;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class App extends Application {
    private Socket mSocket;

    {
        try {
            mSocket = IO.socket(Constants.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static App app;

    public static App getApp() {
        return app;
    }

    public Socket getSocket() {
        return mSocket;
    }


    private String key;
    private String userid;
    private String username;
    private SharedPreferences sharedPreferences;
    private Member_info info;

    public Member_info getInfo() {
        Member_info info = (new UserInfo()).member_info;
        info.member_id = sharedPreferences.getString(Constants.MEMBER_ID, "");
        info.member_name = sharedPreferences.getString(Constants.MEMBER_NAME, "");
        info.member_avatar = sharedPreferences.getString(Constants.MEMBER_AVATAR, "");
        info.store_name = sharedPreferences.getString(Constants.STORE_NAME, "");
        info.grade_id = sharedPreferences.getString(Constants.GRADE_ID, "");
        info.store_id = sharedPreferences.getString(Constants.STORE_ID, "");
        info.seller_name = sharedPreferences.getString(Constants.SELLER_NAME, "");
        return info;
    }

    public void setInfo(Member_info info) {
        this.info = info;
        try {
            sharedPreferences.edit().putString(Constants.MEMBER_ID, this.info == null ? "" : Utils.getString(this.info.member_id)).commit();
            sharedPreferences.edit().putString(Constants.MEMBER_NAME, this.info == null ? "" : Utils.getString(this.info.member_name)).commit();
            sharedPreferences.edit().putString(Constants.MEMBER_AVATAR, this.info == null ? "" : Utils.getString(this.info.member_avatar)).commit();
            sharedPreferences.edit().putString(Constants.STORE_NAME, this.info == null ? "" : Utils.getString(this.info.store_name)).commit();
            sharedPreferences.edit().putString(Constants.GRADE_ID, this.info == null ? "" : Utils.getString(this.info.grade_id)).commit();
            sharedPreferences.edit().putString(Constants.STORE_ID, this.info == null ? "" : Utils.getString(this.info.store_id)).commit();
            sharedPreferences.edit().putString(Constants.SELLER_NAME, this.info == null ? "" : Utils.getString(this.info.seller_name)).commit();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public String getKey() {
        return sharedPreferences.getString(Constants.KEY, "");
    }

    public void setKey(String key) {
        this.key = key;
        sharedPreferences.edit().putString(Constants.KEY, this.key).commit();
    }

    public String getUserid() {
        return sharedPreferences.getString(Constants.USERID, "");
    }

    public void setUserid(String userid) {
        this.userid = userid;
        sharedPreferences.edit().putString(Constants.USERID, this.userid).commit();
    }

    public String getUsername() {
        return sharedPreferences.getString(Constants.USERNAME, "");
    }

    public void setUsername(String username) {
        this.username = username;
        sharedPreferences.edit().putString(Constants.USERNAME, this.username).commit();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        initLogger();
        sharedPreferences = getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE);
//        UpdataUserInfo(getKey(), getUserid());
    }

    private void UpdataUserInfo(String key, String userid) {
        new RxManager().add((Api.getDefault().getUserInfo(key, userid, "member_id").compose(RxHelper.handleResult())).subscribe(userinfo -> {
            App.getApp().setInfo(userinfo.member_info);
        }, throwable -> {
            Logutils.i(throwable.getMessage());
        }));
    }

    private void initLogger() {
    }

}