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
import com.majick.hhscapp.app.AppConfig;
import com.majick.hhscapp.base.BaseActivity;
import com.majick.hhscapp.base.BaseModel;
import com.majick.hhscapp.ui.main.MainActivity;
import com.majick.hhscapp.ui.register.RegisterActivity;
import com.majick.hhscapp.utils.Logutils;

import butterknife.BindView;

import static com.majick.hhscapp.app.Constants.MAINNUMBER;

public class LoginActivity extends BaseActivity<LoginPersenter, BaseModel> implements LoginView {
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
        Logutils.i("获取登陆信息key="+ AppConfig.getKey());
        Bundle bundle = new Bundle();
        bundle.putInt(MAINNUMBER, 3);
        readyGoThenKill(MainActivity.class, bundle);
    }


    @Override
    public void faild(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}
