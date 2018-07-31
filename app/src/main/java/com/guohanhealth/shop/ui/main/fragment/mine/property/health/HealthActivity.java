package com.guohanhealth.shop.ui.main.fragment.mine.property.health;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.base.BaseActivity;
import com.guohanhealth.shop.bean.HealthInfo;
import com.guohanhealth.shop.bean.HealthNumInfo;
import com.guohanhealth.shop.bean.PointInfo;
import com.guohanhealth.shop.custom.EmptyView;
import com.guohanhealth.shop.ui.main.fragment.mine.property.PredepositModel;
import com.guohanhealth.shop.ui.main.fragment.mine.property.point.PointActivity;
import com.guohanhealth.shop.utils.Utils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HealthActivity extends BaseActivity<HealthPersenter, PredepositModel> implements HealthView {


    @BindView(R.id.common_toolbar_title_tv)
    TextView commonToolbarTitleTv;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.healthbean)
    TextView healthbean;
    @BindView(R.id.emptyview)
    EmptyView emptyview;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_health;
    }

    private List<HealthInfo.DataBean> mList;
    private Adapters mAdapters;
    private int curpage=1;
    private boolean isLoad;

    @Override
    protected void initView(Bundle savedInstanceState) {
        initToolBarNav(commonToolbar, commonToolbarTitleTv, "我的健康豆");
        smartrefreshlayout.setRefreshHeader(new ClassicsHeader(mContext));
        smartrefreshlayout.setOnRefreshListener(refreshLayout -> {
            curpage = 1;
            isLoad = false;
            mPresenter.getHealthNum(App.getApp().getKey());
            mPresenter.getPointList(App.getApp().getKey(), curpage + "");
        });
        smartrefreshlayout.setOnLoadMoreListener(refreshLayout -> {
            if (Boolean.valueOf(App.getApp().Hasmore()) && App.getApp().getPage_total() > curpage) {
                isLoad = true;
                curpage++;
                mPresenter.getPointList(App.getApp().getKey(), curpage + "");
            } else {
                stopReOrLoad();
            }
        });
        mList = new ArrayList<>();
        emptyview.setEmpty("暂无健康豆使用记录", "健康豆会一定速率增长");
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapters = new Adapters(R.layout.property_item, mList);
        recyclerview.setAdapter(mAdapters);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getPointList(App.getApp().getKey(), curpage + "");
        mPresenter.getHealthNum(App.getApp().getKey());
    }

    @Override
    public void faild(String msg) {
        showToast(msg);
        stopReOrLoad();
        recyclerview.setVisibility(View.GONE);
        emptyview.setVisibility(View.VISIBLE);
    }

    @Override
    public void getHealthList(HealthInfo info) {
        stopReOrLoad();
        if (info != null) {
            if (!isLoad) {
                mList.clear();
            }
            if (Utils.isEmpty(info.data)) {
                mList.addAll(info.data);
                recyclerview.setVisibility(View.VISIBLE);
                emptyview.setVisibility(View.GONE);
            } else {
                recyclerview.setVisibility(View.GONE);
                emptyview.setVisibility(View.VISIBLE);
            }
            mAdapters.notifyDataSetChanged();
        }
    }

    @Override
    public void getHealthNum(HealthNumInfo info) {
        healthbean.setText(info.Value);
    }

    private void stopReOrLoad() {
        if (smartrefreshlayout != null) {
            if (smartrefreshlayout.isLoading()) {
                smartrefreshlayout.finishLoadMore();
            }
            if (smartrefreshlayout.isRefreshing()) {
                smartrefreshlayout.finishRefresh();
            }
        }
    }


    class Adapters extends BaseQuickAdapter<HealthInfo.DataBean, BaseViewHolder> {

        public Adapters(int layoutResId, @Nullable List<HealthInfo.DataBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, HealthInfo.DataBean item) {
            helper.setText(R.id.text1, "订单编号：" + item.order_sn);
            helper.getView(R.id.text2).setVisibility(View.GONE);
            TextView textView = helper.getView(R.id.text3);
            if (Utils.getString(item.way).equals("2")) {
                textView.setTextColor(mContext.getResources().getColor(R.color.green));
                textView.setText("-" + item.value);
            } else if (Utils.getString(item.way).equals("1")) {
                textView.setTextColor(mContext.getResources().getColor(R.color.appColor));
                textView.setText("+" + item.value);
            }
            helper.setText(R.id.text4, item.created_time);
        }
    }

}
