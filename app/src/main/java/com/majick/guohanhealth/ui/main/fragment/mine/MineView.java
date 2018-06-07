package com.majick.guohanhealth.ui.main.fragment.mine;

import com.majick.guohanhealth.base.BaseView;
import com.majick.guohanhealth.bean.MineInfo;
import com.majick.guohanhealth.bean.UserInfo;
import com.majick.guohanhealth.http.Result;

public interface MineView extends BaseView{
    void getMineInfo(MineInfo info);
    void fail(String msg);
}
