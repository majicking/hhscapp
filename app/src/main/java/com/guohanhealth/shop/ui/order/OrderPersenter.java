package com.guohanhealth.shop.ui.order;

import com.guohanhealth.shop.base.BasePresenter;
import com.guohanhealth.shop.bean.OrderInfo;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.ApiService;
import com.guohanhealth.shop.http.ConsumerError;
import com.guohanhealth.shop.http.HttpErrorCode;
import com.guohanhealth.shop.utils.Logutils;
import com.guohanhealth.shop.utils.Utils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;
import okhttp3.internal.Util;

public class OrderPersenter extends BasePresenter<OrderView, OrderModel> {

    boolean selecttype;
    int curpage;
    String state_type;
    String order_key;

    public void getOrderData(boolean selecttype, String key, int curpage, String state_type, String order_key) {
        this.selecttype = selecttype;
        this.curpage = curpage;
        this.state_type = state_type;
        this.order_key = order_key;
        mRxManager.add(mModel.getOrderData(selecttype, key, curpage, state_type, order_key).subscribe(info -> {
            mView.getData(info);
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));
    }

    public void orderOperation(String url, String key, String order_id, boolean selecttype, int curpage, String state_type, String order_key) {
        Api.post(ApiService.ORDER_OPERATION + "&op=" + url, new FormBody.Builder()
                .add("key", key)
                .add("order_id", order_id)
                .build(), new Callback() {
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
                    if (Utils.getDatasString(json).equals("1")) {
                        mActivity.runOnUiThread(() -> {
                            mView.orderOperation("操作成功");
                            getOrderData(selecttype, key, curpage, state_type, order_key);
                        });

                    }
                } else if (Utils.getCode(json) == HttpErrorCode.ERROR_400) {
                    mActivity.runOnUiThread(() -> {
                        mView.faild(Utils.getErrorString(json));
                    });
                }
            }
        });
    }


    public void getPaymentList(String key) {
        mRxManager.add(mModel.getPayList(key).subscribe(info -> {
            mView.getPayWayList(info);
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));
    }

    public void orderInfo(String url, String key, String order_id) {
        Api.get(ApiService.ORDER_OPERATION + "&op=" + url + "&key=" + key + "&order_id=" + order_id, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mActivity.runOnUiThread(() -> {
                    mView.faild(Utils.getErrorString(e));
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String json = response.body().string();
                    if (Utils.getCode(json) == HttpErrorCode.HTTP_NO_ERROR) {
                        mActivity.runOnUiThread(() -> {
                            mView.lookOrderInfo(Utils.getDatasString(json));
                        });

                    } else if (Utils.getCode(json) == HttpErrorCode.ERROR_400) {
                        mActivity.runOnUiThread(() -> {
                            mView.faild(Utils.getErrorString(json));
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mView.faild(Utils.getErrorString(e));
                }
            }
        });
    }

    public void searchLogistics(String key, String order_id) {
        mRxManager.add(mModel.searchLogistics(key, order_id).subscribe(info -> {
           mView.geExpressInfo(info);
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));
    }
}
