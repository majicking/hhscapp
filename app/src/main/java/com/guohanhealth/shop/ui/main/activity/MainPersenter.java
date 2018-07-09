package com.guohanhealth.shop.ui.main.activity;

import com.guohanhealth.shop.base.BasePresenter;
import com.guohanhealth.shop.http.ConsumerError;


public class MainPersenter extends BasePresenter<MainView, MainModel> {

    public void getCardNumber(String key) {
        mRxManager.add(mModel.getCardNumber(key).subscribe(info -> {
            mView.setCartNumber(info.cart_count);
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));

    }

}
