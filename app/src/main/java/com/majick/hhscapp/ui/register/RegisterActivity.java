package com.majick.hhscapp.ui.register;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.majick.hhscapp.R;
import com.majick.hhscapp.base.BaseActivity;
import com.majick.hhscapp.model.RegisterModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.carbs.android.segmentcontrolview.library.SegmentControlView;

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
    @BindView(R.id.register_getcheckcode)
    Button registerGetcheckcode;
    @BindView(R.id.registerselect2)
    LinearLayout registerselect2;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initToolBarNav(commonToolbar, commonToolbarTitleTv, "注册");
        mPresenter.getImgCode();
        segmentcontrolview.setOnSegmentChangedListener(index -> {
            changeRegisterType(index);
        });

        registerCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            registerCheckbox.setSelected(isChecked);
        });
        registerCheckbox1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            registerCheckbox1.setSelected(isChecked);
        });

    }

    //切换注册方式
    private void changeRegisterType(int index) {
        if (index == 0) {
            registerselect1.setVisibility(View.VISIBLE);
            registerselect2.setVisibility(View.GONE);
        } else {
            registerselect1.setVisibility(View.GONE);
            registerselect2.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void start() {


    }

    @Override
    public void success(String msg) {

    }

    @Override
    public void fail(String msg) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
