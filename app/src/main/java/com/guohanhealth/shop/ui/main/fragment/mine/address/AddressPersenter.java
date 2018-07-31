package com.guohanhealth.shop.ui.main.fragment.mine.address;

import com.guohanhealth.shop.base.BasePresenter;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.ConsumerError;
import com.guohanhealth.shop.http.HttpErrorCode;
import com.guohanhealth.shop.utils.Utils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class AddressPersenter extends BasePresenter<AddressView, AddressModel> {

    public void getAddressList(String key) {
        mRxManager.add(mModel.getAddressList(key).subscribe(data -> {
            mView.getData(data);
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));
    }

    public void editAddressResult(String url, String key, String true_name, String area_id, String city_id,
                                  String area_info, String address, String mob_phone, String is_default, String address_id) {
        Api.post(url, new FormBody.Builder()
                .add("key", key)
                .add("true_name", true_name)
                .add("mob_phone", mob_phone)
                .add("area_id", area_id)
                .add("city_id", city_id)
                .add("area_info", area_info)
                .add("address", address)
                .add("is_default", is_default)
                .add("address_id", address_id)
                .build(), new Callback() {
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
                        if (Utils.getDatasString(json).contains("address_id") || Utils.getDatasString(json).equals("1")) {
                            mActivity.runOnUiThread(() -> {
                                mView.editAddressResult("保存成功");
                            });
                        }
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
    }
}
