package com.guohanhealth.shop.ui.main.fragment.classtype;

import com.guohanhealth.shop.base.BasePresenter;
import com.guohanhealth.shop.http.ConsumerError;

public class ClassTypePersenter extends BasePresenter<ClassTypeView, ClassTypeModel> {

    /**
     * 获取商品大类
     */
    public void getGoodsClass() {
        mRxManager.add(mModel.getGoodsClass().subscribe(goodsClassInfo -> {
            mView.getGoodsClass(goodsClassInfo.class_list);
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));
    }

    /**
     * 获取商品推荐
     */
    public void getBrandList() {
        mRxManager.add(mModel.getBrandList().subscribe(brandListInfo -> {
            mView.getBrandList(brandListInfo.brand_list);
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));
    }

    /**
     * 获取商品子类
     */
    public void getGoodsChild(String gc_id) {
        mRxManager.add(mModel.getGoodsChild(gc_id).subscribe(goodsChild -> {
            mView.getGoodsChild(goodsChild.class_list);
        }, new ConsumerError<Throwable>() {
            @Override
            public void onError(int errorCode, String message) {
                mView.faild(message);
            }
        }));
    }
}
