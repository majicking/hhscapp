package com.majick.guohanhealth.ui.search;

import com.majick.guohanhealth.base.BasePresenter;
import com.majick.guohanhealth.http.ConsumerError;

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
