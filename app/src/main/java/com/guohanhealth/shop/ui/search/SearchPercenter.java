package com.guohanhealth.shop.ui.search;

import com.guohanhealth.shop.base.BasePresenter;
import com.guohanhealth.shop.http.ConsumerError;

public class SearchPercenter extends BasePresenter<SearchView, SearchModel> {
    public void getSearchDataList() {
        mRxManager.add(mModel.getSearchDataList().subscribe(searchInfo -> {
            mView.getSearchList(searchInfo);
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }


        }));
    }

}
