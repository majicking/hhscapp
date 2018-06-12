package com.majick.guohanhealth.ui.goods.goodslist;

import com.majick.guohanhealth.base.BaseView;
import com.majick.guohanhealth.bean.GoodsListInfo;

public interface GoodsListView extends BaseView {
    void getGoodsListData(GoodsListInfo info);
}
