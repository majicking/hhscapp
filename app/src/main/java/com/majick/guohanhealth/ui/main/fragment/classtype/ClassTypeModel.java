package com.majick.guohanhealth.ui.main.fragment.classtype;

import com.majick.guohanhealth.base.BaseModel;
import com.majick.guohanhealth.bean.BrandListInfo;
import com.majick.guohanhealth.bean.GoodsClassChildInfo;
import com.majick.guohanhealth.bean.GoodsClassInfo;
import com.majick.guohanhealth.http.Api;
import com.majick.guohanhealth.http.RxHelper;

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
