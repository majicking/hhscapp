package com.guohanhealth.shop.ui.goods.goodsdetailed.activity;

import android.app.Activity;

import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BasePresenter;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.ApiService;
import com.guohanhealth.shop.ui.goods.GoodsModel;
import com.guohanhealth.shop.utils.JSONParser;
import com.guohanhealth.shop.utils.Logutils;
import com.guohanhealth.shop.utils.Utils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class GoodsDetailsPersenter extends BasePresenter<GoodsDetailsView, GoodsModel> {

    public void getCardNumber(String key) {
        mRxManager.add(mModel.getCardNumber(key).subscribe(info -> {
            mView.getCardNumber(info.cart_count);
        }, throwable -> mView.faild(throwable.getMessage())));
    }


    public void getGoodsDetails(Activity activity, String goods_id, String key){
        Api.get(ApiService.GOODS_DETAIL + "&goods_id=" + goods_id + "&key=" + key, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logutils.i(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                Logutils.i(data);
                activity.runOnUiThread(() -> {
                    if (JSONParser.getStringFromJsonString("code", data).equals("200")) {
                        mView.getGoodsDetails(Utils.getString(JSONParser.getStringFromJsonString("datas", data)));
                    }
                });


            }
        });
    }


    public void buyStep1(Activity activity, String key, String cart_id) {
        Api.post(ApiService.BUY_STEP1, new FormBody.Builder()
                .add("key", key)
                .add("cart_id", cart_id)
                .build(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(() -> {
                    mView.faild(e.getMessage());
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                Logutils.i(data);
                activity.runOnUiThread(() -> {
                    if (JSONParser.getStringFromJsonString("code", data).equals("200")) {
                        mView.buyStep1Data(Utils.getString(JSONParser.getStringFromJsonString("datas", data)));
                    }else if (JSONParser.getStringFromJsonString("code", data).equals("400")) {
                        mView.faild(JSONParser.getStringFromJsonString(Constants.ERROR,Utils.getString(JSONParser.getStringFromJsonString("datas", data))));
                    }
                });

            }
        });
    }

    public void buyStep1V(Activity activity, String key, String goods_id, String goods_number) {
        Api.post(ApiService.VBUY_STEP1, new FormBody.Builder()
                .add("key", key)
                .add("goods_id", goods_id)
                .add("goods_number", goods_number + "")
                .build(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(() -> {
                    mView.faild(e.getMessage());
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                Logutils.i(data);
                activity.runOnUiThread(() -> {
                    if (JSONParser.getStringFromJsonString("code", data).equals("200")) {
                        mView.buyStep1VData(Utils.getString(JSONParser.getStringFromJsonString("datas", data)));
                    } else if (JSONParser.getStringFromJsonString("code", data).equals("400")) {
                        mView.faild(JSONParser.getStringFromJsonString(Constants.ERROR,Utils.getString(JSONParser.getStringFromJsonString("datas", data))));
                    }
                });
            }
        });
    }


}
