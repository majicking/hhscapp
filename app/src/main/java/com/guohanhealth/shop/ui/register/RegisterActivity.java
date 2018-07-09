package com.guohanhealth.shop.ui.register;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.guohanhealth.shop.ui.WebViewActivity;
import com.guohanhealth.shop.ui.login.LoginActivity;
import com.guohanhealth.shop.ui.main.activity.MainActivity;
import com.guohanhealth.shop.utils.Logutils;
import com.guohanhealth.shop.utils.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import cn.carbs.android.segmentcontrolview.library.SegmentControlView;

import static com.guohanhealth.shop.app.Constants.MAINNUMBER;

public class RegisterActivity extends BaseActivity<RegisterPersenter, RegisterModel> implements RegisterView {


    @BindView(R.id.register_username)
    EditText registerUsername;
    @BindView(R.id.register_pwd)
    EditText registerPwd;
    @BindView(R.id.register_checkpwd)
    EditText registerCheckpwd;
    @BindView(R.id.register_emil)
    EditText registerEmil;
    @BindView(R.id.register_invitationcode)
    EditText registerInvitationcode;
    @BindView(R.id.register_checkbox)
    CheckBox registerCheckbox;
    @BindView(R.id.register_agreement)
    TextView registerAgreement;
    @BindView(R.id.register_btn)
    Button registerBtn;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.common_toolbar_title_tv)
    TextView commonToolbarTitleTv;
    @BindView(R.id.segmentcontrolview)
    SegmentControlView segmentcontrolview;
    @BindView(R.id.registerselect1)
    LinearLayout registerselect1;
    @BindView(R.id.register_mobile)
    EditText registerMobile;
    @BindView(R.id.register_checkcode)
    EditText registerCheckcode;
    @BindView(R.id.register_imgcheckcode)
    ImageView registerImgcheckcode;
    @BindView(R.id.register_checkbox1)
    CheckBox registerCheckbox1;
    @BindView(R.id.register_agreement1)
    TextView registerAgreement1;
    @BindView(R.id.registerselect2)
    LinearLayout registerselect2;
    @BindView(R.id.register_smscode)
    EditText registerSmscode;
    @BindView(R.id.register_btngetsmscode)
    Button registerBtngetsmscode;
    @BindView(R.id.register_next)
    Button registerNext;

    private boolean flag;//注册type false 普通注册 true 手机注册
    private boolean isRegister;
    private String code;
    private long time = 60;
    private CountDownTimer timer;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initToolBarNav(commonToolbar, commonToolbarTitleTv, "注册");
        segmentcontrolview.setOnSegmentChangedListener(index -> {
            changeRegisterType(index);
        });
        //普通注册
        registerCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            registerCheckbox.setChecked(isChecked);
            checkAllEdit();
        });
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
        registerUsername.addTextChangedListener(watcher);
        registerPwd.addTextChangedListener(watcher);
        registerCheckpwd.addTextChangedListener(watcher);
        registerEmil.addTextChangedListener(watcher);
        registerBtn.setOnClickListener(v -> {
            if (isRegister) {
                mPresenter.register(registerUsername.getText().toString(),
                        registerPwd.getText().toString(),
                        registerCheckpwd.getText().toString(),
                        registerEmil.getText().toString(),
                        registerCheckcode.getText().toString()
                );
            } else {
                showToast("请完善资料");
            }
        });


        //手机注册第一步
        registerImgcheckcode.setOnClickListener(v -> mPresenter.getImgCode());
        registerBtngetsmscode.setActivated(true);
        registerCheckbox1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            registerCheckbox1.setChecked(isChecked);
            checkAllMobileEdit();
        });
        registerBtngetsmscode.setOnClickListener(v -> {
            if (registerBtngetsmscode.isActivated()) {
                mPresenter.getSMSCode(registerMobile.getText().toString(), code, registerCheckcode.getText().toString());
            }
        });
        TextWatcher watcher1 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkAllMobileEdit();
            }
        };
        registerMobile.addTextChangedListener(watcher1);
        registerCheckcode.addTextChangedListener(watcher1);
        registerSmscode.addTextChangedListener(watcher1);
        registerNext.setOnClickListener(v -> {
            if (isRegister) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PHONE, Utils.getEditViewText(registerMobile));
                bundle.putString(Constants.CAPTCHA, Utils.getEditViewText(registerSmscode));
                readyGo(Register2Activity.class, bundle);
            } else {
                showToast("请完善资料");
            }
        });

        registerAgreement.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("url", Constants.REGISTERARGMENT);
            readyGo(WebViewActivity.class, bundle);
        });
        registerAgreement1.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("url", Constants.REGISTERARGMENT);
            readyGo(WebViewActivity.class, bundle);
        });
    }


    private void checkAllMobileEdit() {
        isRegister = false;
        if (Utils.isEmpty(registerSmscode) && Utils.isEmpty(registerMobile) && Utils.isEmpty(registerCheckcode) && registerCheckbox1.isChecked()) {
            isRegister = true;
            registerNext.setActivated(true);
        } else {
            registerNext.setActivated(false);
            isRegister = false;
        }
    }


    //填完所有款项注册按钮能够点击
    private void checkAllEdit() {
        isRegister = false;
        if (Utils.isEmpty(registerEmil) && Utils.isEmpty(registerPwd) && Utils.isEmpty(registerCheckpwd) && Utils.isEmpty(registerUsername) && registerCheckbox.isChecked()) {
            isRegister = true;
            registerBtn.setActivated(true);
        } else {
            isRegister = false;
            registerBtn.setActivated(false);
        }
    }

    //切换注册方式
    private void changeRegisterType(int index) {
        if (index == 0) {
            flag = false;
            registerselect1.setVisibility(View.VISIBLE);
            registerselect2.setVisibility(View.GONE);
        } else {
            flag = true;
            registerselect1.setVisibility(View.GONE);
            registerselect2.setVisibility(View.VISIBLE);
            mPresenter.getImgCode();
        }
    }


    @Override
    public void setKeyCode(String code) {
        this.code = code;
        Glide.with(mContext)
                .load(Constants.KEYCODEURL + code)
                .transition(new DrawableTransitionOptions().crossFade(500))
                .into(registerImgcheckcode);
    }

    @Override
    public void success(String msg) {
        showToast(msg);
        mPresenter.getUserInfo(App.getApp().getKey(), App.getApp().getUserid());
    }


    @Override
    public void faild(String msg) {
        showToast(msg);
    }

    @Override
    public void smsfail(String msg) {
        showToast(msg);
        mPresenter.getImgCode();

    }

    @Override
    public void smsSuccess(String msg) {
        showToast("发送成功");
        try {
            time = Integer.parseInt(msg);
        } catch (Exception e) {
            e.printStackTrace();
            time = 60;
        }

////        【国翰大健康】您申请注册会员，验证码：837973
//        AutoVerifyCodeConfig config = new AutoVerifyCodeConfig.Builder()
//                .codeLength(6) // 验证码长度
//                .smsCodeType(AutoVerifyCodeConfig.CODE_TYPE_NUMBER)  //验证码类型
//                .smsSenderStart("10690146") // 验证码发送者号码的前几位数字
////                .smsSender("1069014642593665993") // 验证码发送者的号码
//                .smsBodyStartWith("【国翰大健康】") // 设置验证码短信开头文字，固定可以设置
//                .smsBodyContains("您申请注册会员，验证码：") // 设置验证码短信内容包含文字，每个功能包含不一样，例如注册、重置密码
//                .build();
//        AutoVerifyCode.getInstance()
//                .with(mContext)
//                .config(config)  //验证码选项配置
//                .smsCallback(new MessageCallBack())  //短信内容回调
//                .permissionCallback(new PerCallBack())  //短信短信回调
//                .inputCompleteCallback(text -> {
//                    //自动输入完毕，可以进行登录等等操作
//                    Logutils.i("自动输入验证码完成" + text);
//                })
//                .into(registerCheckcode)  //要输入的View
//                .start();       //开始
        timer = new CountDownTimer(time * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                registerBtngetsmscode.setText("重试(" + (millisUntilFinished / 1000) + ")");
                registerBtngetsmscode.setActivated(false);
            }

            @Override
            public void onFinish() {
                registerBtngetsmscode.setText("重新获取");
                registerBtngetsmscode.setActivated(true);
            }
        };
        timer.start();
    }

    @Override
    public void loginSuccess() {
        Logutils.i("获取登陆信息成功");
        Bundle bundle = new Bundle();
        bundle.putInt(MAINNUMBER, 3);
        readyGoThenKill(MainActivity.class, bundle);
        App.getApp().setIsLogin(true);
    }

    @Override
    public void loginFail(String msg) {
        showToast(msg);
        Bundle bundle = new Bundle();
        bundle.putString("username", Utils.getEditViewText(registerUsername));
        bundle.putString("password", Utils.getEditViewText(registerPwd));
        bundle.putInt("type", 2);
        readyGoThenKill(LoginActivity.class, bundle);
        App.getApp().setIsLogin(false);
    }


    /**
     * 从短信字符窜提取验证码
     *
     * @param body      短信内容
     * @param YZMLENGTH 验证码的长度 一般6位或者4位
     * @return 接取出来的验证码
     */
    public static String getyzm(String body, int YZMLENGTH) {
        // 首先([a-zA-Z0-9]{YZMLENGTH})是得到一个连续的六位数字字母组合
        // (?<![a-zA-Z0-9])负向断言([0-9]{YZMLENGTH})前面不能有数字
        // (?![a-zA-Z0-9])断言([0-9]{YZMLENGTH})后面不能有数字出现
        Pattern p = Pattern
                .compile("(?<![a-zA-Z0-9])([a-zA-Z0-9]{" + YZMLENGTH + "})(?![a-zA-Z0-9])");
        Matcher m = p.matcher(body);
        if (m.find()) {
            System.out.println(m.group());
            return m.group(0);
        }
        return null;
    }


}
