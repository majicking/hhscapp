package com.guohanhealth.shop.ui.main.fragment.mine.collect;

import com.guohanhealth.shop.base.BaseModel;
import com.guohanhealth.shop.bean.CartNumberInfo;
import com.guohanhealth.shop.bean.GoodsCollectInfo;
import com.guohanhealth.shop.bean.StoreCollectInfo;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.RxHelper;

import io.reactivex.Observable;

public class CollectModel extends BaseModel {
    public Observable<GoodsCollectInfo> getGoodsCollectList(String key, String curpage) {
        return Api.getDefault().goodsCollectList(key, curpage, "10").compose(RxHelper.handleResult());
    }

    public Observable<StoreCollectInfo> getStoreCollectList(String key, String curpage) {
        return Api.getDefault().storeCollectList(key, curpage, "10").compose(RxHelper.handleResult());

    }
}
