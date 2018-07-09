package com.guohanhealth.shop.ui.main.fragment.mine;

import com.guohanhealth.shop.base.BasePresenter;

public class MinePersenter extends BasePresenter<MineView, MineModel> {

    public void getMineInfo(String key) {
        mRxManager.add(mModel.getMineInfo(key).subscribe(userInfo -> {
            mView.getMineInfo(userInfo);
        }, throwable -> {
            mView.faild(throwable.getMessage());
        }));
    }
}
