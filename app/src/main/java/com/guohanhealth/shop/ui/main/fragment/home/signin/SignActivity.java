package com.guohanhealth.shop.ui.main.fragment.home.signin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.base.BaseActivity;
import com.guohanhealth.shop.bean.SignInfo;
import com.guohanhealth.shop.custom.CustomDialog;
import com.guohanhealth.shop.custom.EmptyView;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.ApiService;
import com.guohanhealth.shop.http.HttpErrorCode;
import com.guohanhealth.shop.utils.Utils;
import com.guohanhealth.shop.view.htmltext.HtmlTextView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SignActivity extends BaseActivity {


    @BindView(R.id.common_toolbar_title_tv)
    TextView commonToolbarTitleTv;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.text2)
    TextView text2;
    @BindView(R.id.text3)
    TextView text3;
    @BindView(R.id.text4)
    TextView text4;
    @BindView(R.id.view1)
    LinearLayout view1;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;
    @BindView(R.id.emptyview)
    EmptyView emptyview;
    private List<SignInfo.DataBean> mList;
    private boolean isLoad = false;
    int curpage = 1;
    boolean hasmore = false;
    int page_total = 1;
    private DataAdapter mAdapter;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_sign;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initToolBarNav(commonToolbar, commonToolbarTitleTv, "每日签到");
        emptyview.setEmpty("一次都没签到哟", "立即签到吧！");
        text2.setOnClickListener(v -> {
            HtmlTextView htmlTextView = new HtmlTextView(mContext);
            String html = "  <ul>\n" +
                    "      <li>1、每人每天最多签到一次，签到后有机会获得积分。</li>\n" +
                    "      <li>2、网站可根据活动举办的实际情况，在法律允许的范围内，对本活动规则变动或调整。</li>\n" +
                    "      <li>3、对不正当手段（包括但不限于作弊、扰乱系统、实施网络攻击等）参与活动的用户，网站有权禁止其参与活动，取消其获奖资格（如奖励已发放，网站有权追回）。</li>\n" +
                    "      <li>4、活动期间，如遭遇自然灾害、网络攻击或系统故障等不可抗拒因素导致活动暂停举办，网站无需承担赔偿责任或进行补偿。</li>\n" +
                    "    </ul>";
            htmlTextView.setHtmlFromString(html, false);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            htmlTextView.setLayoutParams(layoutParams);
            new CustomDialog.Builder(mContext)
                    .setContentView(htmlTextView)
                    .setTitle("活动说明")
                    .setOnlyOneButton(true)
                    .setNegativeButton("确定", (dialog1, which) -> {
                        dialog1.dismiss();
                    })
                    .create().show();
        });
        mList = new ArrayList();
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        smartrefreshlayout.setRefreshHeader(new ClassicsHeader(mContext));
        smartrefreshlayout.setOnRefreshListener(refreshLayout -> {
            curpage = 1;
            isLoad = false;
            getPointListData();
            getDataChecked();
            getPointData();
        });
        smartrefreshlayout.setOnLoadMoreListener(refreshLayout -> {
            if (hasmore && page_total > curpage) {
                isLoad = true;
                curpage++;
                getPointListData();
            } else {
                stopReOrLoad();
            }
        });
        mAdapter = new DataAdapter(R.layout.sign_item, mList);
        recyclerview.setAdapter(mAdapter);
    }

    class DataAdapter extends BaseQuickAdapter<SignInfo.DataBean, com.chad.library.adapter.base.BaseViewHolder> {

        public DataAdapter(int layoutResId, @Nullable List<SignInfo.DataBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(com.chad.library.adapter.base.BaseViewHolder helper, SignInfo.DataBean item) {
            helper.setText(R.id.text1, item.sl_points);
            helper.setText(R.id.text2, item.sl_addtime_text);
        }
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
        getPointListData();
        getDataChecked();
        getPointData();
    }

    public void getDataChecked() {
        Api.get(ApiService.SIGN + "&key=" + App.getApp().getKey() + "&random=" + new Random().nextInt(10), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    showToast(Utils.getErrorString(e));
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    if (Utils.getCode(json) == HttpErrorCode.HTTP_NO_ERROR) {
                        runOnUiThread(() -> {
                            String points_signin = Utils.getValue("points_signin", Utils.getDatasString(json));
                            text3.setText("签到");
                            text4.setText("+" + (!Utils.isEmpty(points_signin) ? "0" : points_signin) + "积分");
                        });
                    } else if (Utils.getCode(json) == HttpErrorCode.ERROR_400) {
                        runOnUiThread(() -> {
                            text3.setText(Utils.getErrorString(json));
                            text4.setText("坚持哟!");
                        });
                    } else {
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
    }

    public void getPointData() {
        Api.get(ApiService.MY_ASSET +
                "&key=" + App.getApp().getKey() +
                "&random=" + new Random().nextInt(10)
                + "&fields=point", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    showToast(Utils.getErrorString(e));
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    if (Utils.getCode(json) == HttpErrorCode.HTTP_NO_ERROR) {
                        runOnUiThread(() -> {
                            String point = Utils.getValue("point", Utils.getDatasString(json));
                            text1.setText("我的积分:" + (!Utils.isEmpty(point) ? "0" : point));
                        });
                    } else {
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
    }


    public void getPointListData() {
        Api.get(ApiService.SIGNIN_LIST +
                "&key=" + App.getApp().getKey() +
                "&random=" + new Random().nextInt(10) +
                "&curpage=" + curpage +
                "&page=10" +
                "&fields=point", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    showToast(Utils.getErrorString(e));
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    if (Utils.getCode(json) == HttpErrorCode.HTTP_NO_ERROR) {
                        runOnUiThread(() -> {
                            stopReOrLoad();
                            hasmore = Boolean.valueOf(Utils.getValue("hasmore", json));
                            page_total = Integer.valueOf(Utils.getValue("page_total", json));
                            SignInfo info = Utils.getObject(Utils.getDatasString(json), SignInfo.class);
                            if (isLoad) {
                                mList.clear();
                            }
                            if (Utils.isEmpty(info.signin_list)) {
                                recyclerview.setVisibility(View.VISIBLE);
                                emptyview.setVisibility(View.GONE);
                                mList.addAll(info.signin_list);
                            } else {
                                recyclerview.setVisibility(View.GONE);
                                emptyview.setVisibility(View.VISIBLE);
                            }
                            mAdapter.notifyDataSetChanged();
                        });
                    } else {
                        runOnUiThread(() -> {
                            stopReOrLoad();
                            emptyview.setVisibility(View.VISIBLE);
                            recyclerview.setVisibility(View.GONE);
                            showToast(Utils.getErrorString(json));
                        });
                    }
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        stopReOrLoad();
                        emptyview.setVisibility(View.VISIBLE);
                        recyclerview.setVisibility(View.GONE);
                        showToast(Utils.getErrorString(e));
                    });
                }
            }
        });
    }

}
