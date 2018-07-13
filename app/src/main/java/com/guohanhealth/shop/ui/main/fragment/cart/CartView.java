package com.guohanhealth.shop.ui.main.fragment.cart;

import com.guohanhealth.shop.base.BaseView;
import com.guohanhealth.shop.bean.CartInfo;

public interface CartView extends BaseView {
    void getData(CartInfo info);
    void buyStep1Data(String data);
}
