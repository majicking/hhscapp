package com.guohanhealth.shop.ui.goods.goodslist;

import com.guohanhealth.shop.base.BasePresenter;
import com.guohanhealth.shop.http.ConsumerError;
import com.guohanhealth.shop.ui.goods.GoodsModel;

public class GoodsListPersenter extends BasePresenter<GoodsListView, GoodsModel> {

    public void getGoodsList(String curpage, String page, String keyword, String key,
                             String order, String price_from, String price_to, String area_id,
                             String gift, String groupbuy, String xianshi, String virtual,
                             String own_shop, String ci, String gc_id,String b_id) {
        mRxManager.add(mModel.getGoodsList(curpage, page, keyword, key, order, price_from, price_to, area_id,
                gift, groupbuy, xianshi, virtual, own_shop, ci,gc_id,b_id).subscribe(info -> {
            mView.getGoodsListData(info.goods_list);
        },new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));
    }
}
