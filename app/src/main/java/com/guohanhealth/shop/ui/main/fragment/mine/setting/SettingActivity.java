package com.guohanhealth.shop.ui.main.fragment.mine.setting;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseActivity;
import com.guohanhealth.shop.bean.CartNumberInfo;
import com.guohanhealth.shop.bean.UserInfo;
import com.guohanhealth.shop.custom.CustomDialog;
import com.guohanhealth.shop.event.RxBus;
import com.guohanhealth.shop.ui.login.LoginActivity;
import com.guohanhealth.shop.utils.SharePreferenceUtils;
import com.guohanhealth.shop.utils.Utils;
import com.zcw.togglebutton.ToggleButton;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    }


    @Override
    public void faild(String msg) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
