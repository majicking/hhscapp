package com.guohanhealth.shop.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.base.BaseActivity;
import com.guohanhealth.shop.base.BaseAppManager;
import com.guohanhealth.shop.ui.main.activity.MainActivity;
import com.guohanhealth.shop.ui.register.RegisterActivity;
import com.guohanhealth.shop.utils.Logutils;

import butterknife.BindView;

import static com.guohanhealth.shop.app.Constants.MAINNUMBER;

public class LoginActivity extends BaseActivity<LoginPersenter, UserModel> implements LoginView {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.ivThreeLogin)
    ImageView ivThreeLogin;
    @BindView(R.id.btnQQ)
    ImageButton btnQQ;
    @BindView(R.id.btnWeiXin)
    ImageButton btnWeiXin;
    @BindView(R.id.btnSina)
    ImageButton btnSina;
    @BindView(R.id.ThreebtnLogin)
    LinearLayout ThreebtnLogin;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.btnregist)
    TextView btnregist;
    @BindView(R.id.forgetpwd)
    TextView forgetpwd;
    @BindView(R.id.back)
    TextView back;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_log_inctivity;
    }

    private int type = 0;

    @Override
    protected void initView(Bundle savedInstanceState) {
        username.setText(getIntent().getStringExtra("username"));
        password.setText(getIntent().getStringExtra("password"));
        type = getIntent().getIntExtra("type", 0);
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && s.length() > 0) {
                    btnLogin.setActivated(true);
                    btnLogin.setTextColor(getResources().getColor(R.color.white));
                } else {
                    btnLogin.setTextColor(getResources().getColor(R.color.djk_textcolor555));
                    btnLogin.setActivated(false);

                }
            }
        });
        btnLogin.setOnClickListener(view -> {
            if (btnLogin.isActivated()) {
                String name = username.getText().toString().trim();
                String pwd = password.getText().toString().trim();
                mPresenter.login(name, pwd);
            } else {
                showToast("请输入账号");
            }
        });
        btnregist.setOnClickListener(v -> readyGo(RegisterActivity.class));
        back.setOnClickListener(v -> finish());
    }


    @Override
    public void sucess(String message) {
        showToast(message);
        Logutils.i("获取登陆信息key=" + App.getApp().getKey());
        App.getApp().setIsLogin(true);
        finish();
        if (type > 0) {
            Bundle bundle = new Bundle();
            bundle.putInt(MAINNUMBER, 3);
            readyGoThenKill(MainActivity.class, bundle);
        }

    }


    @Override
    public void faild(String message) {
        showToast(message);
        App.getApp().setIsLogin(false);
    }
}
