package com.majick.guohanhealth.ui.goods.goodsorder;

import com.majick.guohanhealth.base.BasePresenter;
import com.majick.guohanhealth.http.ConsumerError;
import com.majick.guohanhealth.ui.goods.GoodsModel;
import com.majick.guohanhealth.utils.Logutils;

public class GoodsOrderPercenter extends BasePresenter<GoodsOrderView, GoodsModel> {
    public void updataAdress(String key, String city_id, String area_id, String freight_hash) {
        mRxManager.add(mModel.changedAdress(key, city_id, area_id, freight_hash).subscribe(info -> {
            Logutils.i(info);
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));
    }
}
