package com.majick.guohanhealth.ui.goods.goodslist;

import com.majick.guohanhealth.base.BaseView;
import com.majick.guohanhealth.bean.GoodsListInfo;

import java.util.List;

public interface GoodsListView extends BaseView {
    void getGoodsListData(List<GoodsListInfo.GoodsListBean> info);
}
