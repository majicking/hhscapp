package com.guohanhealth.shop.ui.main.fragment.mine.property.health;

import com.guohanhealth.shop.base.BasePresenter;
import com.guohanhealth.shop.bean.HealthInfo;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.ApiService;
import com.guohanhealth.shop.http.ConsumerError;
import com.guohanhealth.shop.http.HttpErrorCode;
import com.guohanhealth.shop.ui.main.fragment.mine.property.PredepositModel;
import com.guohanhealth.shop.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HealthPersenter extends BasePresenter<HealthView, PredepositModel> {
    public void getPointList(String key, String curpage) {
        Api.get(ApiService.HEALTHBEANLOG + "&key=" + key + "&curpage=" + curpage + "&pages=10", new Callback() {
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

                            if (!Boolean.parseBoolean(Utils.getDatasString(json))) {
                                mView.getHealthList(new HealthInfo());
                            } else {
                                mView.getHealthList(Utils.getObject(Utils.getDatasString(json), HealthInfo.class));
                            }
                        });
                    } else if (Utils.getCode(json) == HttpErrorCode.ERROR_400) {
                        mActivity.runOnUiThread(() -> {
                            mView.faild(Utils.getErrorString(json));
                        });
                    }
                } catch (Exception e) {
                    mActivity.runOnUiThread(() -> {
                        mView.faild(Utils.getErrorString(e));
                    });
                }
            }
        });
//        mRxManager.add(mModel.getHealthList(key, curpage).subscribe(info -> {
//            mView.getHealthList(info);
//        }, new ConsumerError<Throwable>() {
//            @Override
//            public void onError(int errorCode, String message) {
//                mView.faild(message);
//            }
//        }));
    }

    public void getHealthNum(String key) {
        mRxManager.add(mModel.healthNum(key).subscribe(info -> {
            mView.getHealthNum(info);
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));
    }
}
