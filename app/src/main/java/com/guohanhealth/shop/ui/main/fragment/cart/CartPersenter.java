package com.guohanhealth.shop.ui.main.fragment.cart;

import com.guohanhealth.shop.base.BasePresenter;
import com.guohanhealth.shop.http.ConsumerError;

public class CartPersenter extends BasePresenter<CartView, CartModel> {
    public void getCartList(String key) {
        mRxManager.add(mModel.getCartList(key).subscribe(info -> {
            mView.getData(info);
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));
    }
}
