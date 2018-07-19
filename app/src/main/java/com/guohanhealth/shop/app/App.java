package com.guohanhealth.shop.app;

import android.app.Application;
import android.content.SharedPreferences;

import com.guohanhealth.shop.BuildConfig;
import com.guohanhealth.shop.bean.LoginBean;
import com.guohanhealth.shop.bean.PaySignInfo;
import com.guohanhealth.shop.bean.SearchWordsInfo;
import com.guohanhealth.shop.bean.UserInfo;
import com.guohanhealth.shop.bean.UserInfo.*;
import com.guohanhealth.shop.bean.greendao.DaoMaster;
import com.guohanhealth.shop.bean.greendao.DaoSession;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.RxHelper;
import com.guohanhealth.shop.http.RxManager;
import com.guohanhealth.shop.utils.Logutils;
import com.guohanhealth.shop.utils.Utils;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.greendao.database.Database;

import java.net.URISyntaxException;
import java.util.Random;

import io.reactivex.ObservableSource;
import io.socket.client.IO;
import io.socket.client.Socket;

public class App extends Application {
    private Socket mSocket;
    private String DATA_BASE_NAME = BuildConfig.APPLICATION_ID;
    private static DaoSession mdaoSession;

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

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        sharedPreferences = getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE);
        getHotWords();  /**获取热门词*/
        UpdataUserInfo(getKey(), getUserid());/**更新用户信息*/
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(this, null);
        boolean registerApp = iwxapi.registerApp(Constants.WX_APP_ID);
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(this, DATA_BASE_NAME);
        Database db = openHelper.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        mdaoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoSession() {
        return mdaoSession;
    }

    private String key;
    private String userid;
    private String username;
    private String hotname;
    private String hotvalue;
    private SharedPreferences sharedPreferences;
    private Member_info info;
    private int page_total = 1;
    private boolean hasmore;
    private String notify_url;
    private String pay_sn;

    public String getPay_sn() {
        return pay_sn;
    }

    public void setPay_sn(String pay_sn) {
        this.pay_sn = pay_sn;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public int getPage_total() {
        return page_total;
    }

    public void setPage_total(int page_total) {
        this.page_total = page_total;
    }

    public boolean Hasmore() {
        return hasmore;
    }

    public void setHasmore(boolean hasmore) {
        this.hasmore = hasmore;
    }

    public String getHotname() {
        return sharedPreferences.getString(Constants.HOTNAME, "");
    }

    public void setHotname(String hotname) {
        this.hotname = hotname;
        sharedPreferences.edit().putString(Constants.HOTNAME, hotname).commit();
    }

    public String getHotvalue() {
        return sharedPreferences.getString(Constants.HOTVALUE, "");
    }

    public void setHotvalue(String hotvalue) {
        this.hotvalue = hotvalue;
        sharedPreferences.edit().putString(Constants.HOTVALUE, hotvalue).commit();
    }

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


    private void UpdataUserInfo(String key, String userid) {
        new RxManager().add((Api.getDefault().getUserInfo(key, userid, "member_id").compose(RxHelper.handleResult())).subscribe(userinfo -> {
            Logutils.i("自动登陆成功" + userinfo.member_info.toString());
            App.getApp().setInfo(userinfo.member_info);
            setIsLogin(true);
        }, throwable -> {
            setIsLogin(false);
            Logutils.i("自动登陆失败：" + throwable.getMessage());
        }));
    }


    private void getHotWords() {
        new RxManager().add(Api.getDefault().getSearchDataWords().compose(RxHelper.handleResult()).subscribe(info -> {
            if (info != null && info.hot_info != null && info.hot_info.size() > 0) {
                App.getApp().setHotname(info.hot_info.get(new Random().nextInt(info.hot_info.size())).name);
                App.getApp().setHotvalue(info.hot_info.get(new Random().nextInt(info.hot_info.size())).value);
                Logutils.i("获取热词：" + info);
            } else {
                Logutils.i("热词为空");
            }
        }, throwable -> {
            Logutils.i("热词失败：" + throwable.getMessage());
        }));
    }

    private boolean isLogin;

    public boolean isLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

}
