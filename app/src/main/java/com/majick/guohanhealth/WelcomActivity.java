package com.majick.guohanhealth;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Bundle;

import com.majick.guohanhealth.ui.main.activity.MainActivity;

public class WelcomActivity extends Activity {
    int time = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //獲取系統對象
//        Window window = getWindow();
//        //設置狀態欄全屏
//        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        //設置底部全屏
////      window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        //需要设置这个标志才能调用setStatusBarColor来设置状态栏颜色
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        //設置狀態欄透明色
//        window.setStatusBarColor(Color.TRANSPARENT);
//        //設置底部狀態欄全屏
//        //window.setNavigationBarColor(Color.TRANSPARENT);
//        //設置佈局全屏
//        window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        setContentView(R.layout.activity_welcom);
        new CountDownTimer(time * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                startActivity(new Intent(WelcomActivity.this, MainActivity.class));
                finish();
            }
        }.start();
    }
}
