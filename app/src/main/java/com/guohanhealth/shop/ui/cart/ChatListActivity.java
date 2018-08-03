package com.guohanhealth.shop.ui.cart;

import android.app.Service;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
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
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseActivity;
import com.guohanhealth.shop.bean.ChatInfo;
import com.guohanhealth.shop.bean.ChatMessageInfo;
import com.guohanhealth.shop.custom.CustomDialog;
import com.guohanhealth.shop.custom.EmptyView;
import com.guohanhealth.shop.event.ChatListEvent;
import com.guohanhealth.shop.event.RxBus;
import com.guohanhealth.shop.utils.Utils;
import com.guohanhealth.shop.utils.engine.GlideEngine;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

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
    private boolean isrefish = false;

    private ArrayList<ChatInfo> mList;
    private Adapters mAdapters;
    private Map<String, String> mStatusMap;
    private Map<String, ChatMessageInfo> chatmsgList;
    private Map<String, Integer> msgNumMap;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_cart;
    }

    @Override
    protected void onStart() {
        super.onStart();
        /**进入聊天列表取消通知栏*/
        App.getApp().setNotify(false);
    }


    @Override
    protected void onPause() {
        super.onPause();
        /**进入聊天列表开启通知栏*/
        App.getApp().setNotify(true);
//        RxBus.getDefault().unRegister(this);
    }

    String message;
    int isNewMessage = 0;
    int isNewStatus = 0;

    @Override
    protected void initView(Bundle savedInstanceState) {
        mStatusMap = new HashMap<>();
        msgNumMap = new HashMap<>();
        chatmsgList = new HashMap<>();
        initToolBarNav(commonToolbar, commonToolbarTitleTv, "消息列表");
        emptyview.setEmpty("还没有聊天记录", "有问题记得找客服哟");
        smartrefreshlayout.setRefreshHeader(new ClassicsHeader(mContext));
        /**清除所以通知栏*/
        App.getApp().getNotificationManager().cancelAll();
        smartrefreshlayout.setOnRefreshListener(view -> {
            curpage = 1;
            isLoad = false;
            isrefish = true;
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
        mAdapters = new Adapters(R.layout.chatlist_item, mList);
        recyclerview.setAdapter(mAdapters);
        mAdapters.setOnItemClickListener((adapter, view, position) -> {
            if (mList.get(position).u_id.equals(App.getApp().getInfo().member_id)) {
                showToast("不能和自己聊天");
                return;
            }

            if (App.getApp().isConnect()) {//本人在线
                if (mStatusMap != null && mStatusMap.size() > 0) {
                    if (mStatusMap.get(mList.get(position).u_id) != null &&
                            mStatusMap.get(mList.get(position).u_id).equals("1")) {
                        msgNumMap.remove(mList.get(position).u_id);
                        mAdapters.notifyDataSetChanged();
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.CHATID, mList.get(position).u_id);
                        bundle.putString(Constants.CHATNAME, mList.get(position).u_name);
                        bundle.putString(Constants.CHATAVATAR, mList.get(position).avatar);
                        bundle.putString(Constants.DATA, message);
                        readyGo(CartActivity.class, bundle);
                    } else {
                        showToast("你的好友已经离线啦");
                    }
                } else {
                    showToast("你的好友已经离线啦");
                }
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
        RxBus.getDefault().register(this, ChatListEvent.class, data -> {
            try {
                if (data.type.equals("message")) {
                    message = (String) data.obj;
                    JSONObject obj = new JSONObject((String) data.obj);
                    Iterator<?> itName = obj.keys();
                    while (itName.hasNext()) {
                        String ID = itName.next().toString();
                        String str = obj.getString(ID);
                        ChatMessageInfo info = Utils.getObject(str, ChatMessageInfo.class);

                        if (msgNumMap.containsKey(info.f_id)) {//消息来了数量加1
                            int count = msgNumMap.get(info.f_id);
                            count++;
                            msgNumMap.put(info.f_id, count);
                        } else {
                            msgNumMap.put(info.f_id, 1);
                        }
                        chatmsgList.put(info.user.u_id, info);
                    }

                    List<ChatMessageInfo> updatalist = new ArrayList<>();
                    List<ChatMessageInfo> addlist = new ArrayList<>();
                    List<String> updataidlist = new ArrayList<>();
                    List<String> addidlist = new ArrayList<>();
                    List<String> itemidlist = new ArrayList<>();

                    for (HashMap.Entry<String, ChatMessageInfo> item : chatmsgList.entrySet()) {
                        updataidlist.add(item.getKey()); //把用户id装进集合
                        addidlist.add(item.getKey());
                    }
                    for (int i = 0; i < mList.size(); i++) {
                        itemidlist.add(mList.get(i).u_id);
                    }
                    //有几个是存在现在得列表中的 更新列表
                    updataidlist.retainAll(itemidlist);
                    if (Utils.isEmpty(updataidlist)) {
                        for (int i = 0; i < updataidlist.size(); i++) {
                            updatalist.add(chatmsgList.get(updataidlist.get(i)));
                        }
                        for (ChatMessageInfo updata : updatalist) {
                            if (itemidlist.indexOf(updata.user.u_id) != -1) {
                                ChatInfo childitem = mList.get(itemidlist.indexOf(updata.user.u_id));
                                childitem.t_msg = updata.t_msg;
//                            childitem.avatar = updata.user.avatar;
                                childitem.time = updata.add_time;
                                mStatusMap.put(childitem.u_id, "1");
                            }
                        }
                    }
                    //有几个是不存在现在列表的 添加进来
                    addidlist.removeAll(itemidlist);
                    if (Utils.isEmpty(addidlist)) {
                        for (int i = 0; i < addidlist.size(); i++) {
                            addlist.add(chatmsgList.get(addidlist));
                        }
                        for (ChatMessageInfo add : addlist) {
                            ChatInfo childitem = new ChatInfo();
                            childitem.time = add.add_time;
                            childitem.avatar = add.user.avatar;
                            childitem.t_msg = add.t_msg;
                            childitem.u_id = add.user.u_id;
                            childitem.r_state = "1";
                            childitem.recent = 1;
                            childitem.u_name = add.user.u_name;
                            childitem.m_id = add.m_id;
                            mList.add(childitem);
                            mStatusMap.put(childitem.u_id, "1");
                        }
                    }
//                if (Utils.isEmpty(mList)) {
//                    for (int i = 0; i < mList.size(); i++) {
//                        App.getApp().getSocket().emit("get_state", new JSONObject().put(mList.get(i).u_id, "0"));
//                    }
//                }
                    if (isNewMessage > 0) {
                        if (!isrefish) {
                            App.getApp().mediaPlayer();
                        }
                    }
                    isrefish = false;
                    isNewMessage++;
                } else if (data.type.equals("get_state")) {//查询是否在线
                    JSONObject getStateObj = new JSONObject(new JSONObject((String) data.obj).getString("u_state"));
                    Iterator<?> itName = getStateObj.keys();
                    mStatusMap.clear();
                    while (itName.hasNext()) {
                        String u_id = itName.next().toString();
                        String status = getStateObj.getString(u_id);
                        mStatusMap.put(u_id, status);
                    }
                    if (isNewStatus > 0) {
                        if (!isrefish) {
                            App.getApp().mediaPlayer();
                        }
                    }
                    isrefish = false;
                    isNewStatus++;
                }

                mAdapters.notifyDataSetChanged();
            } catch (Exception e) {
                showToast(Utils.getErrorString(e));
            }
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
                TextView number = helper.getView(R.id.text4);
                GlideEngine.getInstance().loadCircleImage(mContext, 80, R.mipmap.djk_icon_member, helper.getView(R.id.img1), item.avatar);

                if (mStatusMap != null && mStatusMap.size() > 0) {

                    if (mStatusMap.get(item.u_id) != null && mStatusMap.get(item.u_id).equals("1")) {//如果在线
                        helper.getView(R.id.img2).setVisibility(View.GONE);
                        if (item.r_state.equals("2")) {//1已读 2未读
                            if (msgNumMap != null && msgNumMap.size() > 0) {
                                if (msgNumMap.get(item.u_id) != null) {
                                    number.setVisibility(View.VISIBLE);
                                    number.setText("" + msgNumMap.get(item.u_id));//设置数量
                                }
                            } else {//数量=0隐藏
                                number.setVisibility(View.GONE);
                            }
                        } else {//已读 隐藏
                            number.setVisibility(View.GONE);
                        }
                    } else {//不在线
                        number.setVisibility(View.GONE);
                        helper.getView(R.id.img2).setVisibility(View.VISIBLE);
                    }
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
