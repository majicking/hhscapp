package com.guohanhealth.shop.ui.main.fragment.classtype;

import com.guohanhealth.shop.base.BaseView;
import com.guohanhealth.shop.bean.BrandListInfo;
import com.guohanhealth.shop.bean.GoodsClassChildInfo;
import com.guohanhealth.shop.bean.GoodsClassInfo;

import java.util.List;

public interface ClassTypeView extends BaseView {
    void getGoodsClass(List<GoodsClassInfo.Class_list> info);

    void getBrandList(List<BrandListInfo.Brand_list> info);

    void getGoodsChild(List<GoodsClassChildInfo.Class_list> info);

}
