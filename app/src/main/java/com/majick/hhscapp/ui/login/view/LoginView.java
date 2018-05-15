package com.majick.hhscapp.ui.login.view;

import com.majick.hhscapp.base.BaseView;

public interface LoginView  extends BaseView{

    void sucess(String message);
    void faild(String message);
    void start();
}
