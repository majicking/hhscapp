package com.majick.guohanhealth.ui.search;

import com.majick.guohanhealth.base.BaseView;
import com.majick.guohanhealth.bean.SearchInfo;
import com.majick.guohanhealth.bean.SearchWordsInfo;

public interface SearchView extends BaseView {
    void getSearchList(SearchInfo info);

}
