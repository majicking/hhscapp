package com.guohanhealth.shop.ui.main.fragment.mine.property;

import com.alipay.android.phone.mrpc.core.t;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.base.BasePresenter;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.ApiService;
import com.guohanhealth.shop.http.ConsumerError;
import com.guohanhealth.shop.http.HttpErrorCode;
import com.guohanhealth.shop.utils.Logutils;
import com.guohanhealth.shop.utils.Utils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PredepositPersenter extends BasePresenter<PredepositView, PredepositModel> {
    public void getMyAssect(String key) {
        mRxManager.add(mModel.getMyAsset(key).subscribe(info -> {
            mView.getMyAssect(info);
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));
    }

    public void recharge(String key, String number) {
        mRxManager.add(mModel.recharge(key, number).flatMap(data -> {
            App.getApp().setPay_sn(data.pay_sn);
            return mModel.getPayList(key);
        }).subscribe(info -> {
            mView.getPaymentList(info);
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));
    }

    public void rechargeOrder(String key, String paysn) {
        mRxManager.add(mModel.rechargeOrder(key, paysn).subscribe(info -> {

        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));
    }


    public void predeposit(String key, String op, String curpage, Class c) {
//        mRxManager.add(mModel.predeposit(key, op, curpage).subscribe(info -> {
//            mView.getData(info);
//        }, new ConsumerError<Throwable>() {
//            @Override
//            public void onError(int errorCode, String message) {
//                mView.faild(message);
//            }
//        }));
        Api.get(ApiService.PREDEPOSIT + "&op=" + op + "&curpage=" + curpage + "&page=10" + "&key=" + key, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mActivity.runOnUiThread(() -> {
                    mView.faild(Utils.getErrorString(e));
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                if (Utils.getCode(json) == HttpErrorCode.HTTP_NO_ERROR) {
                    mActivity.runOnUiThread(
                            () -> mView.getData(Utils.getObject(Utils.getDatasString(json), c))
                    );
                } else if (Utils.getCode(json) == HttpErrorCode.ERROR_400) {
                    mActivity.runOnUiThread(() -> mView.faild(Utils.getErrorString(json)));
                }
            }
        });
    }

}
