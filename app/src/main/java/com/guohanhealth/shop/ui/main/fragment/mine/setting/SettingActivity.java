package com.guohanhealth.shop.ui.main.fragment.mine.setting;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseActivity;
import com.guohanhealth.shop.bean.CartNumberInfo;
import com.guohanhealth.shop.bean.UserInfo;
import com.guohanhealth.shop.custom.CustomDialog;
import com.guohanhealth.shop.event.RxBus;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.ApiService;
import com.guohanhealth.shop.http.HttpErrorCode;
import com.guohanhealth.shop.ui.login.LoginActivity;
import com.guohanhealth.shop.utils.SharePreferenceUtils;
import com.guohanhealth.shop.utils.Utils;
import com.zcw.togglebutton.ToggleButton;

import java.io.IOException;
import java.util.Random;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SettingActivity extends BaseActivity {


    @BindView(R.id.common_toolbar_title_tv)
    TextView commonToolbarTitleTv;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.setting_logout)
    Button settingLogout;
    @BindView(R.id.toggle_notify)
    ToggleButton toggleNotify;
    @BindView(R.id.toggle_media)
    ToggleButton toggleMedia;
    @BindView(R.id.setting_editpwd)
    LinearLayout settingEditpwd;
    @BindView(R.id.setting_phonecheck)
    LinearLayout settingPhonecheck;
    @BindView(R.id.setting_paypwd)
    LinearLayout settingPaypwd;
    @BindView(R.id.setting_userback)
    LinearLayout settingUserback;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initToolBarNav(commonToolbar, commonToolbarTitleTv, "设置");
        if (Utils.isEmpty(App.getApp().getKey())) {
            settingLogout.setVisibility(View.VISIBLE);
        } else {
            settingLogout.setVisibility(View.GONE);
        }
        settingLogout.setOnClickListener(v -> {
            CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
            builder.setTitle("提示")
                    .setMessage("确认退出登录？")
                    .setPositiveButton("确定", (d, i) -> {
                        d.dismiss();
                        App.getApp().setKey("");
                        App.getApp().setUserid("");
                        App.getApp().setUsername("");
                        RxBus.getDefault().post(new CartNumberInfo());
                        App.getApp().setInfo(new UserInfo().member_info);
                        readyGoThenKill(LoginActivity.class);
                        App.getApp().getSocket().disconnect();
                        App.getApp().getSocket().io().reconnection(false);
                    })
                    .setNegativeButton("取消", (d, i) -> {
                        d.dismiss();
                    })
                    .create().show();
        });

        if ((Boolean) SharePreferenceUtils.getParam(mContext, Constants.ISNOTIFY, false)) {
            toggleNotify.setToggleOn();
        } else {
            toggleNotify.setToggleOff();
        }
        toggleNotify.setOnToggleChanged(isSelect -> {
            SharePreferenceUtils.setParam(mContext, Constants.ISNOTIFY, isSelect);
        });

        if ((Boolean) SharePreferenceUtils.getParam(mContext, Constants.ISMEDIAPLAYER, false)) {
            toggleMedia.setToggleOn();
        } else {
            toggleMedia.setToggleOff();
        }
        toggleMedia.setOnToggleChanged(isSelect -> {
            SharePreferenceUtils.setParam(mContext, Constants.ISMEDIAPLAYER, isSelect);
        });

        settingEditpwd.setOnClickListener(v -> {
            setPwd(1);
        });
        settingPhonecheck.setOnClickListener(v -> {
            setPwd(2);
        });
        settingPaypwd.setOnClickListener(v -> {
            setPwd(3);
        });
        settingUserback.setOnClickListener(v -> {
            readyGo(FeedBackActivity.class);
        });
    }

    public void setPwd(int type) {
        Api.get(ApiService.GET_MOBILE_INFO + "&key=" + App.getApp().getKey() + "&t=" + new Random().nextInt(10), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    showToast(Utils.getErrorString(e));
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    if (Utils.getCode(json) == HttpErrorCode.HTTP_NO_ERROR) {
                        if (Utils.getValue("state", Utils.getDatasString(json)).equals("true")) {
                            String mobile = Utils.getValue("mobile", Utils.getDatasString(json));
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.PHONENUMBER, mobile);
                            bundle.putInt(Constants.TYPE, type);
                            readyGo(CheckPhoneNumberActivity.class, bundle);
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putInt(Constants.TYPE, 0);
                            readyGo(BundlePhoneActivity.class, bundle);
                        }
                    } else {
                        runOnUiThread(() -> {
                            showToast(Utils.getErrorString(json));
                        });
                    }
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        showToast(Utils.getErrorString(e));
                    });
                }

            }
        });
    }


}
