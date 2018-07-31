package com.guohanhealth.shop.ui.main.fragment.home;

import android.os.Handler;
import android.os.Message;

import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.base.BasePresenter;
import com.guohanhealth.shop.bean.Adv_list;
import com.guohanhealth.shop.bean.GoodsInfo;
import com.guohanhealth.shop.bean.Home1Info;
import com.guohanhealth.shop.bean.Home2Info;
import com.guohanhealth.shop.bean.Home3Info;
import com.guohanhealth.shop.bean.Home4Info;
import com.guohanhealth.shop.bean.Home5Info;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.ApiService;
import com.guohanhealth.shop.http.HttpErrorCode;
import com.guohanhealth.shop.utils.JSONParser;
import com.guohanhealth.shop.utils.Logutils;
import com.guohanhealth.shop.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class HomePersenter extends BasePresenter<HomeView, HomeModel> {

    public void getHomeData() {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mView.stopRefresh();
                if (msg.what == 2) {
                    mView.faild(Utils.getErrorString((Exception) msg.obj));
                } else if (msg.what == 1) {
                    String json = (String) msg.obj;
                    try {
                        int code = Utils.getCode(json);
                        String datas = Utils.getDatasString(json);
                        if (code == 200) {
                            JSONArray arr = new JSONArray(datas);
                            int size = null == arr ? 0 : arr.length();
                            for (int i = 0; i < size; i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                JSONObject JsonObj = new JSONObject(obj.toString());
                                if (!JsonObj.isNull("home1")) {
                                    mView.showHome1(JSONParser.JSON2Object(JsonObj.getString("home1"), Home1Info.class));
                                } else if (!JsonObj.isNull("home2")) {
                                    mView.showHome2(JSONParser.JSON2Object(JsonObj.getString("home2"), Home2Info.class));
                                } else if (!JsonObj.isNull("home3")) {
                                    mView.showHome3(JSONParser.JSON2Object(JsonObj.getString("home3"), Home3Info.class));
                                } else if (!JsonObj.isNull("home4")) {
                                    mView.showHome4(JSONParser.JSON2Object(JsonObj.getString("home4"), Home4Info.class));
                                } else if (!JsonObj.isNull("home5")) {
                                    mView.showHome5(JSONParser.JSON2Object(JsonObj.getString("home5"), Home5Info.class));
                                } else if (!JsonObj.isNull("adv_list")) {//banner
                                    mView.showAdvList(JSONParser.JSON2Object(JsonObj.getString("adv_list"), Adv_list.class).item);
                                } else if (!JsonObj.isNull("video_list")) {     //视频接口
                                    mView.showVideoView(JsonObj);
                                } else if (!JsonObj.isNull("goods")) {//商品版块
                                    mView.showGoods(JSONParser.JSON2Object(JsonObj.getString("goods"), GoodsInfo.class));
                                } else if (!JsonObj.isNull("goods1")) {    //限时商品
                                    mView.showGoods1(JsonObj);
                                } else if (!JsonObj.isNull("goods2")) {     //抢购商品
                                    mView.showGoods2(JsonObj);
                                }
                            }

                        } else if (code == HttpErrorCode.ERROR_400) {
                            mView.faild(Utils.getErrorString(json));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mView.faild(Utils.getErrorString(e));
                    }
                }
            }
        };
        Api.getOkHttp().newCall(new Request.Builder().url(Constants.URLHEAD + ApiService.HOMEINDEX).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what = 2;
                message.obj = e;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Message message = new Message();
                message.what = 1;
                message.obj = json;
                handler.sendMessage(message);
            }
        });
    }


}
