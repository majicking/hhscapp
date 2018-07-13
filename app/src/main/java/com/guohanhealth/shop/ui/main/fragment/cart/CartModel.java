package com.guohanhealth.shop.ui.main.fragment.cart;

import com.guohanhealth.shop.base.BaseModel;
import com.guohanhealth.shop.bean.Base;
import com.guohanhealth.shop.bean.BaseInfo;
import com.guohanhealth.shop.bean.CartInfo;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.Result;
import com.guohanhealth.shop.http.RxHelper;

import io.reactivex.Observable;

public class CartModel extends BaseModel {
    public Observable<CartInfo> getCartList(String key) {
        return Api.getDefault().getCartList(key).compose(RxHelper.handleResult());
    }

    public Observable<Object> delCart(String key, String cart_id) {
        return Api.getDefault().delCart(key, cart_id).compose(RxHelper.handleResult());
    }

    public Observable<BaseInfo> upCartNumber(String key, String cart_id, String quantity) {
        return Api.getDefault().upCartNumber(key, cart_id, quantity).compose(RxHelper.handleResult());
    }
}
