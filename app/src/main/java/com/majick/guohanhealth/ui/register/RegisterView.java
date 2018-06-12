package com.majick.guohanhealth.ui.register;

import com.majick.guohanhealth.base.BaseView;

public interface RegisterView extends BaseView {
    void setKeyCode(String code);

    void success(String msg);

    void smsfail(String msg);

    void smsSuccess(String time);

    void loginSuccess();

    void loginFail(String msg);
}
