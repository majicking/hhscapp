package com.guohanhealth.shop.ui.search;

import com.guohanhealth.shop.base.BaseView;
import com.guohanhealth.shop.bean.SearchInfo;
import com.guohanhealth.shop.bean.SearchWordsInfo;

public interface SearchView extends BaseView {
    void getSearchList(SearchInfo info);

}
