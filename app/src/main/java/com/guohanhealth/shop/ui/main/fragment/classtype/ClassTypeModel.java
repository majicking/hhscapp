package com.guohanhealth.shop.ui.main.fragment.classtype;

import com.guohanhealth.shop.base.BaseModel;
import com.guohanhealth.shop.bean.BrandListInfo;
import com.guohanhealth.shop.bean.GoodsClassChildInfo;
import com.guohanhealth.shop.bean.GoodsClassInfo;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.RxHelper;

import io.reactivex.Observable;

public class ClassTypeModel extends BaseModel {
    /**
     * 获取商品大类
     */
    public Observable<GoodsClassInfo> getGoodsClass() {
        return Api.getDefault().getGoodsClass().compose(RxHelper.handleResult());
    }

    /**
     * 获取商品推荐
     */
    public Observable<BrandListInfo> getBrandList() {
        return Api.getDefault().getBrandList().compose(RxHelper.handleResult());
    }

    /**
     * 获取商品子类
     */
    public Observable<GoodsClassChildInfo> getGoodsChild(String gc_id) {
        return Api.getDefault().getGoodsChild(gc_id).compose(RxHelper.handleResult());
    }
}
