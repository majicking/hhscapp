package com.guohanhealth.shop.ui.main.fragment.cart;

import android.app.Activity;

import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BasePresenter;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.ApiService;
import com.guohanhealth.shop.http.ConsumerError;
import com.guohanhealth.shop.utils.JSONParser;
import com.guohanhealth.shop.utils.Logutils;
import com.guohanhealth.shop.utils.Utils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class CartPersenter extends BasePresenter<CartView, CartModel> {
    public void getCartList(String key) {
        mRxManager.add(mModel.getCartList(key).subscribe(info -> {
            mView.getData(info);
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));
    }

    public void delCart(String key, String cart_id) {
        mRxManager.add(mModel.delCart(key, cart_id).flatMap(info -> {
            return mModel.getCartList(key);
        }).subscribe(info1 -> {
            mView.getData(info1);
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));
    }

    public void upCartNumber(String key, String cart_id, String quantity) {
        mRxManager.add(mModel.upCartNumber(key, cart_id, quantity).flatMap(info -> {
            return mModel.getCartList(key);
        }).subscribe(info -> {
            mView.getData(info);
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));
    }

    public void buyStep1(Activity activity, String key, String cart_id, String ifcart) {
        Api.post(ApiService.BUY_STEP1, new FormBody.Builder()
                .add("key", key)
                .add("cart_id", cart_id)
                .add("ifcart", ifcart)
                .build(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(() -> {
                    mView.faild(Utils.getErrorString(e));
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                Logutils.i(data);
                activity.runOnUiThread(() -> {
                    if (JSONParser.getStringFromJsonString("code", data).equals("200")) {
                        mView.buyStep1Data(Utils.getString(JSONParser.getStringFromJsonString("datas", data)));
                    } else if (JSONParser.getStringFromJsonString("code", data).equals("400")) {
                        mView.faild(JSONParser.getStringFromJsonString(Constants.ERROR, Utils.getString(JSONParser.getStringFromJsonString("datas", data))));
                    }
                });

            }
        });
    }

}
