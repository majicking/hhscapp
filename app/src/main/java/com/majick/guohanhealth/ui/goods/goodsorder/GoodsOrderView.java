package com.majick.guohanhealth.ui.goods.goodsorder;

import com.majick.guohanhealth.base.BaseView;
import com.majick.guohanhealth.bean.PayWayInfo;
import com.majick.guohanhealth.bean.Step2Info;
import com.majick.guohanhealth.bean.UpDataAddressInfo;

public interface GoodsOrderView extends BaseView {
    void updataAddress(UpDataAddressInfo info);

    void getPayInfo(Step2Info info);

    void getPaymentList(PayWayInfo info);
}
