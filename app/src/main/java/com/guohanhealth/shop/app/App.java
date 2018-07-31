package com.guohanhealth.shop.app;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;

import com.github.nkzawa.emitter.Emitter.Listener;
import com.github.nkzawa.engineio.client.Transport;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Manager;
import com.github.nkzawa.socketio.client.Socket;

import com.guohanhealth.shop.BuildConfig;
import com.guohanhealth.shop.R;
import com.guohanhealth.shop.bean.UserInfo;
import com.guohanhealth.shop.bean.UserInfo.Member_info;
import com.guohanhealth.shop.bean.greendao.DaoMaster;
import com.guohanhealth.shop.bean.greendao.DaoSession;
import com.guohanhealth.shop.event.ChatEvent;
import com.guohanhealth.shop.event.ObjectEvent;
import com.guohanhealth.shop.event.RxBus;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.RxHelper;
import com.guohanhealth.shop.http.RxManager;
import com.guohanhealth.shop.utils.Logutils;
import com.guohanhealth.shop.utils.Utils;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.greendao.database.Database;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Map;
import java.util.Random;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        sharedPreferences = getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE);
        getHotWords();  /**获取热门词*/
        UpdataUserInfo(getKey(), getUserid());/**更新用户信息*/
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(this, null);
        boolean registerApp = iwxapi.registerApp(Constants.WX_APP_ID);
        initDB();
        initChat();
    }

    private Socket mSocket;
    private String DATA_BASE_NAME = BuildConfig.APPLICATION_ID;
    private static DaoSession mdaoSession;
    private static App app;

    public static App getApp() {
        return app;
    }

    public Socket getSocket() {
        return mSocket;
    }

    boolean isNotify = true;
    boolean isConnect = false;
    /**
     * 消息通知
     */
    private Notification mNotification;
    private NotificationManager mNotificationManager;

    public Notification getNotification() {
        return mNotification;
    }

    public void setNotification(Notification notification) {
        mNotification = notification;
    }

    public NotificationManager getNotificationManager() {
        return mNotificationManager;
    }

    public void setNotificationManager(NotificationManager notificationManager) {
        mNotificationManager = notificationManager;
    }

    public boolean isNotify() {
        return isNotify;
    }

    public void setNotify(boolean notify) {
        isNotify = notify;
    }

    public boolean isConnect() {
        return isConnect;
    }

    public void setConnect(boolean connect) {
        isConnect = connect;
    }

    /**
     * 聊天socketio
     */
    private void initChat() {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotification = new Notification(R.mipmap.ic_launcher_logo, getString(R.string.app_name), System.currentTimeMillis());
        try {
            IO.Options options = new IO.Options();
            options.host = Constants.IM_HOST;
            //连接Socket
            mSocket = IO.socket(Constants.IM_HOST, options);
            mSocket.io().reconnectionDelay(2000);
            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            Logutils.i(e.toString());
        }
        mSocket.io().on(Manager.EVENT_TRANSPORT, args -> {
            Transport transport = (Transport) args[0];
            transport.on(Transport.EVENT_REQUEST_HEADERS, args1 -> {
                @SuppressWarnings("unchecked")
                Map<String, String> headers = (Map<String, String>) args1[0];
                headers.put("Referer", Constants.IM_HOST);
                headers.put("Origin", Constants.IM_HOST);

            });
            transport.on(Transport.EVENT_RESPONSE_HEADERS, new Listener() {
                @Override
                public void call(Object... args) {
                    @SuppressWarnings("unchecked")
                    Map<String, String> headers = (Map<String, String>) args[0];
                    String Referer = headers.get("Referer");
                    String Origin = headers.get("Origin");
                }
            });
        });
        //通知已连接
        mSocket.on(Socket.EVENT_CONNECT, args -> {
            Logutils.i("---------------->已连接");
            UpDateUser();

        });
        mSocket.on(Socket.EVENT_ERROR, v -> {
            Logutils.i("---------------->错误");
        });
        //通知已断开
        mSocket.on(Socket.EVENT_DISCONNECT, args -> {
            Logutils.i("---------------->已断开");
            isConnect = false;////设置链接失败
            mNotification.tickerText = "您的IM帐号已离线";
//            Intent intent = new Intent(getApplicationContext(), IMFriendsListActivity.class);
//            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
//              mNotification.setLatestEventInfo(getApplicationContext(), "", "",contentIntent);//ShopNC商城客户端
//            mNotificationManager.notify(-1, mNotification);// 通知一下才会生效哦
//            mNotificationManager.cancel(-1);
        });
        //获取node消息
        mSocket.on("get_msg", get_msg -> {
            String message = get_msg[0].toString();
            isConnect = true;//设置链接成功
            if (!message.equals("{}")) {
                if (isNotify()) {
                    Logutils.i("连接成功了-------------");
                    mNotification.tickerText = "新消息注意查收";
//                        Intent intent = new Intent(getApplicationContext(), IMNewListActivity.class);
//                        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
//                        mNotification.setLatestEventInfo(getApplicationContext(), "消息提示", "有新消息注意查收", contentIntent);//ShopNC商城客户端
//                        mNotificationManager.notify(13, mNotification);// 通知一下才会生效哦
                } else {
                    Logutils.i("message" + message);
                    RxBus.getDefault().post(new ChatEvent("message", message));
                }
            }
        });
        mSocket.on("get_state", obj -> {
            String get_state = obj[0].toString();
            Logutils.i("get_state=" + get_state);
            RxBus.getDefault().post(new ChatEvent("state", get_state));
        });
    }

    /**
     * node 更新会员状态
     */
    public void UpDateUser() {
        if (Utils.isEmpty(getInfo().member_id)) {
            try {
                String update_user = "{\"u_id\":\"" + getInfo().member_id + "\",\"u_name\":\"" + getUsername() + "\",\"avatar\":\"" + getInfo().member_avatar + "\"}";
                mSocket.emit("update_user", new JSONObject(update_user));
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 数据库初始化
     */
    private void initDB() {
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
        UserInfo.Member_info info = new UserInfo.Member_info();
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
