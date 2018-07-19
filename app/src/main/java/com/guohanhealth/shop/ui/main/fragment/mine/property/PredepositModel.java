package com.guohanhealth.shop.ui.main.fragment.mine.property;

import com.guohanhealth.shop.base.BaseModel;
import com.guohanhealth.shop.bean.BaseInfo;
import com.guohanhealth.shop.bean.MyAssectInfo;
import com.guohanhealth.shop.bean.PaySignInfo;
import com.guohanhealth.shop.bean.RechargeOrderInfo;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.Result;
import com.guohanhealth.shop.http.RxHelper;

import io.reactivex.Observable;

public class PredepositModel extends BaseModel {
    public Observable<MyAssectInfo> getMyAsset(String key) {
        return Api.getDefault().getMyAsset(key).compose(RxHelper.handleResult());
    }

    public Observable<PaySignInfo> recharge(String key, String number) {
        return Api.getDefault().recharge(key, number).compose(RxHelper.handleResult());
    }

    public Observable<RechargeOrderInfo> rechargeOrder(String key, String paysn) {
        return Api.getDefault().rechargeOrder(key, paysn).compose(RxHelper.handleResult());
    }

    public Observable predeposit(String key, String op, String curpage ) {
        return Api.getDefault().predeposit(key, op, curpage, "10").compose(RxHelper.handleOnlyResult());
    }
}
