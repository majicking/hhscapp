package com.guohanhealth.shop.ui.main.fragment.mine.history;

import com.guohanhealth.shop.base.BaseModel;
import com.guohanhealth.shop.bean.CartNumberInfo;
import com.guohanhealth.shop.bean.HistoryInfo;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.RxHelper;

import io.reactivex.Observable;

public class HistoryModel extends BaseModel {
    public Observable<HistoryInfo> historyList(String key, String curpage) {
        return Api.getDefault().historyList(key, curpage, "10").compose(RxHelper.handleResult());
    }
}
