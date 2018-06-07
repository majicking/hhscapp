package com.majick.guohanhealth.ui.main.fragment.mine;

import com.majick.guohanhealth.base.BaseModel;
import com.majick.guohanhealth.bean.MineInfo;
import com.majick.guohanhealth.http.Api;
import com.majick.guohanhealth.http.RxHelper;

import io.reactivex.Observable;

public class MineModel extends BaseModel {
    public Observable<MineInfo> getMineInfo(String key) {
        return Api.getDefault().getMineInfo(key).compose(RxHelper.handleResult());
    }
}
