package com.guohanhealth.shop.ui.main.fragment.mine;

import com.guohanhealth.shop.base.BaseView;
import com.guohanhealth.shop.bean.MineInfo;
import com.guohanhealth.shop.bean.UserInfo;
import com.guohanhealth.shop.http.Result;

public interface MineView extends BaseView{
    void getMineInfo(MineInfo info);
}
