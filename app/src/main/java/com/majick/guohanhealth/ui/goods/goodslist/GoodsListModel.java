package com.majick.guohanhealth.ui.goods.goodslist;

import com.majick.guohanhealth.base.BaseModel;
import com.majick.guohanhealth.bean.GoodsListInfo;
import com.majick.guohanhealth.http.Api;
import com.majick.guohanhealth.http.RxHelper;

import io.reactivex.Observable;


public class GoodsListModel extends BaseModel {
    public Observable<GoodsListInfo> getGoodsList(String curpage, String page, String keyword, String key,
                                                  String order, String price_from, String price_to, String area_id,
                                                  String gift, String groupbuy, String xianshi, String virtual,
                                                  String own_shop, String ci) {
        return Api.getDefault().getGoodsList(curpage, page, keyword, key, order, price_from, price_to, area_id,
                gift, groupbuy, xianshi, virtual, own_shop, ci).compose(RxHelper.handleResult());
    }
}
