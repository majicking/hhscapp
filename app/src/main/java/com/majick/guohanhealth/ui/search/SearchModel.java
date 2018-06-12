package com.majick.guohanhealth.ui.search;


import com.majick.guohanhealth.base.BaseModel;
import com.majick.guohanhealth.bean.SearchInfo;
import com.majick.guohanhealth.bean.SearchWordsInfo;
import com.majick.guohanhealth.http.Api;
import com.majick.guohanhealth.http.RxHelper;

import io.reactivex.Observable;

public class SearchModel extends BaseModel {
    public Observable<SearchInfo> getSearchDataList() {
        return Api.getDefault().getSearchDataList().compose(RxHelper.handleResult());
    }

}
