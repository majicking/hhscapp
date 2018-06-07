package com.majick.guohanhealth.ui.main.fragment.mine;

import com.majick.guohanhealth.base.BasePresenter;

public class MinePersenter extends BasePresenter<MineView, MineModel> {

    public void getMineInfo(String key) {
        mRxManager.add(mModel.getMineInfo(key).subscribe(userInfo -> {
            mView.getMineInfo(userInfo);
        }, throwable -> {
            mView.fail(throwable.getMessage());
        }));
    }
}
