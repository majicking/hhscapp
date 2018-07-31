package com.guohanhealth.shop.ui.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseActivity;
import com.guohanhealth.shop.bean.SearchBean;
import com.guohanhealth.shop.bean.SearchInfo;
import com.guohanhealth.shop.bean.greendao.SearchBeanDao;
import com.guohanhealth.shop.ui.goods.goodslist.GoodsListActivity;
import com.guohanhealth.shop.utils.Logutils;
import com.guohanhealth.shop.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

public class SearchActivity extends BaseActivity<SearchPercenter, SearchModel> implements SearchView {


    @BindView(R.id.search_back)
    RelativeLayout searchBack;
    @BindView(R.id.search_edit)
    EditText searchEdit;
    @BindView(R.id.search_btn)
    Button searchBtn;
    @BindView(R.id.search_del)
    Button searchDel;
    @BindView(R.id.search_view_history)
    LinearLayout searchViewHistory;
    @BindView(R.id.tagcontainerlayout)
    TagContainerLayout tagcontainerlayout;
    @BindView(R.id.historytagcontainerlayout)
    TagContainerLayout historytagcontainerlayout;
    private List<String> hotlist;
    private List<String> historylist;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        searchBack.setOnClickListener(v -> finish());
        searchBtn.setOnClickListener(v -> {
            doSomeThing(Utils.getEditViewText(searchEdit));
        });
        hotlist = new ArrayList<>();
        historylist = new ArrayList<>();
        searchEdit.setHint(App.getApp().getHotname());
        searchEdit.setText(App.getApp().getHotvalue());

        tagcontainerlayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                doSomeThing(text);
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }
        });
        historytagcontainerlayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                doSomeThing(text);
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }
        });
        searchDel.setOnClickListener(v -> {
            if (historylist != null && historylist.size() > 0) {
                App.getDaoSession().getSearchBeanDao().deleteAll();
                searchViewHistory.setVisibility(View.GONE);
            } else {
                showToast("暂无数据");
            }
        });

        setHistoryData();
    }

    private void setHistoryData() {
        List<SearchBean> list = App.getDaoSession().getSearchBeanDao().queryBuilder().build().list();
        if (list != null && list.size() > 0) {
            searchDel.setActivated(true);
            for (int i = 0; i < list.size(); i++) {
                historylist.add(list.get(i).getName());
            }
            historytagcontainerlayout.setTags(historylist);
        } else {
            searchDel.setActivated(false);
        }
        for (int i = 0; i < historytagcontainerlayout.getChildCount(); i++) {
            TagView tagView = (TagView) historytagcontainerlayout.getChildAt(i);
            tagView.setTagTextColor(Constants.BGCOLORS[new Random().nextInt(10)]);
        }
    }

    public void doSomeThing(String value) {

        if (Utils.isEmpty(value)) {
            List<SearchBean> list = App.getDaoSession().getSearchBeanDao().queryBuilder().where(SearchBeanDao.Properties.Name.eq(value)).list();
            if (list == null || (list != null && list.size() < 1)) {
                App.getDaoSession().getSearchBeanDao().insertOrReplace(new SearchBean(value));
            }
        }
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEYWORD, value);
        readyGoThenKill(GoodsListActivity.class, bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getSearchDataList();
    }


    @Override
    public void faild(String msg) {
        showToast(msg);
    }

    @Override
    public void getSearchList(SearchInfo info) {
        historylist.clear();
        hotlist.clear();
        hotlist = info.list;
        if (hotlist != null && hotlist.size() > 0) {
            tagcontainerlayout.setTags(hotlist);
        }
        searchViewHistory.setVisibility(View.VISIBLE);
        if (info.his_list != null && info.his_list.size() > 0) {
            historytagcontainerlayout.setTags(info.his_list);
        } else {
            setHistoryData();
        }

        for (int i = 0; i < tagcontainerlayout.getChildCount(); i++) {
            TagView tagView = (TagView) tagcontainerlayout.getChildAt(i);
            tagView.setTagTextColor(Constants.BGCOLORS[new Random().nextInt(10)]);
        }
    }

}
