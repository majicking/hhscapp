package com.guohanhealth.shop.ui.main.fragment.mine.property.rechargecardbalance;

import com.guohanhealth.shop.base.BaseView;
import com.guohanhealth.shop.bean.PreInfo;
import com.guohanhealth.shop.bean.RcdInfo;

public interface RechargeCardView extends BaseView {
    void getData(Object info);
    void getPreData(PreInfo info);
    void getResult(String s);
}
