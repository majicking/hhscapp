package com.guohanhealth.shop.ui.main.fragment.mine.address;

import com.guohanhealth.shop.base.BaseModel;
import com.guohanhealth.shop.bean.AddressInfo;
import com.guohanhealth.shop.bean.AddressManagerInfo;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.RxHelper;

import io.reactivex.Observable;

public class AddressModel extends BaseModel {

    public Observable<AddressManagerInfo> getAddressList(String key) {
        return Api.getDefault().getAddressList(key).compose(RxHelper.handleResult());
    }
}
