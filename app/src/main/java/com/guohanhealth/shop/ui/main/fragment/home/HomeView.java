package com.guohanhealth.shop.ui.main.fragment.home;

import com.guohanhealth.shop.base.BaseView;
import com.guohanhealth.shop.bean.Adv_list;
import com.guohanhealth.shop.bean.GoodsInfo;
import com.guohanhealth.shop.bean.Home1Info;
import com.guohanhealth.shop.bean.Home2Info;
import com.guohanhealth.shop.bean.Home3Info;
import com.guohanhealth.shop.bean.Home4Info;
import com.guohanhealth.shop.bean.Home5Info;
import com.guohanhealth.shop.bean.SearchWordsInfo;

import org.json.JSONObject;

import java.util.List;

public interface HomeView extends BaseView {

    void showHome1(Home1Info info);

    void showHome2(Home2Info jsonobj);

    void showHome3(Home3Info jsonobj);

    void showHome4(Home4Info jsonobj);

    void showHome5(Home5Info jsonobj);

    void showAdvList(List<Adv_list.Item> jsonobj);

    void showVideoView(JSONObject jsonobj);

    void showGoods(GoodsInfo goodsInfo);

    void showGoods1(JSONObject jsonobj);

    void showGoods2(JSONObject jsonobj);
    void stopRefresh();

}
