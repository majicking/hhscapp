package com.guohanhealth.shop.ui.main.fragment.mine.property;

import com.guohanhealth.shop.base.BaseView;
import com.guohanhealth.shop.bean.MyAssectInfo;
import com.guohanhealth.shop.bean.PayWayInfo;
import com.guohanhealth.shop.bean.RechargeOrderInfo;
import com.guohanhealth.shop.http.Result;

public interface PredepositView extends BaseView {
    void getMyAssect(MyAssectInfo info);

    void getPaymentList(PayWayInfo info);

    void getData(Object result);
}
