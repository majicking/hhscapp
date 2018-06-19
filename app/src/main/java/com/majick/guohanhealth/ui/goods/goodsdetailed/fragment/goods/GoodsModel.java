package com.majick.guohanhealth.ui.goods.goodsdetailed.fragment.goods;

import com.majick.guohanhealth.base.BaseModel;
import com.majick.guohanhealth.bean.GoodsDetailedInfo;
import com.majick.guohanhealth.http.Api;
import com.majick.guohanhealth.http.RxHelper;

import io.reactivex.Observable;

public class GoodsModel extends BaseModel {
    public Observable<GoodsDetailedInfo> getGoodsDetails(String goods_id, String key) {
        return Api.getDefault().getGoodsDetails(goods_id, key).compose(RxHelper.handleResult());
    }
}
