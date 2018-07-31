package com.guohanhealth.shop.ui.cart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.base.BaseActivity;
import com.guohanhealth.shop.base.ChatInfo;
import com.guohanhealth.shop.bean.AddressManagerInfo;
import com.guohanhealth.shop.custom.CustomDialog;
import com.guohanhealth.shop.custom.EmptyView;
import com.guohanhealth.shop.event.RxBus;
import com.guohanhealth.shop.utils.Utils;
import com.guohanhealth.shop.utils.engine.GlideEngine;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatListActivity extends BaseActivity<ChatPersenter, ChatModel> implements ChatView {


    @BindView(R.id.common_toolbar_title_tv)
    TextView commonToolbarTitleTv;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;
    @BindView(R.id.emptyview)
    EmptyView emptyview;
    private int curpage;
    private boolean isLoad = false;
    private ArrayList<ChatInfo> mList;
    private Adapters mAdapters;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_cart;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initToolBarNav(commonToolbar, commonToolbarTitleTv, "消息列表");
        emptyview.setEmpty("还没有聊天记录", "有问题记得找客服哟");
        smartrefreshlayout.setRefreshHeader(new ClassicsHeader(mContext));
        /**清除所以通知栏*/
        App.getApp().getNotificationManager().cancelAll();
        smartrefreshlayout.setOnRefreshListener(view -> {
            curpage = 1;
            isLoad = false;
            getData();
        });
        smartrefreshlayout.setOnLoadMoreListener(view -> {
            if (Boolean.valueOf(App.getApp().Hasmore()) && App.getApp().getPage_total() > curpage) {
                isLoad = true;
                curpage++;
                getData();
            } else {
                stopReOrLoad();
            }
        });
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mList = new ArrayList<>();
        mAdapters = new Adapters(R.layout.chat_item, mList);
        recyclerview.setAdapter(mAdapters);
        mAdapters.setOnItemClickListener((adapter, view, position) -> {
            if (App.getApp().isConnect()) {

            } else {
                new CustomDialog.Builder(mContext)
                        .setTitle("离线通知")
                        .setMessage("您的IM账号已经离线啦？点击确认重连")
                        .setPositiveButton("确认", (d, w) -> {
                            d.dismiss();
                            App.getApp().getSocket().connect();
                        })
                        .setNegativeButton("取消", (d, w) -> {
                            d.dismiss();
                        }).create().show();
            }
        });
        RxBus.getDefault().register(this, Object.class, data -> {
            getData();
            RxBus.getDefault().unRegister(this);
        });
    }

    @Override
    public void getData(Object data) {
        stopReOrLoad();
        if (!isLoad) {
            mList.clear();
        }
        if (Utils.isEmpty((List<ChatInfo>) data)) {
            mList.addAll((List<ChatInfo>) data);
            recyclerview.setVisibility(View.VISIBLE);
            emptyview.setVisibility(View.GONE);
        } else {
            recyclerview.setVisibility(View.GONE);
            emptyview.setVisibility(View.VISIBLE);
        }
        mAdapters.notifyDataSetChanged();
    }

    class Adapters extends BaseQuickAdapter<ChatInfo, BaseViewHolder> {

        public Adapters(int layoutResId, @Nullable List<ChatInfo> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, ChatInfo item) {
            if (!App.getApp().getInfo().member_id.equals(item.m_id)) {
                helper.itemView.setVisibility(View.VISIBLE);
                helper.setText(R.id.text1, item.u_name);
                helper.setText(R.id.text2, item.time);
                helper.setText(R.id.text3, item.t_msg);
                GlideEngine.getInstance().loadCircleImage(mContext, 80, R.mipmap.djk_icon_member, helper.getView(R.id.img1), item.avatar);
                if (item.r_state.equals("1")) {
                    helper.getView(R.id.img2).setVisibility(View.GONE);
                } else {
                    helper.getView(R.id.img2).setVisibility(View.VISIBLE);
                }
            } else {
                helper.itemView.setVisibility(View.GONE);
            }

        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void faild(String msg) {
        stopReOrLoad();
        showToast(msg);
        recyclerview.setVisibility(View.GONE);
        emptyview.setVisibility(View.VISIBLE);
    }


    private void getData() {
        mPresenter.getChatDate();
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
}
