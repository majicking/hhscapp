package com.guohanhealth.shop.ui.main.fragment.mine.history;

import com.guohanhealth.shop.base.BasePresenter;
import com.guohanhealth.shop.base.BaseView;
import com.guohanhealth.shop.http.ConsumerError;

public class HistoryPersenter extends BasePresenter<BaseView, HistoryModel> {
    public void getHistoryList(String key, String curpage) {
        mRxManager.add(mModel.historyList(key, curpage).subscribe(data -> {
            mView.getData(data);
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));
    }
}
