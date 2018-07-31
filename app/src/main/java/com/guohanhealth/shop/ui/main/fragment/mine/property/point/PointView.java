package com.guohanhealth.shop.ui.main.fragment.mine.property.point;

import com.guohanhealth.shop.base.BaseView;
import com.guohanhealth.shop.bean.PointInfo;
import com.guohanhealth.shop.bean.PreInfo;

public interface PointView extends BaseView {
    void getPointList(PointInfo info);
    void getPreData(PreInfo info);
}
