package com.majick.guohanhealth.ui.search;

import com.majick.guohanhealth.base.BasePresenter;

public class SearchPercenter extends BasePresenter<SearchView, SearchModel> {
    public void getSearchDataList() {
        mRxManager.add(mModel.getSearchDataList().subscribe(searchInfo -> {
            mView.getSearchList(searchInfo);
        }, throwable -> mView.faild(throwable.getMessage())));
    }

}
