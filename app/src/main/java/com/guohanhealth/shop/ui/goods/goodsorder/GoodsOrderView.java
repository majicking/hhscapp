package com.guohanhealth.shop.ui.goods.goodsorder;

import com.guohanhealth.shop.base.BaseView;
import com.guohanhealth.shop.bean.PayWayInfo;
import com.guohanhealth.shop.bean.Step2Info;
import com.guohanhealth.shop.bean.UpDataAddressInfo;

public interface GoodsOrderView extends BaseView {
    void updataAddress(UpDataAddressInfo info);

    void getPayInfo(Step2Info info);

    void getPaymentList(PayWayInfo info);
}
