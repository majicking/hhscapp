package com.guohanhealth.shop.ui.goods.goodslist;

import com.guohanhealth.shop.base.BaseView;
import com.guohanhealth.shop.bean.GoodsListInfo;

import java.util.List;

public interface GoodsListView extends BaseView {
    void getGoodsListData(List<GoodsListInfo.GoodsListBean> info);
}
