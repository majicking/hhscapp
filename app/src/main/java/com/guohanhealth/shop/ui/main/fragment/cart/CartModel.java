package com.guohanhealth.shop.ui.main.fragment.cart;

import com.guohanhealth.shop.base.BaseModel;
import com.guohanhealth.shop.bean.CartInfo;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.RxHelper;

import io.reactivex.Observable;

public class CartModel extends BaseModel {
    public Observable<CartInfo> getCartList(String key) {
        return Api.getDefault().getCartList(key).compose(RxHelper.handleResult());
    }
}
