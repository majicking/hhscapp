package com.majick.guohanhealth.ui.goods.goodslist;

import com.majick.guohanhealth.base.BasePresenter;

public class GoodsListPersenter extends BasePresenter<GoodsListView, GoodsListModel> {

    public void getGoodsList(String curpage, String page, String keyword, String key,
                             String order, String price_from, String price_to, String area_id,
                             String gift, String groupbuy, String xianshi, String virtual,
                             String own_shop, String ci) {
        mView.showLoadingDialog("");
        mRxManager.add(mModel.getGoodsList(curpage, page, keyword, key, order, price_from, price_to, area_id,
                gift, groupbuy, xianshi, virtual, own_shop, ci).subscribe(info -> {
            mView.hideLoadingDialog();
            mView.getGoodsListData(info.goods_list);
        }, th -> {
            mView.hideLoadingDialog();
            mView.faild(th.getMessage());
        }));
    }
}
