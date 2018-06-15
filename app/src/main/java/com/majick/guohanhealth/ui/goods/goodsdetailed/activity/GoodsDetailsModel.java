package com.majick.guohanhealth.ui.goods.goodsdetailed.activity;

import com.majick.guohanhealth.base.BaseModel;
import com.majick.guohanhealth.bean.GoodsInfo;
import com.majick.guohanhealth.http.Api;
import com.majick.guohanhealth.http.RxHelper;

import io.reactivex.Observable;

public class GoodsDetailsModel extends BaseModel {
    public Observable<GoodsInfo> getGoodsDetails(String goods_id, String key) {
        return Api.getDefault().getGoodsDetails(goods_id, key).compose(RxHelper.handleResult());
    }
}
