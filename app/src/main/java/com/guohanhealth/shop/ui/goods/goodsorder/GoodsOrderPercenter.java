package com.guohanhealth.shop.ui.goods.goodsorder;

import com.guohanhealth.shop.base.BasePresenter;
import com.guohanhealth.shop.http.ConsumerError;
import com.guohanhealth.shop.ui.goods.GoodsModel;
import com.guohanhealth.shop.utils.Logutils;

import java.util.zip.Inflater;

public class GoodsOrderPercenter extends BasePresenter<GoodsOrderView, GoodsModel> {
    public void updataAdress(String key, String city_id, String area_id, String freight_hash) {
        mRxManager.add(mModel.changedAdress(key, city_id, area_id, freight_hash).subscribe(info -> {
            mView.updataAddress(info);
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));
    }

    public void buyStep2(String key, String cart_id, String ifcart, String address_id, String vat_hash, String offpay_hash, String offpay_hash_batch, String pay_name, String invoice_id, String pd_pay, String rcb_pay, String healthbean_pay, String password, String type, String quantity, String buyer_phone) {
        mRxManager.add(mModel.buyStep2(key, cart_id, ifcart, address_id, vat_hash, offpay_hash, offpay_hash_batch, pay_name, invoice_id, pd_pay, rcb_pay, healthbean_pay, password, type, quantity, buyer_phone)
                .subscribe(info -> {
                    mView.getPayInfo(info);
                    Logutils.i(info);
                }, new ConsumerError<Throwable>() {
                    @Override
                    public void onError(int errorCode, String message) {
                        mView.faild(message);
                    }
                }));
    }

    public void getPaymentList(String key) {
        mRxManager.add(mModel.getPayList(key).subscribe(info -> {
            mView.getPaymentList(info);
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));
    }
}
