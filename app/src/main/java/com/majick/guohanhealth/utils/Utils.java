package com.majick.guohanhealth.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.majick.guohanhealth.R;
import com.majick.guohanhealth.app.App;
import com.majick.guohanhealth.app.Constants;
import com.majick.guohanhealth.bean.WxPayInfo;
import com.majick.guohanhealth.custom.CustomPopuWindow;
import com.majick.guohanhealth.http.Api;
import com.majick.guohanhealth.http.ApiService;
import com.majick.guohanhealth.http.HttpErrorCode;
import com.majick.guohanhealth.ui.WebViewActivity;
import com.majick.guohanhealth.ui.goods.goodsdetailed.activity.GoodsDetailsActivity;
import com.majick.guohanhealth.ui.login.LoginActivity;
import com.majick.guohanhealth.ui.search.SearchActivity;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

/**
 * 2018/4/27 15:14
 * Created  by:
 * MailAddress：majick@aliyun.com
 */
public class Utils {
    /**
     * 获取当前类上的泛型参数，并实例化
     *
     * @param object 当前类
     * @param index  类泛型参数角标
     * @param <T>    泛型实例化对象
     * @return 泛型实例化对象
     */
    public static <T> T getGenericInstance(Object object, int index) {
        try {
            ParameterizedType type = (ParameterizedType) (object.getClass().getGenericSuperclass());//获取当前类的父类泛型参数
            Class<T> clazz = (Class) (type.getActualTypeArguments()[index]);//获取泛型class
            return clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {

        }
        return null;
    }


    public static boolean isEmpty(EditText editText) {
        if (editText != null) {
            String string = editText.getText().toString().trim();
            return isEmpty(string);
        }
        return false;
    }

    public static boolean isEmpty(List<?> list) {
        if (list != null && list.size() > 0)
            return true;
        return false;
    }

    public static boolean isEmpty(Object[] list) {
        if (list != null && list.length > 0)
            return true;
        return false;
    }

    public static boolean isEmpty(TextView textView) {
        if (textView != null) {
            String string = textView.getText().toString().trim();
            return isEmpty(string);
        }
        return false;
    }


    public static boolean isEmpty(String string) {
        if (string != null && string.length() > 0 && !"".equals(string) && !"[]".equals(string) && !"null".equals(string)) {
            return true;
        }
        return false;
    }

    public static String getEditViewText(EditText editText) {
        if (editText != null) {
            return isEmpty(editText.getText().toString().trim()) ? editText.getText().toString().trim() : "";
        }
        return "";
    }

    public static String getTextViewText(TextView textView) {
        if (textView != null) {
            return isEmpty(textView.getText().toString().trim()) ? textView.getText().toString().trim() : "";
        }
        return "";
    }

    /**
     * 相当于三元运算
     */
    public static String getString(String string) {
        if (isEmpty(string))
            return string;
        return "";
    }

    /**
     * 检查是否已登录，如果未登录直接弹出登录页面
     */
    public static boolean isLogin(Context context) {
        if (isEmpty(App.getApp().getKey()) && App.getApp().isLogin())
            return true;
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        return false;
    }


    public static void GoToType(Context context, String type, String data) {
        switch (type) {
            case Constants.KEYWORD:
                Intent intent = new Intent(context, SearchActivity.class);
                intent.putExtra(Constants.KEYWORD, data);
                intent.putExtra("gc_name", data);
                context.startActivity(intent);
                break;
            case "special"://专题编号
                Intent intent1 = new Intent(context, WebViewActivity.class);
                intent1.putExtra("url", Constants.URL + "act=index&op=special&special_id=" + data + "&type=html");
                context.startActivity(intent1);
                break;
            case "goods":
                //商品编号
                Intent intent2 = new Intent(context, GoodsDetailsActivity.class);
                intent2.putExtra(Constants.GOODS_ID, data);
                context.startActivity(intent2);
                break;
            case "url":
                //地址
                Intent intent3 = new Intent(context, WebViewActivity.class);
                intent3.putExtra("url", data);
                context.startActivity(intent3);
                break;
        }
    }


    public static CustomPopuWindow getPopuWindown(Context mContext, View view, int point) {
        CustomPopuWindow popuWindow = new CustomPopuWindow.PopupWindowBuilder(mContext)
                .setView(view)
                .size(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                .setFocusable(true).setOnDissmissListener(() -> {
                    WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
                    lp.alpha = 1; //0.0-1.0
                    ((Activity) mContext).getWindow().setAttributes(lp);
                }).create().setBackgroundDrawable((Drawable) null)
                .showAtLocation(view, point, 0, 0);
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = 0.4f; //0.0-1.0
        ((Activity) mContext).getWindow().setAttributes(lp);
        return popuWindow;
    }

    public static CustomPopuWindow shopPayWindown(Context context, List<String> paylist, String pay_sn, String type, View.OnClickListener closelistener) {
        View view = LayoutInflater.from(context).inflate(R.layout.payway, null);
        ImageView close = view.findViewById(R.id.close);
        close.setOnClickListener(closelistener);
        View alipay = view.findViewById(R.id.alipay);
        View wxpay = view.findViewById(R.id.wxpay);
        if (Utils.isEmpty(paylist)) {
            for (int i = 0; i < paylist.size(); i++) {
                if (paylist.get(i).equals("alipay")) {
                    alipay.setVisibility(View.VISIBLE);
                    int finalI = i;
                    alipay.setOnClickListener(v -> {
                        payData(context, pay_sn, paylist.get(finalI), type);
                    });
                }
                if (paylist.get(i).equals("wxpay")) {
                    wxpay.setVisibility(View.VISIBLE);
                    int finalI1 = i;
                    wxpay.setOnClickListener(v -> {
                        payData(context, pay_sn, paylist.get(finalI1), type);
                    });
                }
            }
        }


        return getPopuWindown(context, view, Gravity.BOTTOM);
    }

    private static void payData(Context context, String pay_sn, String type, String is_virtual) {
        String url = "";
        if (type.equals("alipay")) {
            url = (is_virtual.equals("1") ? ApiService.ALIPAYURLV : ApiService.ALIPAYURL);
        } else if (type.equals("wxpay")) {
            url = (is_virtual.equals("1") ? ApiService.WXPAYURLV : ApiService.WXPAYURL);
        }
        Api.post(url, new FormBody.Builder()
                .add("key", App.getApp().getKey())
                .add("pay_sn", pay_sn)
                .build(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ((Activity) context).runOnUiThread(() -> {

                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                if (response.code() == HttpErrorCode.HTTP_NO_ERROR) {
                    ((Activity) context).runOnUiThread(() -> {
                        if (type.equals("alipay")) {
                            String signStr = JSONParser.getStringFromJsonString("signStr", JSONParser.getStringFromJsonString("datas", json));
                            String notify_url = getNotifyUrl(signStr);
                            Logutils.i(signStr);
                        } else if (type.equals("wxpay")) {
                            WxPayInfo info = JSONParser.JSON2Object(JSONParser.getStringFromJsonString("datas", json), WxPayInfo.class);
//                            "package":"Sign=WXPay",
                            Logutils.i(info);
                        }

                    });
                }

            }
        });
    }

    @NonNull
    private static String getNotifyUrl(String signStr) {
        String[] split = signStr.split("\\&");
        String urls = "";
        for (String s : split) {
            if (s.contains("notify_url")) {
                urls = s;
                break;
            }
        }
        return urls.split("\\=")[1].replace("\"", "");
    }


//    /**
//     * 获取微信参数
//     *
//     * @param pay_sn 支付编号
//     */
//    public static void loadingWXPaymentData(final Context context, String pay_sn, String type) {
//        if (type.equals("3")) {
//            RemoteDataHandler.asyncDataStringGet(Constants.WEIXINPAY_PREDBROADCAST + pay_sn + "&key=" + MyShopApplication.getInstance().getLoginKey(), data -> {
//                String json = data.getJson();
//                if (data.getCode() == HttpStatus.SC_OK) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(json);
//                        String appid = jsonObject.getString("appid");// 微信开放平台appid
//                        String noncestr = jsonObject
//                                .getString("noncestr");// 随机字符串
//                        String packageStr = jsonObject
//                                .getString("package");// 支付内容
//                        String partnerid = jsonObject
//                                .getString("partnerid");// 财付通id
//                        String prepayid = jsonObject
//                                .getString("prepayid");// 微信预支付编号
//                        String sign = jsonObject.getString("sign");// 签名
//                        String timestamp = jsonObject
//                                .getString("timestamp");// 时间戳
//
//                        IWXAPI api = WXAPIFactory.createWXAPI(context, appid);
//
//                        PayReq req = new PayReq();
//                        req.appId = appid;
//                        req.partnerId = partnerid;
//                        req.prepayId = prepayid;
//                        req.nonceStr = noncestr;
//                        req.timeStamp = timestamp;
//                        req.packageValue = packageStr;
//                        req.sign = sign;
//                        req.extData = "app data"; // optional
//                        LogUtils.i("id=" + req.appId);
////                        Toast.makeText(context, "正常调起支付",
////                                Toast.LENGTH_SHORT).show();
//                        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
//                        boolean result = api.sendReq(req);
//                        LogUtils.i("唤起微信结果=" + result);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    ShopHelper.showApiError(context, json);
//                }
//            });
//            return;
//        }
//        HashMap<String, String> params = new HashMap<String, String>();
//        params.put("key", MyShopApplication.getInstance().getLoginKey());
//        params.put("pay_sn", pay_sn);
//        LogUtils.i(Constants.URL_MEMBER_WX_PAYMENT);
//        LogUtils.i(MyShopApplication.getInstance().getLoginKey());
//        LogUtils.i(pay_sn);
//
//        RemoteDataHandler.asyncLoginPostDataString(
//                type.equals("1") ? Constants.URL_MEMBER_WX_PAYMENT : Constants.URL_MEMBER_WX_VPAYMENT, params, MyShopApplication.getInstance(),
//                data -> {
//                    String json = data.getJson();
//                    if (data.getCode() == HttpStatus.SC_OK) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(json);
//                            String appid = jsonObject.getString("appid");// 微信开放平台appid
//                            String noncestr = jsonObject
//                                    .getString("noncestr");// 随机字符串
//                            String packageStr = jsonObject
//                                    .getString("package");// 支付内容
//                            String partnerid = jsonObject
//                                    .getString("partnerid");// 财付通id
//                            String prepayid = jsonObject
//                                    .getString("prepayid");// 微信预支付编号
//                            String sign = jsonObject.getString("sign");// 签名
//                            String timestamp = jsonObject
//                                    .getString("timestamp");// 时间戳
//
//                            IWXAPI api = WXAPIFactory.createWXAPI(context, appid);
//
//                            PayReq req = new PayReq();
//                            req.appId = appid;
//                            req.partnerId = partnerid;
//                            req.prepayId = prepayid;
//                            req.nonceStr = noncestr;
//                            req.timeStamp = timestamp;
//                            req.packageValue = packageStr;
//                            req.sign = sign;
//                            req.extData = "app data"; // optional
//                            LogUtils.i("id=" + req.appId);
////                            Toast.makeText(context, "正常调起支付",
////                                    Toast.LENGTH_SHORT).show();
//                            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
//                            boolean result = api.sendReq(req);
//                            LogUtils.i("唤起微信结果=" + result);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//                        ShopHelper.showApiError(context, json);
//                    }
//                });
//    }
//
//    /**
//     * 获取支付宝原生支付的参数act=member_payment_recharge&op=alipay_native_pay&pay_sn=&payment_code=alipay_native
//     */
//    public static void loadingAlipayNativePaymentData(final Context context, String pay_sn, String type, final DataCallback callback) {
//        if (type.equals("3")) {
//            RemoteDataHandler.asyncDataStringGet(Constants.ALIPAY_MEMBERPAYMENTRECHARGE + pay_sn + "&key=" + MyShopApplication.getInstance().getLoginKey(), data -> {
//                String json = data.getJson();
//                if (data.getCode() == HttpStatus.SC_OK) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(json);
//                        String signStr = jsonObject.optString("signStr");
//                        LogUtils.i(signStr);
//                        String[] split = signStr.split("\\&");
//                        String urls = "";
//                        for (String s : split) {
//                            if (s.contains("notify_url")) {
//                                urls = s;
//                                break;
//                            }
//                        }
//                        MyShopApplication.getInstance().setNotify_ur(urls.split("\\=")[1].replace("\"", ""));
//                        PayDemoActivity payDemoActivity = new PayDemoActivity(context, signStr, callback);
//                        payDemoActivity.doPay();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    ShopHelper.showApiError(context, json);
//                }
//            });
//            return;
//        }
//
//        HashMap<String, String> params = new HashMap<String, String>();
//        params.put("key", MyShopApplication.getInstance().getLoginKey());
//        params.put("pay_sn", pay_sn);
//        RemoteDataHandler.asyncLoginPostDataString(
//                type.equals("1") ? Constants.URL_ALIPAY_NATIVE_GOODS : Constants.URL_ALIPAY_NATIVE_Virtual, params, MyShopApplication.getInstance(),
//                data -> {
//                    String json = data.getJson();
//                    if (data.getCode() == HttpStatus.SC_OK) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(json);
//                            String signStr = jsonObject.optString("signStr");
//                            LogUtils.i(signStr);
//                            String[] split = signStr.split("\\&");
//                            String urls = "";
//                            for (String s : split) {
//                                if (s.contains("notify_url")) {
//                                    urls = s;
//                                    break;
//                                }
//                            }
//                            MyShopApplication.getInstance().setNotify_ur(urls.split("\\=")[1].replace("\"", ""));
//                            PayDemoActivity payDemoActivity = new PayDemoActivity(context, signStr, callback);
//                            payDemoActivity.doPay();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//                        ShopHelper.showApiError(context, json);
//                    }
//                });
//    }


}
