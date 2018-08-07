package com.guohanhealth.shop.ui.main.fragment.mine.collect;

import com.guohanhealth.shop.base.BasePresenter;
import com.guohanhealth.shop.http.ConsumerError;

public class CollectPersenter extends BasePresenter<CollectView, CollectModel> {

    public void getGoodsCollectList(String key, String curpage) {
        mRxManager.add(mModel.getGoodsCollectList(key, curpage).subscribe(data -> {
            mView.getData(data);
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));
    }

    public void getStoreCollectList(String key, String curpage) {
        mRxManager.add(mModel.getStoreCollectList(key, curpage).subscribe(data -> {
            mView.getData(data);
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));
    }

}
