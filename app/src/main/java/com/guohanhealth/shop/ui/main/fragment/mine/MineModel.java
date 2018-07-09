package com.guohanhealth.shop.ui.main.fragment.mine;

import com.guohanhealth.shop.base.BaseModel;
import com.guohanhealth.shop.bean.MineInfo;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.RxHelper;

import io.reactivex.Observable;

public class MineModel extends BaseModel {
    public Observable<MineInfo> getMineInfo(String key) {
        return Api.getDefault().getMineInfo(key).compose(RxHelper.handleResult());
    }
}
