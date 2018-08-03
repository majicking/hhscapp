package com.guohanhealth.shop.ui.cart;

import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guohanhealth.shop.R;
import com.guohanhealth.shop.adapter.CommonAdapter;
import com.guohanhealth.shop.adapter.ViewHolder;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BaseActivity;
import com.guohanhealth.shop.bean.ChatMessageInfo;
import com.guohanhealth.shop.bean.SmileInfo;
import com.guohanhealth.shop.custom.MyGridView;
import com.guohanhealth.shop.event.ChatEvent;
import com.guohanhealth.shop.event.RxBus;
import com.guohanhealth.shop.utils.Utils;
import com.guohanhealth.shop.utils.engine.GlideEngine;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartActivity extends BaseActivity<ChatPersenter, ChatModel> implements ChatView {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.history)
    TextView history;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.smile)
    ImageView smile;
    @BindView(R.id.editmsg)
    EditText editmsg;
    @BindView(R.id.sendmsg)
    Button sendmsg;
    @BindView(R.id.gridview)
    MyGridView gridview;
    private Adapters mAdapters;
    private List<ChatMessageInfo> mList;
    private boolean simFlag;
    private List<SmileInfo> mSmileList;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_cart2;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    String id, name, avatar, message;


    @Override
    protected void initView(Bundle savedInstanceState) {
        id = getIntent().getStringExtra(Constants.CHATID);
        name = getIntent().getStringExtra(Constants.CHATNAME);
        avatar = getIntent().getStringExtra(Constants.CHATAVATAR);
        message = getIntent().getStringExtra(Constants.DATA);
        back.setOnClickListener(v -> finish());
        title.setText(Utils.isEmpty(name) ? name : "我的聊天");
        mList = new ArrayList<>();
        mAdapters = new Adapters(R.layout.chat_item, mList);
        recycleview.setLayoutManager(new LinearLayoutManager(mContext));
        recycleview.setAdapter(mAdapters);
        mList.clear();
        setChat(message);
        RxBus.getDefault().register(this, ChatEvent.class, data -> {
            setChat((String) data.obj);
        });
        recycleview.setOnTouchListener((v, event) -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
            gridview.setVisibility(View.GONE);
            simFlag = false;
            return false;
        });
        sendmsg.setOnClickListener(v -> {
            if (!Utils.isEmpty(editmsg)) {
                showToast("请输入内容");
                return;
            }
            mPresenter.sendMessage(App.getApp().getKey(), id, name, Utils.getEditViewText(editmsg));
            editmsg.setText("");
            sendmsg.setEnabled(false);
        });
        smile.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }

            if (simFlag) {
                simFlag = false;
                gridview.setVisibility(View.GONE);
            } else {
                simFlag = true;
                gridview.setVisibility(View.VISIBLE);
            }

        });
        mSmileList = SmileInfo.getSmileList();
        gridview.setAdapter(new CommonAdapter<SmileInfo>(mContext, mSmileList, R.layout.img_item) {
            @Override
            public void convert(ViewHolder viewHolder, SmileInfo item, int position, View convertView, ViewGroup parentViewGroup) {
                viewHolder.setImageBitmap(R.id.img, item.path);
            }
        });
        gridview.setOnItemClickListener((arg0, arg1, arg2, arg3) -> {
            SmileInfo bean = (SmileInfo) gridview.getItemAtPosition(arg2);
            if (bean != null) {
                Bitmap bitmap = null;
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), bean.path);
                ImageSpan imageSpan = new ImageSpan(mContext, bitmap);
                SpannableString spannableString = new SpannableString(bean.title);
                spannableString.setSpan(imageSpan, 0, bean.title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//					chat_editmessage.append(spannableString);
                int index = editmsg.getSelectionStart();
                Editable editable = editmsg.getText();
                editable.insert(index, spannableString);
            }
        });
        editmsg.setOnTouchListener((v, event) -> {
            if (simFlag) {
                simFlag = false;
                gridview.setVisibility(View.GONE);
            }
            return false;
        });

    }

    public void setChat(String message) {
        try {
            JSONObject obj = new JSONObject(message);
            Iterator<?> itName = obj.keys();
            while (itName.hasNext()) {
                String ID = itName.next().toString();
                String str = obj.getString(ID);
                ChatMessageInfo bean = Utils.getObject(str, ChatMessageInfo.class);
                if (id.equals(bean.f_id)) {
                    mList.add(bean);
                }
            }
            if (Utils.isEmpty(mList)) {
                App.getApp().mediaPlayer();
                App.getApp().getSocket().emit("del_msg", new JSONObject("{'max_id':" + mList.get(mList.size() - 1).m_id + ",'f_id':" + id + "}"));
                recycleview.scrollToPosition(mList.size() - 1);
                mAdapters.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void faild(String msg) {
        showToast(msg);
        sendmsg.setEnabled(true);

    }

    @Override
    public void getData(Object data) {
        sendmsg.setEnabled(true);
        try {
            ChatMessageInfo msg = Utils.getObject(Utils.getValue("msg", (String) data), ChatMessageInfo.class);
            msg.isSend = true;
            mList.add(msg);
            recycleview.scrollToPosition(mList.size() - 1);
            mAdapters.notifyDataSetChanged();
            App.getApp().getSocket().emit("send_msg", new JSONObject(Utils.getValue("msg", (String) data)));
        } catch (Exception e) {
            e.printStackTrace();
            showToast(Utils.getErrorString(e));
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    class Adapters extends BaseQuickAdapter<ChatMessageInfo, BaseViewHolder> {

        public Adapters(int layoutResId, @Nullable List<ChatMessageInfo> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, ChatMessageInfo item) {
            helper.setText(R.id.text, item.add_time);
            View sendmsgview = helper.getView(R.id.sendmsgview);
            View getmsgview = helper.getView(R.id.getmsgview);
            TextView text1 = helper.getView(R.id.text1);
            TextView text11 = helper.getView(R.id.text11);
            String msg = item.t_msg;
            text1.setText("");
            text11.setText("");
            SpannableString spannableString = new SpannableString(msg);
            for (int i = 0; i < mSmileList.size(); i++) {
                for (int j = 0; j < mSmileList.size(); j++) {
                    int num = msg.indexOf(mSmileList.get(j).title);
                    if (num != -1) {
                        String str = "";
                        for (int k = 0; k < mSmileList.get(j).title.length(); k++) {
                            str += "1";
                        }
                        msg = msg.replaceFirst(mSmileList.get(j).title, str);
                        int start = num;
                        int end = num + mSmileList.get(j).title.length();
                        Bitmap bitmap = null;
                        bitmap = BitmapFactory.decodeResource(mContext.getResources(), mSmileList.get(j).path);
                        ImageSpan imageSpan = new ImageSpan(mContext, bitmap);
                        spannableString.setSpan(imageSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }

            if (!item.isSend) {
                getmsgview.setVisibility(View.VISIBLE);
                sendmsgview.setVisibility(View.GONE);
                text1.append(spannableString);
                helper.setText(R.id.text2, item.f_name);
                GlideEngine.getInstance().loadCircleImage(mContext, 80, R.mipmap.djk_icon_member, helper.getView(R.id.img1), avatar);
            } else {
                sendmsgview.setVisibility(View.VISIBLE);
                getmsgview.setVisibility(View.GONE);
                GlideEngine.getInstance().loadCircleImage(mContext, 80, R.mipmap.djk_icon_member, helper.getView(R.id.img2), App.getApp().getInfo().member_avatar);
                text11.append(spannableString);
                helper.setText(R.id.text22, item.f_name);
            }

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        App.getApp().setNotify(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        App.getApp().setNotify(true);
        RxBus.getDefault().unRegister(this);
    }
}
