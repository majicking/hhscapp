package com.majick.guohanhealth.ui.goods.goodsdetailed.activity;

import com.majick.guohanhealth.base.BaseView;

public interface GoodsDetailsView extends BaseView {
    void getCardNumber(String number);
    void getGoodsDetails(String data);
    void buyStep1Data(String data);

    void buyStep1VData(String data);
}
