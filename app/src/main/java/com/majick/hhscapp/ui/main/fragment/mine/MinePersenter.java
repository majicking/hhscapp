package com.majick.hhscapp.ui.main.fragment.mine;

import com.majick.hhscapp.base.BasePresenter;

public class MinePersenter extends BasePresenter<MineView, MineModel> {

    public void getMineInfo(String key) {
        mRxManager.add(mModel.getMineInfo(key).subscribe(userInfo -> {
            mView.getMineInfo(userInfo);
        }, throwable -> {
            mView.fail(throwable.getMessage());
        }));
    }
}
