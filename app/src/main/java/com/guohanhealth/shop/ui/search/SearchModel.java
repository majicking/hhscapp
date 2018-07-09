package com.guohanhealth.shop.ui.search;


import com.guohanhealth.shop.base.BaseModel;
import com.guohanhealth.shop.bean.SearchInfo;
import com.guohanhealth.shop.bean.SearchWordsInfo;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.RxHelper;

import io.reactivex.Observable;

public class SearchModel extends BaseModel {
    public Observable<SearchInfo> getSearchDataList() {
        return Api.getDefault().getSearchDataList().compose(RxHelper.handleResult());
    }

}
