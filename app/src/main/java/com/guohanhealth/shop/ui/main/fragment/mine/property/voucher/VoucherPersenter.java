package com.guohanhealth.shop.ui.main.fragment.mine.property.voucher;

import com.guohanhealth.shop.base.BasePresenter;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.ApiService;
import com.guohanhealth.shop.http.ConsumerError;
import com.guohanhealth.shop.http.HttpErrorCode;
import com.guohanhealth.shop.ui.main.fragment.mine.property.PredepositModel;
import com.guohanhealth.shop.utils.Utils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class VoucherPersenter extends BasePresenter<VoucherView, PredepositModel> {

    public void voucherList(String key, String curpage) {
        mRxManager.add(mModel.voucherList(key, curpage).subscribe(info -> {
            mView.getData(info);
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));
    }

    public void getImgCode() {
        mRxManager.add(mModel.getImgCode().subscribe(imgCodeKey -> {
            mView.getData(imgCodeKey.codekey);
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));
    }



    public void getVoutcher(String key, String pwd_code, String captcha, String codekey) {
        Api.post(ApiService.VOUCHER_PWEX, new FormBody.Builder()
                        .add("key", key)
                        .add("pwd_code", pwd_code)
                        .add("captcha", captcha)
                        .add("codekey", codekey)
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
                        try {
                            if (Utils.getCode(json) == HttpErrorCode.HTTP_NO_ERROR) {
                                mActivity.runOnUiThread(() -> {
                                    mView.getResult(Utils.getDatasString(json));
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
                }
        );
    }

}
