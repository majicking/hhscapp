package com.guohanhealth.shop.ui.main.fragment.mine.returngoods;

import com.guohanhealth.shop.base.BaseModel;
import com.guohanhealth.shop.bean.RefundlistInfo;
import com.guohanhealth.shop.bean.ReturnlistInfo;
import com.guohanhealth.shop.bean.StoreCollectInfo;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.RxHelper;

import io.reactivex.Observable;

public class ReturnModel extends BaseModel {

    public Observable<ReturnlistInfo> geReturnList(String key, String curpage) {
        return Api.getDefault().geReturnList(key, curpage, "10").compose(RxHelper.handleResult());

    }
    public Observable<RefundlistInfo> geRefundList(String key, String curpage) {
        return Api.getDefault().geRefundList(key, curpage, "10").compose(RxHelper.handleResult());

    }
}
