package com.majick.hhscapp.ui.register;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.majick.hhscapp.R;
import com.majick.hhscapp.app.AppConfig;
import com.majick.hhscapp.app.Constants;
import com.majick.hhscapp.base.BaseActivity;
import com.majick.hhscapp.model.RegisterModel;
import com.majick.hhscapp.ui.main.MainActivity;
import com.majick.hhscapp.utils.Logutils;
import com.majick.hhscapp.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.majick.hhscapp.app.Constants.MAINNUMBER;

public class Register2Activity extends BaseActivity<Register2Persenter, RegisterModel> implements RegisterView {

    @BindView(R.id.edusername)
    EditText edusername;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etPassword1)
    EditText etPassword1;
    @BindView(R.id.etcode)
    EditText etcode;
    @BindView(R.id.btnShowPassword)
    CheckBox btnShowPassword;
    @BindView(R.id.register_showpwd)
    Button registerShowpwd;
    @BindView(R.id.btnRegSubmit)
    Button btnRegSubmit;
    @BindView(R.id.registerselect3)
    LinearLayout registerselect3;
    @BindView(R.id.common_toolbar_title_tv)
    TextView commonToolbarTitleTv;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    private boolean isRegister;//信息是否填写完毕

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_register2;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initToolBarNav(commonToolbar, commonToolbarTitleTv, "手机绑定注册");
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkEditext();
            }
        };
        edusername.addTextChangedListener(watcher);
        etPassword.addTextChangedListener(watcher);
        etcode.addTextChangedListener(watcher);
        etPassword1.addTextChangedListener(watcher);

        //手机注册第二步
        btnShowPassword.setOnCheckedChangeListener((v, ischecked) -> {
            checkshowpwd(ischecked);
        });
        registerShowpwd.setOnClickListener(v -> {
            checkshowpwd(!btnShowPassword.isChecked());
        });
        //最后一步
        btnRegSubmit.setOnClickListener(v -> {
            if (isRegister) {
                mPresenter.registerMobile(
                        getIntent().getStringExtra(Constants.PHONE),
                        Utils.getEditText(edusername),
                        Utils.getEditText(etPassword),
                        getIntent().getStringExtra(Constants.CAPTCHA),
                        Utils.getEditText(etcode));
            } else {
                showToast("请完善资料");
            }
        });
    }

    //填完所有款项注册按钮能够点击
    private void checkEditext() {

        isRegister = false;
        if (Utils.isEmpty(edusername) && Utils.isEmpty(etPassword) && Utils.isEmpty(etPassword1)) {
            isRegister = true;
            btnRegSubmit.setActivated(true);
        } else {
            isRegister = false;
            btnRegSubmit.setActivated(false);
        }

    }


    //显示密码
    private void checkshowpwd(boolean ischeck) {
        if (ischeck) {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            etPassword1.setInputType(InputType.TYPE_CLASS_TEXT);
        } else {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            etPassword1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        btnShowPassword.setChecked(ischeck);
    }

    @Override
    public void setKeyCode(String code) {

    }

    @Override
    public void success(String msg) {
        showToast(msg);
        mPresenter.getUserInfo();
    }

    @Override
    public void fail(String msg) {
        showToast(msg);
    }

    @Override
    public void smsfail(String msg) {

    }

    @Override
    public void smsSuccess(String time) {

    }


    @Override
    public void loginSuccess() {
        Logutils.i("获取登陆信息成功" + AppConfig.getInfo().toString());
        Bundle bundle = new Bundle();
        bundle.putInt(MAINNUMBER, 3);
        readyGoThenKill(MainActivity.class, bundle);
    }

    @Override
    public void loginFail(String msg) {
        showToast(msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
