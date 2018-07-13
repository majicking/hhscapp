package com.guohanhealth.shop.ui.order;

import com.guohanhealth.shop.base.BasePresenter;
import com.guohanhealth.shop.http.ConsumerError;
import com.guohanhealth.shop.utils.Logutils;

public class OrderPersenter extends BasePresenter<OrderView, OrderModel> {


    public void getOrderData(boolean selecttype, String key, int curpage, String state_type, String order_key) {
        mRxManager.add(mModel.getOrderData(selecttype, key, curpage, state_type, order_key).subscribe(info -> {
            mView.getData(info);
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));
    }

    public void orderOperation(String url, String key, String order_id) {
        mRxManager.add(mModel.orderOperation(url, key, order_id).subscribe(info -> {
            if (info.datas.equals("1")) {
                mView.orderOperation("操作成功");
            }
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));
    }

    public void getPaymentList(String key) {
        mRxManager.add(mModel.getPayList(key).subscribe(info -> {
            mView.getPayWayList(info);
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));
    }

}
