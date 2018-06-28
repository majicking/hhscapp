package com.majick.guohanhealth.ui.goods.goodsdetailed.fragment.comment;

import com.majick.guohanhealth.base.BasePresenter;
import com.majick.guohanhealth.http.ConsumerError;
import com.majick.guohanhealth.ui.goods.GoodsModel;

public class CommentPersenter extends BasePresenter<CommentView, GoodsModel> {
    public void getGoodsEvaluateData(String goods_id, String curpage, String type, String page) {
        mRxManager.add(mModel.goodsEvaluate(goods_id, curpage, type, page).subscribe(info -> {
            mView.getData(info.goods_eval_list);
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));
    }
}
