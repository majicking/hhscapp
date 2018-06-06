package com.majick.hhscapp.ui.main.fragment.mine;

import com.majick.hhscapp.base.BaseModel;
import com.majick.hhscapp.bean.MineInfo;
import com.majick.hhscapp.http.Api;
import com.majick.hhscapp.http.RxHelper;

import io.reactivex.Observable;

public class MineModel extends BaseModel {
    public Observable<MineInfo> getMineInfo(String key) {
        return Api.getDefault().getMineInfo(key).compose(RxHelper.handleResult());
    }
}
