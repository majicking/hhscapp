package com.guohanhealth.shop.ui.main.fragment.mine.returngoods;

import com.guohanhealth.shop.base.BasePresenter;
import com.guohanhealth.shop.http.ConsumerError;

public class ReturnPersenter extends BasePresenter<ReturnView, ReturnModel> {
    public void geReturnList(String key, String curpage) {
        mRxManager.add(mModel.geReturnList(key, curpage).subscribe(data -> {
            mView.getData(data);
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));
    }

    public void geRefundList(String key, String curpage) {
        mRxManager.add(mModel.geRefundList(key, curpage).subscribe(data -> {
            mView.getData(data);
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));
    }
}
