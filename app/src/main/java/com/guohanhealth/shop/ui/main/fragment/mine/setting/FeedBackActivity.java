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
import com.guohanhealth.shop.utils.engine.GlideEngine;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class FeedBackActivity extends BaseActivity {


    @BindView(R.id.common_toolbar_title_tv)
    TextView commonToolbarTitleTv;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.edit1)
    EditText edit1;
    @BindView(R.id.btn)
    Button btn;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_feed_back;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initToolBarNav(commonToolbar, commonToolbarTitleTv, "用户反馈");
        TextWatcher textWatcher = new TextWatcher() {
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
        edit1.addTextChangedListener(textWatcher);
        btn.setOnClickListener(v -> {
            if (!Utils.isEmpty(edit1)) {
                showToast("请输入反馈内容");
                return;
            }
            Api.post(ApiService.FEEDBACK_ADD, new FormBody.Builder()
                    .add("key", App.getApp().getKey())
                    .add("feedback", Utils.getEditViewText(edit1))
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
                            runOnUiThread(() -> {
                                showToast("提交成功！");
                                finish();
                            });
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

    boolean isEmpty;

    private void checkAllEdit() {
        isEmpty = false;
        if (Utils.isEmpty(edit1)) {
            isEmpty = true;
            btn.setActivated(true);
        } else {
            isEmpty = false;
            btn.setActivated(false);
        }
    }

}
