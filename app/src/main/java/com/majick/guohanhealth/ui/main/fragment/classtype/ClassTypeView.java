package com.majick.guohanhealth.ui.main.fragment.classtype;

import com.majick.guohanhealth.base.BaseView;
import com.majick.guohanhealth.bean.BrandListInfo;
import com.majick.guohanhealth.bean.GoodsClassChildInfo;
import com.majick.guohanhealth.bean.GoodsClassInfo;

import java.util.List;

public interface ClassTypeView extends BaseView {
    void getGoodsClass(List<GoodsClassInfo.Class_list> info);

    void getBrandList(List<BrandListInfo.Brand_list> info);

    void getGoodsChild(List<GoodsClassChildInfo.Class_list> info);

    void fail(String msg);
}
