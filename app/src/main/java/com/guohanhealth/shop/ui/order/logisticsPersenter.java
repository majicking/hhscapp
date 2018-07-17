package com.guohanhealth.shop.ui.order;

import com.guohanhealth.shop.base.BasePresenter;
import com.guohanhealth.shop.http.ConsumerError;
import com.guohanhealth.shop.utils.Logutils;

public class logisticsPersenter extends BasePresenter<LogisticsView, OrderModel> {
    public void searchLogistics(String key, String order_id) {
        mRxManager.add(mModel.searchLogistics(key, order_id).subscribe(info -> {
            Logutils.i(info);
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));
    }
}
