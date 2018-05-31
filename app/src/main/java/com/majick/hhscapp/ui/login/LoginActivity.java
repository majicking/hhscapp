package com.majick.hhscapp.ui.login;

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

import com.majick.hhscapp.R;
import com.majick.hhscapp.base.BaseActivity;
import com.majick.hhscapp.model.UserModel;
import com.majick.hhscapp.ui.main.MainActivity;
import com.majick.hhscapp.ui.register.RegisterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @Override
    protected void initView(Bundle savedInstanceState) {
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
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        hideLoadingDialog();
        readyGo(MainActivity.class);
    }

    @Override
    public void start() {
        showLoadingDialog("登陆中...");
    }

    @Override
    public void faild(String message) {
        hideLoadingDialog();
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

    }
}
