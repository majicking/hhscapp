package com.guohanhealth.shop.ui.order;

import com.guohanhealth.shop.base.BaseModel;
import com.guohanhealth.shop.bean.LogisticsInfo;
import com.guohanhealth.shop.bean.ObjectBeans;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.Result;
import com.guohanhealth.shop.http.RxHelper;

import io.reactivex.Observable;

public class OrderModel extends BaseModel {
    public Observable<Result> getOrderData(boolean selecttype, String key, int curpage, String state_type, String order_key) {
        if (selecttype) {
            return Api.getDefault().getOrderListV(key, state_type, curpage + "", order_key).compose(RxHelper.<Result>handleOnlyResult());
        } else {
            return Api.getDefault().getOrderList(key, state_type, curpage + "", order_key).compose(RxHelper.<Result>handleOnlyResult());
        }
    }

    public Observable<Result> orderOperation(String url, String key, String order_id) {
        return Api.getDefault().orderOperation(url, key, order_id).compose(RxHelper.handleOnlyResult());
    }

    public Observable<LogisticsInfo> searchLogistics(String key, String order_id) {
        return Api.getDefault().searchLogistics(key, order_id).compose(RxHelper.handleResult());
    }
}
