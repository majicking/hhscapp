package com.majick.hhscapp.ui.main.fragment.mine;

import com.majick.hhscapp.base.BaseView;
import com.majick.hhscapp.bean.MineInfo;
import com.majick.hhscapp.bean.UserInfo;
import com.majick.hhscapp.http.Result;

public interface MineView extends BaseView{
    void getMineInfo(MineInfo info);
    void fail(String msg);
}
