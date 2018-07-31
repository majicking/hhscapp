package com.guohanhealth.shop.ui.main.fragment.mine.property.point;

import com.guohanhealth.shop.base.BasePresenter;
import com.guohanhealth.shop.http.ConsumerError;
import com.guohanhealth.shop.ui.main.fragment.mine.property.PredepositModel;

public class PointPersenter extends BasePresenter<PointView, PredepositModel> {
    public void getPointList(String key, String curpage) {
        mRxManager.add(mModel.getPointList(key, curpage).subscribe(info -> {
            mView.getPointList(info);
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));
    }
    public void getPre(String key, String type) {
        mRxManager.add(mModel.getPre(key, type).subscribe(info -> {
            mView.getPreData(info);
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));
    }
}
