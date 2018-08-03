package com.guohanhealth.shop.ui.cart;

import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.base.BasePresenter;
import com.guohanhealth.shop.bean.ChatInfo;
import com.guohanhealth.shop.bean.ChatMessageInfo;
import com.guohanhealth.shop.bean.ChatSendMsgInfo;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.ApiService;
import com.guohanhealth.shop.http.HttpErrorCode;
import com.guohanhealth.shop.utils.Logutils;
import com.guohanhealth.shop.utils.Utils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class ChatPersenter extends BasePresenter<ChatView, ChatModel> {
    public void getChatDate() {
        Api.post(ApiService.GET_USER_LIST, new FormBody.Builder()
                        .add("key", App.getApp().getKey())
                        .build(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        mActivity.runOnUiThread(() -> {
                            mView.faild(Utils.getErrorString(e));
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        List<ChatInfo> mlist = new ArrayList<>();
                        try {
                            String json = response.body().string();
                            if (Utils.getCode(json) == HttpErrorCode.HTTP_NO_ERROR) {
                                JSONObject object = new JSONObject();//记录UID
                                JSONObject obj = new JSONObject(Utils.getDatasString(json));
                                String uList = obj.getString("list");
                                JSONObject arr = new JSONObject(uList);
                                int size = null == arr ? 0 : arr.length();
                                mlist.clear();
                                if (size < 1) {
                                    mView.getData(mlist);
                                } else {
                                    Iterator<?> itName = arr.keys();
                                    while (itName.hasNext()) {
                                        String ID = itName.next().toString();
                                        String str = arr.getString(ID);
                                        ChatInfo bean = Utils.getObject(str, ChatInfo.class);
                                        mlist.add(bean);
                                        object.put(bean.u_id, "0");
                                    }
                                    mActivity.runOnUiThread(() -> {
                                        mView.getData(mlist);
                                    });
                                    App.getApp().UpDateUser();
                                    App.getApp().getSocket().emit("get_state", object);
                                }
                            } else if (Utils.getCode(json) == HttpErrorCode.ERROR_400) {
                                mActivity.runOnUiThread(() -> {
                                    mView.faild(Utils.getErrorString(json));
                                });
                            }
                        } catch (Exception e) {
                            mActivity.runOnUiThread(() -> {
                                mView.faild(Utils.getErrorString(e));
                            });
                        }
                    }
                }
        );
    }

    public void sendMessage(String key, String t_id, final String t_name, String t_msg) {
        Api.post(ApiService.SEND_MSG, new FormBody.Builder()
                        .add("key", key)
                        .add("t_id", t_id)
                        .add("t_name", t_name)
                        .add("t_msg", t_msg)
                        .build()
                , new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        mActivity.runOnUiThread(() -> {
                            mView.faild(Utils.getErrorString(e));
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            String json = response.body().string();
                            if (Utils.getCode(json) == HttpErrorCode.HTTP_NO_ERROR) {
                                mActivity.runOnUiThread(() -> {
                                    mView.getData(Utils.getDatasString(json));
                                });
                            } else if (Utils.getCode(json) == HttpErrorCode.ERROR_400) {
                                mActivity.runOnUiThread(() -> {
                                    mView.faild(Utils.getErrorString(json));
                                });
                            }
                        } catch (Exception e) {
                            mActivity.runOnUiThread(() -> {
                                mView.faild(Utils.getErrorString(e));
                            });
                        }
                    }
                }
        );
    }
}
