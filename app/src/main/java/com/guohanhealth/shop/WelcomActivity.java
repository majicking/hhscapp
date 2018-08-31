package com.guohanhealth.shop;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.ui.main.activity.MainActivity;

public class WelcomActivity extends Activity {
    int time = 1;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStatus();
        }
        setContentView(R.layout.activity_welcom);
        imageView = findViewById(R.id.splash_loading_item);
        Animation translate = AnimationUtils.loadAnimation(this,
                R.anim.splash_loading);
        translate.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                //启动homeactivty，相当于Intent
                startActivity(new Intent(WelcomActivity.this, MainActivity.class).putExtra(Constants.MAINNUMBER, 0));

                overridePendingTransition(R.anim.fade_in,
                        R.anim.fade_out);
                ActivityCompat.requestPermissions(WelcomActivity.this,
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.CALL_PHONE,
                                Manifest.permission.CLEAR_APP_CACHE,
                                Manifest.permission.READ_CONTACTS,
                                Manifest.permission.RECORD_AUDIO
                        }, 1);
                WelcomActivity.this.finish();
            }
        });
        imageView.setAnimation(translate);

//        new CountDownTimer(time * 1000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//            }
//
//            @Override
//            public void onFinish() {
//                startActivity(new Intent(WelcomActivity.this, MainActivity.class));
//                finish();
//            }
//        }.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setStatus() {
        //        獲取系統對象
        Window window = getWindow();
        //設置狀態欄全屏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //設置底部全屏
//      window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //需要设置这个标志才能调用setStatusBarColor来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //設置狀態欄透明色
        try {
            window.setStatusBarColor(Color.TRANSPARENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //設置底部狀態欄全屏
        //window.setNavigationBarColor(Color.TRANSPARENT);
        //設置佈局全屏
        window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }
}
