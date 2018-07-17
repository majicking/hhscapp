package com.guohanhealth.shop.ui.main.fragment.mine;

import com.guohanhealth.shop.base.BasePresenter;
import com.guohanhealth.shop.http.ConsumerError;

public class MinePersenter extends BasePresenter<MineView, MineModel> {

    public void getMineInfo(String key) {
        mRxManager.add(mModel.getMineInfo(key).subscribe(userInfo -> {
            mView.getMineInfo(userInfo);
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));
    }
}
