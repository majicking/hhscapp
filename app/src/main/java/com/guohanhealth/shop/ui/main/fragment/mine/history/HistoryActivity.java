package com.guohanhealth.shop.ui.main.fragment.mine.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseActivity;
import com.guohanhealth.shop.base.BaseView;
import com.guohanhealth.shop.bean.GoodsCollectInfo;
import com.guohanhealth.shop.bean.HistoryInfo;
import com.guohanhealth.shop.custom.EmptyView;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.ApiService;
import com.guohanhealth.shop.http.HttpErrorCode;
import com.guohanhealth.shop.ui.goods.goodsdetailed.activity.GoodsDetailsActivity;
import com.guohanhealth.shop.ui.main.activity.MainActivity;
import com.guohanhealth.shop.ui.main.fragment.mine.collect.GoodsCollectFragment;
import com.guohanhealth.shop.utils.Utils;
import com.guohanhealth.shop.utils.engine.GlideEngine;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class HistoryActivity extends BaseActivity<HistoryPersenter, HistoryModel> implements BaseView {


    @BindView(R.id.common_toolbar_title_tv)
    TextView commonToolbarTitleTv;
    @BindView(R.id.common_toolbar_img_right)
    ImageView commonToolbarImgRight;
    @BindView(R.id.common_toolbar_title_right)
    TextView commonToolbarTitleRight;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;
    @BindView(R.id.emptyview)
    EmptyView emptyview;
    private Adapters mAdapters;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_history;
    }

    boolean isLoad;
    public List<HistoryInfo.DataBean> mList;

    @Override
    protected void initView(Bundle savedInstanceState) {
        initToolBarNav(commonToolbar, commonToolbarTitleTv, "我的足迹",
                commonToolbarImgRight, R.drawable.icon_del);
        commonToolbarImgRight.setOnClickListener(v -> {
            if (Utils.isEmpty(mList)) {
                Api.post(ApiService.BROWSE_CLEARALL, new FormBody.Builder().add("key", App.getApp().getKey()).build(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(() -> {
                            showToast(Utils.getErrorString(e));
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            String json = response.body().string();
                            if (Utils.getCode(json) == HttpErrorCode.HTTP_NO_ERROR) {
                                if (Utils.getDatasString(json).equals("1")) {
                                    runOnUiThread(() -> {
                                        showToast("操作成功");
                                        getHistoryData();
                                    });
                                }
                            } else if (Utils.getCode(json) == HttpErrorCode.ERROR_400) {
                                runOnUiThread(() -> {
                                    showToast(Utils.getErrorString(json));
                                });
                            }
                        } catch (Exception e) {
                            runOnUiThread(() -> {
                                showToast(Utils.getErrorString(e));
                            });
                        }

                    }
                });
            } else {
                showToast("没有记录啦");
            }
        });
        mList = new ArrayList<>();
        smartrefreshlayout.setRefreshHeader(new ClassicsHeader(mContext));
        smartrefreshlayout.setOnRefreshListener(refreshLayout -> {
            curpage = 1;
            isLoad = false;
            getHistoryData();
        });
        smartrefreshlayout.setOnLoadMoreListener(refreshLayout -> {
            if (Boolean.valueOf(App.getApp().Hasmore()) && App.getApp().getPage_total() > curpage) {
                isLoad = true;
                curpage++;
                getHistoryData();
            } else {
                stopReOrLoad();
            }
        });
        emptyview.setEmpty("暂无您的浏览记录", "可以去看看哪些想要买的");
        emptyview.setVisibility(View.VISIBLE);
        emptyview.setClickListener(v -> {
            readyGoThenKill(MainActivity.class);
        });
        mAdapters = new Adapters(R.layout.history_item, mList);
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerview.setAdapter(mAdapters);
        mAdapters.setOnItemClickListener((a, v, i) -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.GOODS_ID, mList.get(i).goods_id);
            readyGo(GoodsDetailsActivity.class, bundle);
        });
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
        getHistoryData();
    }

    public void getHistoryData() {
        mPresenter.getHistoryList(App.getApp().getKey(), curpage + "");
    }

    class Adapters extends BaseQuickAdapter<HistoryInfo.DataBean, BaseViewHolder> {

        public Adapters(int layoutResId, @Nullable List<HistoryInfo.DataBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, HistoryInfo.DataBean item) {
            GlideEngine.getInstance().loadImage(mContext, helper.getView(R.id.img), item.goods_image_url);
            helper.setText(R.id.text1, item.goods_name);
            helper.setText(R.id.text2, "￥ " + item.goods_promotion_price);
            helper.setText(R.id.text3, item.browsetime_text);
        }
    }

    int curpage = 1;


    @Override
    public void faild(String msg) {
        showToast(msg);
        stopReOrLoad();
        recyclerview.setVisibility(View.GONE);
        emptyview.setVisibility(View.VISIBLE);
    }

    @Override
    public void getData(Object data) {
        stopReOrLoad();
        HistoryInfo info = (HistoryInfo) data;
        if (info != null) {
            if (!isLoad) {
                mList.clear();
            }
            if (Utils.isEmpty(info.goodsbrowse_list)) {
                recyclerview.setVisibility(View.VISIBLE);
                emptyview.setVisibility(View.GONE);
                mList.addAll(info.goodsbrowse_list);
            } else {
                recyclerview.setVisibility(View.GONE);
                emptyview.setVisibility(View.VISIBLE);
            }
            mAdapters.notifyDataSetChanged();
        }
    }
}
