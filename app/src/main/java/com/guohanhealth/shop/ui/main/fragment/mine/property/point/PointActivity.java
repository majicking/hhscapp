package com.guohanhealth.shop.ui.main.fragment.mine.property.point;

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
import com.guohanhealth.shop.bean.PointInfo;
import com.guohanhealth.shop.bean.PreInfo;
import com.guohanhealth.shop.custom.EmptyView;
import com.guohanhealth.shop.ui.main.fragment.mine.property.PredepositModel;
import com.guohanhealth.shop.utils.Utils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PointActivity extends BaseActivity<PointPersenter, PredepositModel> implements PointView {


    @BindView(R.id.common_toolbar_title_tv)
    TextView commonToolbarTitleTv;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.point)
    TextView point;
    @BindView(R.id.emptyview)
    EmptyView emptyview;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;
    private List<PointInfo.DataBean> mList;
    private Adapters mAdapters;
    private int curpage=1;
    private boolean isLoad;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_point;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initToolBarNav(commonToolbar, commonToolbarTitleTv, "我的积分");
        smartrefreshlayout.setRefreshHeader(new ClassicsHeader(mContext));
        smartrefreshlayout.setOnRefreshListener(refreshLayout -> {
            curpage = 1;
            isLoad = false;
            mPresenter.getPre(App.getApp().getKey(), "point");
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
        emptyview.setEmpty("暂无积分记录", "消费，登陆都能获取积分哟");
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapters = new Adapters(R.layout.property_item, mList);
        recyclerview.setAdapter(mAdapters);
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

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getPointList(App.getApp().getKey(), curpage + "");
        mPresenter.getPre(App.getApp().getKey(), "point");
    }

    @Override
    public void getPointList(PointInfo info) {
        stopReOrLoad();

        if (info != null) {
            if (!isLoad) {
                mList.clear();
            }
            if (Utils.isEmpty(info.log_list)) {
                mList.addAll(info.log_list);
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
    public void faild(String msg) {
        showToast(msg);
        stopReOrLoad();
        recyclerview.setVisibility(View.GONE);
        emptyview.setVisibility(View.VISIBLE);
    }

    @Override
    public void getPreData(PreInfo info) {
        point.setText(info.point);
    }

    class Adapters extends BaseQuickAdapter<PointInfo.DataBean, BaseViewHolder> {

        public Adapters(int layoutResId, @Nullable List<PointInfo.DataBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, PointInfo.DataBean item) {
            helper.setText(R.id.text1, item.stagetext);
            helper.setText(R.id.text2, item.pl_desc);
            TextView textView = helper.getView(R.id.text3);
            if (Utils.getString(item.pl_points).charAt(0) == '-') {
                textView.setTextColor(mContext.getResources().getColor(R.color.green));
                textView.setText(item.pl_points);
            } else {
                textView.setTextColor(mContext.getResources().getColor(R.color.appColor));
                textView.setText("+" + item.pl_points);
            }
            helper.setText(R.id.text4, item.addtimetext);
        }
    }

}
