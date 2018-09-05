package com.guohanhealth.shop.ui.main.fragment.mine.setting;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
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
import java.util.Random;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class CheckPhoneNumberActivity extends BaseActivity {

    @BindView(R.id.common_toolbar_title_tv)
    TextView commonToolbarTitleTv;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.setting_paypwd)
    LinearLayout settingPaypwd;
    @BindView(R.id.btn2)
    Button btn2;
    @BindView(R.id.edit1)
    EditText edit1;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.edit2)
    EditText edit2;
    private String mobile;
    private int type = 2;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_updata_pwd;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initToolBarNav(commonToolbar, commonToolbarTitleTv, "身份验证");
        mobile = getIntent().getStringExtra(Constants.PHONENUMBER);
        type = getIntent().getIntExtra(Constants.TYPE, 2);
        if (Utils.isEmpty(mobile)) {
            text1.setText("当前手机号码" + Utils.getString(mobile));
        }
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
        edit2.addTextChangedListener(textWatcher);
        img.setOnClickListener(c -> {
            getImgCode();
        });
        btn1.setOnClickListener(v -> {
            if (!Utils.isEmpty(edit1)) {
                showToast("请输入图片验证码");
                return;
            }
            getMsgCode();
        });
        btn2.setOnClickListener(v -> {
            if (!Utils.isEmpty(edit1)) {
                showToast("请输入图片验证码");
                return;
            }
            if (!Utils.isEmpty(edit2)) {
                showToast("请输入短信验证码");
                return;
            }
            getData();
        });
    }

    public void getData() {
        String url = "";
        switch (type) {
            case 1:
                url = ApiService.MODIFY_PASSWORD_STEP3;
                break;
            case 2:
                url = ApiService.MODIFY_MOBILE_STEP3;
                break;
            case 3:
                url = ApiService.MODIFY_PAYPWD_STEP3;
                break;
        }
        Api.post(url, new FormBody.Builder()
                .add("auth_code", Utils.getEditViewText(edit2))
                .add("key", App.getApp().getKey())
                .build(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    getImgCode();
                    showToast(Utils.getErrorString(e));
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    if (Utils.getCode(json) == HttpErrorCode.HTTP_NO_ERROR) {
                        runOnUiThread(() -> {
                            if (Utils.getDatasString(json).equals("1")) {
                                if (type == 1 || type == 3) {
                                    /**登陆密码修改*/
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(Constants.TYPE, type);
                                    readyGoThenKill(SetPwdActivity.class, bundle);
                                } else if (type == 2) {
                                    /**手机验证*/
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(Constants.TYPE, 0);
                                    readyGoThenKill(BundlePhoneActivity.class, bundle);
                                }

                            }

                        });
                    } else {
                        runOnUiThread(() -> {
                            getImgCode();
                            showToast(Utils.getErrorString(json));
                        });
                    }
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        getImgCode();
                        showToast(Utils.getErrorString(e));
                    });
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getImgCode();
    }

    String codekey;

    public void getImgCode() {
        Api.get(ApiService.IMGKEYCODE + "&t=" + new Random().nextInt(10), new Callback() {
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
                            String code = Utils.getValue("codekey", Utils.getDatasString(json));
                            if (Utils.isEmpty(code)) {
                                codekey = code;
                                GlideEngine.getInstance().loadImage(mContext, img, Constants.KEYCODEURL + code);
                            }
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
    }

    public void getMsgCode() {
        String url = "";
        switch (type) {
            case 1:
                url = ApiService.MODIFY_PASSWORD_STEP2;
                break;
            case 2:
                url = ApiService.MODIFY_MOBILE_STEP2;
                break;
            case 3:
                url = ApiService.MODIFY_PAYPWD_STEP2;
                break;
        }
        Api.post(url, new FormBody.Builder()
                        .add("codekey", codekey)
                        .add("captcha", Utils.getEditViewText(edit1))
                        .add("key", App.getApp().getKey())
                        .build()
                , new Callback() {

                    private CountDownTimer timer;

                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(() -> {
                            getImgCode();
                            showToast(Utils.getErrorString(e));
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String json = response.body().string();
                        try {
                            if (Utils.getCode(json) == HttpErrorCode.HTTP_NO_ERROR) {
                                runOnUiThread(() -> {
                                    int time = 0;
                                    try {
                                        time = Integer.parseInt(Utils.getDatasString(json));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        time = 60;
                                    }
                                    timer = new CountDownTimer(time * 1000, 1000) {
                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                            btn1.setText("重试(" + (millisUntilFinished / 1000) + ")");
                                            btn1.setEnabled(false);
                                        }

                                        @Override
                                        public void onFinish() {
                                            getImgCode();
                                            btn1.setText("重新获取");
                                            btn1.setEnabled(true);
                                        }
                                    };
                                    timer.start();

                                });

                            } else {
                                runOnUiThread(() -> {
                                    showToast(Utils.getErrorString(json));
                                    getImgCode();
                                });

                            }
                        } catch (Exception e) {
                            runOnUiThread(() -> {
                                getImgCode();
                                showToast(Utils.getErrorString(e));
                            });
                        }

                    }
                });
    }

    boolean isEmpty;

    //填完所有款项注册按钮能够点击
    private void checkAllEdit() {
        isEmpty = false;
        if (Utils.isEmpty(edit1) && Utils.isEmpty(edit2)) {
            isEmpty = true;
            btn2.setActivated(true);
        } else {
            isEmpty = false;
            btn2.setActivated(false);
        }
    }
}
