package com.majick.guohanhealth.ui.goods.goodsdetailed.fragment.goods;

import com.majick.guohanhealth.base.BasePresenter;

public class GoodsPersenter extends BasePresenter<GoodsView, GoodsModel> {
    public void getGoodsDetails(String goods_id, String key) {
        mRxManager.add(mModel.getGoodsDetails(goods_id, key).subscribe(goodsInfo -> {
            mView.getGoodsDetails(goodsInfo);
        }, throwable -> mView.faild(throwable.getMessage())));
    }
}
