package com.guohanhealth.shop.ui.goods.goodsdetailed.activity;

import com.guohanhealth.shop.base.BaseView;

public interface GoodsDetailsView extends BaseView {
    void getCardNumber(String number);
    void getGoodsDetails(String data);
    void buyStep1Data(String data);

    void buyStep1VData(String data);
}
