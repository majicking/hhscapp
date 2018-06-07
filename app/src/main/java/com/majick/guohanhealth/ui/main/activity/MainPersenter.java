package com.majick.guohanhealth.ui.main.activity;

import com.majick.guohanhealth.base.BasePresenter;

public class MainPersenter extends BasePresenter<MainView,MainModel> {

        public void updataCartNumber(){
            mModel.getCartNumber();

        }

}
