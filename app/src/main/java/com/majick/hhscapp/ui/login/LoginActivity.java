package com.majick.hhscapp.ui.login;

import android.os.Bundle;
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
import com.majick.hhscapp.ui.login.persenter.LoginPersenter;
import com.majick.hhscapp.ui.login.view.LoginView;

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

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_log_inctivity;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        btnLogin.setOnClickListener(view -> {
            mPresenter.login(username.getText().toString().trim(), password.getText().toString().trim());
        });

    }

    @Override
    public void sucess(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        hideLoadingDialog();
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
