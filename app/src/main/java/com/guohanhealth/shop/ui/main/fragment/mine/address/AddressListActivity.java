package com.guohanhealth.shop.ui.main.fragment.mine.address;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseActivity;
import com.guohanhealth.shop.bean.AddressInfo;
import com.guohanhealth.shop.bean.AddressManagerInfo;
import com.guohanhealth.shop.custom.CustomDialog;
import com.guohanhealth.shop.custom.EmptyView;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.ApiService;
import com.guohanhealth.shop.http.HttpErrorCode;
import com.guohanhealth.shop.utils.Utils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class AddressListActivity extends BaseActivity<AddressPersenter, AddressModel> implements AddressView {


    @BindView(R.id.emptyview)
    EmptyView emptyview;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.add)
    ImageView add;
    private List<AddressInfo> mList;
    private Adapters mAdapters;
    private int curpage = 1;
    int type = 0;//不同页面进入标识

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_address_list;
    }

    boolean isLoad = false;

    @Override
    protected void initView(Bundle savedInstanceState) {
        type = getIntent().getIntExtra(Constants.ORDERTYPE, 0);
        back.setOnClickListener(v -> finish());
        title.setText("收货地址管理");
        add.setOnClickListener(v -> readyGo(AddressAddActivity.class));
        emptyview.setEmpty("我不知道该送到哪儿去", "点击添加收货地址");
        emptyview.setClickListener(v -> readyGo(AddressAddActivity.class));
        smartrefreshlayout.setRefreshHeader(new ClassicsHeader(mContext));
        smartrefreshlayout.setOnRefreshListener(view -> {
            curpage = 1;
            isLoad = false;
            getAddressListData();
        });
        smartrefreshlayout.setOnLoadMoreListener(view -> {
            if (Boolean.valueOf(App.getApp().Hasmore()) && App.getApp().getPage_total() > curpage) {
                isLoad = true;
                curpage++;
                getAddressListData();
            } else {
                stopReOrLoad();
            }
        });
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mList = new ArrayList<>();
        mAdapters = new Adapters(R.layout.address_item, mList);
        recyclerview.setAdapter(mAdapters);
        mAdapters.setOnItemClickListener((adapter, view, position) -> {
            if (type == 1) {
                Intent intent = new Intent();
                intent.putExtra(Constants.DATA, mList.get(position));
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddressListData();
    }


    @Override
    public void faild(String msg) {
        stopReOrLoad();
        showToast(msg);
        recyclerview.setVisibility(View.GONE);
        emptyview.setVisibility(View.VISIBLE);
    }

    @Override
    public void getData(Object info) {
        stopReOrLoad();
        if (!isLoad) {
            mList.clear();
        }
        AddressManagerInfo data = (AddressManagerInfo) info;
        if (Utils.isEmpty(data.address_list)) {
            mList.addAll(data.address_list);
            recyclerview.setVisibility(View.VISIBLE);
            emptyview.setVisibility(View.GONE);
        } else {
            recyclerview.setVisibility(View.GONE);
            emptyview.setVisibility(View.VISIBLE);
        }
        mAdapters.notifyDataSetChanged();
    }

    @Override
    public void editAddressResult(Object data) {
        showToast("操作成功");
        getAddressListData();
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


    class Adapters extends BaseQuickAdapter<AddressInfo, BaseViewHolder> {

        public Adapters(int layoutResId, @Nullable List<AddressInfo> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, AddressInfo item) {
            helper.setText(R.id.text1, item.true_name);
            helper.setText(R.id.text2, item.mob_phone);
            helper.setText(R.id.text3, item.area_info + " " + item.address);
            if (type == 1) {
                helper.getView(R.id.defaultaddress).setVisibility(View.GONE);
            } else {
                helper.getView(R.id.defaultaddress).setVisibility(View.VISIBLE);
                TextView textView = helper.getView(R.id.defaulttext);
                CheckBox check = helper.getView(R.id.checkbox);
                if (item.is_default.equals("1")) {
                    check.setChecked(true);
                    textView.setText("默认地址");
                } else {
                    check.setChecked(false);
                    textView.setText("设为默认");
                }
                check.setOnClickListener(v -> {
                    mPresenter.editAddressResult(ApiService.ADDRESS_EDIT, App.getApp().getKey(),
                            item.true_name, item.area_id, item.city_id, item.area_info, item.address, item.mob_phone, check.isChecked() ? "1" : "0", item.address_id);

                });
                helper.getView(R.id.view_del).setOnClickListener(v -> {
                    new CustomDialog.Builder(mContext)
                            .setTitle("提示")
                            .setMessage("确定删除该地址?")
                            .setPositiveButton("确定", (d, i) -> {
                                Api.post(ApiService.ADDRESS_DEL, new FormBody.Builder()
                                        .add("key", App.getApp().getKey())
                                        .add("address_id", item.address_id)
                                        .build(), new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        d.dismiss();
                                        runOnUiThread(() -> {
                                            showToast(Utils.getErrorString(e));
                                        });
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        d.dismiss();
                                        try {
                                            String json = response.body().string();
                                            if (Utils.getCode(json) == HttpErrorCode.HTTP_NO_ERROR) {
                                                if (Utils.getDatasString(json).equals("1")) {
                                                    runOnUiThread(() -> {
                                                        showToast("删除成功");
                                                        getAddressListData();
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
                            })
                            .setNegativeButton("取消", (d, i) -> {
                                d.dismiss();
                            }).create().show();
                });
                helper.getView(R.id.view_edit).setOnClickListener(v -> {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.DATA, item);
                    bundle.putInt("type", 1);
                    readyGo(AddressAddActivity.class, bundle);
                });
            }
        }
    }

    /**
     * 获取数据
     */
    public void getAddressListData() {
        mPresenter.getAddressList(App.getApp().getKey());
    }


}
