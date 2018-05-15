package com.majick.hhscapp.ui.main.activity;

import com.majick.hhscapp.base.BasePresenter;

public class MainPersenter extends BasePresenter<MainView,MainModel> {

        public void updataCartNumber(){
            mModel.getCartNumber();

        }

}
