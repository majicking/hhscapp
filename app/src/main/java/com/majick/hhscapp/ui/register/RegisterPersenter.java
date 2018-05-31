package com.majick.hhscapp.ui.register;

import com.majick.hhscapp.base.BasePresenter;
import com.majick.hhscapp.model.RegisterModel;
import com.majick.hhscapp.utils.Logutils;

public class RegisterPersenter extends BasePresenter<RegisterView, RegisterModel> {
    public void getImgCode() {
        mRxManager.add(mModel.getImgCode().subscribe(imgCodeKey -> {
            Logutils.i(imgCodeKey);
        }));
    }

    public void getSMSCode(String mobile, String imgcode) {

    }

    public void register() {

    }

    public void registerMobile() {

    }
}
