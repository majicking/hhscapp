package com.guohanhealth.shop.ui.goods;

import com.guohanhealth.shop.base.BaseModel;
import com.guohanhealth.shop.bean.BaseInfo;
import com.guohanhealth.shop.bean.EvalInfo;
import com.guohanhealth.shop.bean.GoodsDetailedInfo;
import com.guohanhealth.shop.bean.GoodsListInfo;
import com.guohanhealth.shop.bean.UpDataAddressInfo;
import com.guohanhealth.shop.bean.Step2Info;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.Result;
import com.guohanhealth.shop.http.RxHelper;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GoodsModel extends BaseModel {


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

    public Observable<UpDataAddressInfo> changedAdress(String key, String city_id, String area_id, String freight_hash) {
        return Api.getDefault().changedAdress(key, city_id, area_id, freight_hash).compose(RxHelper.handleResult());
    }

    public Observable<Step2Info> buyStep2(String key,
                                          String cart_id,
                                          String ifcart,
                                          String address_id,
                                          String vat_hash,
                                          String offpay_hash,
                                          String offpay_hash_batch,
                                          String pay_name,
                                          String invoice_id,
                                          String pd_pay,
                                          String rcb_pay,
                                          String healthbean_pay,
                                          String password, String type, String quantity, String buyer_phone) {
        if (type.equals("1")) {
            return Api.getDefault().buyStep2V(key, cart_id, quantity, buyer_phone, pd_pay, rcb_pay,
                    healthbean_pay, password, "android").compose(RxHelper.handleResult());
        }
        return Api.getDefault().buyStep2(key, cart_id, ifcart, address_id, vat_hash,
                offpay_hash, offpay_hash_batch,
                pay_name, invoice_id, pd_pay, rcb_pay,
                healthbean_pay, password, "android").compose(RxHelper.handleResult());

    }

    public Observable<Object> addCart(String key, String goods_id, String quantity) {
        return Api.getDefault().addCart(key, goods_id, quantity).compose(RxHelper.handleResult());
    }

}
