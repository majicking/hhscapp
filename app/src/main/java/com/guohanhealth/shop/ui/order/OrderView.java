package com.guohanhealth.shop.ui.order;

import com.guohanhealth.shop.base.BaseView;
import com.guohanhealth.shop.bean.LogisticsInfo;
import com.guohanhealth.shop.bean.OrderInfo;
import com.guohanhealth.shop.bean.PayWayInfo;
import com.guohanhealth.shop.http.Result;

public interface OrderView extends BaseView {
    void getData(Result info);

    void orderOperation(String msg);

    void getPayWayList(PayWayInfo info);

    void lookOrderInfo(String info);

    void geExpressInfo(LogisticsInfo data);
}
