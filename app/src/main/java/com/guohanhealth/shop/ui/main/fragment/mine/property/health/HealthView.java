package com.guohanhealth.shop.ui.main.fragment.mine.property.health;

import com.guohanhealth.shop.base.BaseView;
import com.guohanhealth.shop.bean.HealthInfo;
import com.guohanhealth.shop.bean.HealthNumInfo;
import com.guohanhealth.shop.bean.PreInfo;

import java.util.List;

public interface HealthView extends BaseView {
    void getHealthList(HealthInfo info);
    void getHealthNum(HealthNumInfo info);
}
