package com.majick.guohanhealth.ui.main.fragment.cart;

import com.majick.guohanhealth.base.BaseView;

public interface CartView extends BaseView {
    void getData();
    void fail(String msg);
}
