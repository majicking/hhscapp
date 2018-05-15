package com.majick.hhscapp.app;

import android.app.Application;

import com.majick.hhscapp.http.ApiService;

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

    public static App getAppContext() {
        return app;
    }

    public Socket getSocket() {
        return mSocket;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        initLogger();
    }

    private void initLogger() {
        //DEBUG版本才打控制台log
//        if (BuildConfig.DEBUG) {
//            Logger.addLogAdapter(new AndroidLogAdapter(PrettyFormatStrategy.newBuilder().
//                    tag(getString(R.string.app_name)).build()));
//        }
        //把log存到本地
//        Logger.addLogAdapter(new DiskLogAdapter(TxtFormatStrategy.newBuilder().
//                tag(getString(R.string.app_name)).build(getPackageName(), getString(R.string.app_name))));
    }

}
