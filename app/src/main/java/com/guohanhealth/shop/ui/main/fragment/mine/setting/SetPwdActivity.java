package com.guohanhealth.shop.ui.main.fragment.mine.setting;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseActivity;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.ApiService;
import com.guohanhealth.shop.http.HttpErrorCode;
import com.guohanhealth.shop.utils.Utils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class SetPwdActivity extends BaseActivity {


    @BindView(R.id.common_toolbar_title_tv)
    TextView commonToolbarTitleTv;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.edit1)
    EditText edit1;
    @BindView(R.id.edit2)
    EditText edit2;
    @BindView(R.id.btn)
    Button btn;
    int type;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_set_pwd;
    }

    String url = "";

    @Override
    protected void initView(Bundle savedInstanceState) {

        type = getIntent().getIntExtra(Constants.TYPE, 1);
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkAllEdit();
            }
        };
        edit1.addTextChangedListener(watcher);
        edit2.addTextChangedListener(watcher);

        switch (type) {
            case 1:
                initToolBarNav(commonToolbar, commonToolbarTitleTv, "设置登陆密码");
                url = ApiService.MODIFY_PASSWORD_STEP5;
                break;
            case 3:
                initToolBarNav(commonToolbar, commonToolbarTitleTv, "设置支付密码");
                url = ApiService.MODIFY_PAYPWD_STEP5;
                break;
        }
        btn.setOnClickListener(v -> {
            if (!Utils.isEmpty(edit1) || !Utils.isEmpty(edit2)) {
                showToast("请输入密码");
                return;
            }
            Api.post(url, new FormBody.Builder()
                    .add("password", Utils.getEditViewText(edit1))
                    .add("password1", Utils.getEditViewText(edit2))
                    .add("key", App.getApp().getKey())
                    .build(), new Callback() {
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
                            if (Utils.getDatasString(json).equals("1")) {
                                runOnUiThread(() -> {
                                    showToast("密码设置成功");
                                    finish();
                                });

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
        });
    }

    public boolean isEmpty;

    //填完所有款项注册按钮能够点击
    private void checkAllEdit() {
        isEmpty = false;
        if (Utils.isEmpty(edit1) && Utils.isEmpty(edit2)) {
            isEmpty = true;
            btn.setActivated(true);
        } else {
            isEmpty = false;
            btn.setActivated(false);
        }
    }
}
