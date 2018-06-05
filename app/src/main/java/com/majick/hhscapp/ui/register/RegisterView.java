package com.majick.hhscapp.ui.register;

import com.majick.hhscapp.base.BaseView;

public interface RegisterView extends BaseView {
    void setKeyCode(String code);

    void success(String msg);

    void fail(String msg);

    void smsfail(String msg);

    void smsSuccess(String time);

    void loginSuccess();

    void loginFail(String msg);
}
