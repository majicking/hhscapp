package com.majick.hhscapp.ui.main.fragment.cart;

import com.majick.hhscapp.base.BaseView;

public interface CartView extends BaseView {
    void getData();
    void fail(String msg);
}
