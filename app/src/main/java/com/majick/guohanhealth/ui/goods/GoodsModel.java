package com.majick.guohanhealth.ui.goods;

import com.majick.guohanhealth.base.BaseModel;
import com.majick.guohanhealth.bean.BaseInfo;
import com.majick.guohanhealth.bean.CardNumberInfo;
import com.majick.guohanhealth.bean.EvalInfo;
import com.majick.guohanhealth.bean.GoodsDetailedInfo;
import com.majick.guohanhealth.bean.GoodsListInfo;
import com.majick.guohanhealth.http.Api;
import com.majick.guohanhealth.http.Result;
import com.majick.guohanhealth.http.RxHelper;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GoodsModel extends BaseModel {
    public Observable<CardNumberInfo> getCardNumber(String key) {
        return Api.getDefault().getCardNumber(key).compose(RxHelper.handleResult());
    }

    public Observable<GoodsListInfo> getGoodsList(String curpage, String page, String keyword, String key,
                                                  String order, String price_from, String price_to, String area_id,
                                                  String gift, String groupbuy, String xianshi, String virtual,
                                                  String own_shop, String ci) {
        return Api.getDefault().getGoodsList(curpage, page, keyword, key, order, price_from, price_to, area_id,
                gift, groupbuy, xianshi, virtual, own_shop, ci).compose(RxHelper.handleResult());
    }

    public Observable<Result<GoodsDetailedInfo>> getGoodsDetails(String goods_id, String key) {
        return Api.getDefault().getGoodsDetails(goods_id, key).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Result<BaseInfo>> buyStep1(String key, String cart_id) {
        return Api.getDefault().buyStep1(key, cart_id, "0").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Result<BaseInfo>> buyStep1V(String key, String good_id, String goods_number) {
        return Api.getDefault().buyStep1V(key, good_id, goods_number).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<EvalInfo> goodsEvaluate(String goods_id, String curpage, String type, String page) {
        return Api.getDefault().goodsEvaluate(goods_id, curpage, type, page).compose(RxHelper.handleResult());
    }

}
