package com.guohanhealth.shop.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.JsonSyntaxException;
import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.bean.PayData;
import com.guohanhealth.shop.bean.PayInfos;
import com.guohanhealth.shop.bean.WxPayInfo;
import com.guohanhealth.shop.custom.CustomDialog;
import com.guohanhealth.shop.custom.CustomPopuWindow;
import com.guohanhealth.shop.event.CallBack;
import com.guohanhealth.shop.event.DataCallback;
import com.guohanhealth.shop.event.ObjectEvent;
import com.guohanhealth.shop.event.RxBus;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.ApiService;
import com.guohanhealth.shop.http.HttpErrorCode;
import com.guohanhealth.shop.http.ServerException;
import com.guohanhealth.shop.ui.WebViewActivity;
import com.guohanhealth.shop.ui.goods.goodsdetailed.activity.GoodsDetailsActivity;
import com.guohanhealth.shop.ui.goods.goodsorder.GoodsOrderActivity;
import com.guohanhealth.shop.ui.login.LoginActivity;
import com.guohanhealth.shop.ui.main.spec.SpecActivity;
import com.guohanhealth.shop.ui.order.OrderActivity;
import com.guohanhealth.shop.ui.search.SearchActivity;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zcw.togglebutton.ToggleButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

    /**
     * 获取bitmap
     *
     * @param filePath
     * @return
     */
    public static Bitmap getBitmapByPath(String filePath, int w, int h) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            File file = new File(filePath);
            if (file.exists()) {
                fis = new FileInputStream(file);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(filePath, options);
                int originalWidth = options.outWidth;//图片原始宽度
                int originalHeight = options.outHeight;//图片原始高度
                if ((originalWidth == -1) || (originalHeight == -1))
                    return null;
                //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
                int be = 1;//be=1表示不缩放
                if (originalWidth > originalHeight && originalWidth > w) {//如果宽度大的话根据宽度固定大小缩放
                    be = (int) (originalWidth / w);
                } else if (originalWidth < originalHeight && originalHeight > h) {//如果高度高的话根据宽度固定大小缩放
                    be = (int) (originalHeight / h);
                }
                if (be <= 0)
                    be = 1;
                options.inJustDecodeBounds = false;
                options.inSampleSize = be;//设置缩放比例
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                bitmap = BitmapFactory.decodeFile(filePath, options);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return compressImage(bitmap);
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;
        int bytes = baos.toByteArray().length;
        while ((bytes / 1024 > 100) && (options >= 20)) {  //循环判断如果压缩后图片是否大于10kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            bytes = baos.toByteArray().length;
        }
        image.recycle();
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 短暂显示Toast消息
     *
     * @param context
     * @param message
     */
    public static void showToast(Context context, String message) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_toast, null);
        TextView text = (TextView) view.findViewById(R.id.toast_message);
        text.setText(message);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 300);
        toast.setView(view);
        toast.show();
    }

    public static boolean isEmpty(EditText editText) {
        if (editText != null) {
            String string = editText.getText().toString().trim();
            return isEmpty(string);
        }
        return false;
    }

    public static boolean isEmpty(Map map) {
        if (map != null && map.size() > 0) {
            return true;
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
                Intent intent1 = new Intent(context, SpecActivity.class);
                intent1.putExtra("special_id", data);
                context.startActivity(intent1);
//                Intent intent1 = new Intent(context, WebViewActivity.class);
//                intent1.putExtra("url", Constants.URL + "act=index&op=special&special_id=" + data + "&type=html");
//                context.startActivity(intent1);
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
                .setOutsideTouchable(true)
                .setAnimationStyle(R.style.popuStyle)
                .size(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                .setFocusable(true).setOnDissmissListener(() -> {
                    WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
                    lp.alpha = 1; //0.0-1.0
                    ((Activity) mContext).getWindow().setAttributes(lp);
                }).create().setBackgroundDrawable((Drawable) null).showAtLocation(view, point, 0, 0);
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = 0.4f; //0.0-1.0
        ((Activity) mContext).getWindow().setAttributes(lp);
        return popuWindow;
    }

    public static PopupWindow shopPayWindown(Context context, List<String> paylist, String pay_sn, String type, View.OnClickListener closelistener) {
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
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.style.popuStyle);
        popupWindow.setOnDismissListener(() -> {
            WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
            lp.alpha = 1; //0.0-1.0
            ((Activity) context).getWindow().setAttributes(lp);
        });
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable((Drawable) null);
        if (!popupWindow.isShowing())
            popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = 0.4f; //0.0-1.0
        ((Activity) context).getWindow().setAttributes(lp);
        return popupWindow;

    }


    private static void payData(Context context, String pay_sn, String type, String is_virtual) {
        String url = "";
        if (type.equals("alipay")) {
            url = (is_virtual.equals("1") ? ApiService.ALIPAYURLV : is_virtual.equals("2") ? ApiService.ALIPAYMENT + pay_sn + "&key=" + App.getApp().getKey() : ApiService.ALIPAYURL);
        } else if (type.equals("wxpay")) {
            url = (is_virtual.equals("1") ? ApiService.WXPAYURLV : is_virtual.equals("2") ? ApiService.WEIXINMENT + pay_sn + "&key=" + App.getApp().getKey() : ApiService.WXPAYURL);
        }
        Api.post(url, new FormBody.Builder()
                .add("key", App.getApp().getKey())
                .add("pay_sn", pay_sn)
                .build(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                RxBus.getDefault().post(new ObjectEvent(e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String json = response.body().string();
                    if (getCode(json) == HttpErrorCode.HTTP_NO_ERROR) {
                        if (type.equals("alipay")) {
                            String signStr = JSONParser.getStringFromJsonString("signStr", JSONParser.getStringFromJsonString("datas", json));
                            String notify_url = getNotifyUrl(signStr);
                            Logutils.i(signStr);
                            App.getApp().setNotify_url(notify_url);
                            PayTask alipay = new PayTask((Activity) context);
                            String result = alipay.pay(signStr, true);
                            RxBus.getDefault().post(new PayResult(result));
//                            doPay(context, signStr);
                        } else if (type.equals("wxpay")) {
                            WxPayInfo info = JSONParser.JSON2Object(JSONParser.getStringFromJsonString("datas", json), WxPayInfo.class);
                            Logutils.i(info);
                            IWXAPI api = WXAPIFactory.createWXAPI(context, info.appid);
                            PayReq req = new PayReq();
                            req.appId = info.appid;
                            req.partnerId = info.partnerid;
                            req.prepayId = info.prepayid;
                            req.nonceStr = info.noncestr;
                            req.timeStamp = info.timestamp;
                            req.packageValue = "Sign=WXPay";
                            req.sign = info.sign;
                            req.extData = "app data"; // optional
                            Logutils.i("id=" + req.appId);
//                            Toast.makeText(context, "正常调起支付",
//                                    Toast.LENGTH_SHORT).show();
                            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                            boolean result = api.sendReq(req);
                            Logutils.i("唤起微信结果=" + result);
                        }

                    } else if (getCode(json) == HttpErrorCode.ERROR_400) {
                        RxBus.getDefault().post(new ObjectEvent(getErrorString(json)));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    RxBus.getDefault().post(new ObjectEvent(getErrorString(e)));
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

    public static String getErrorString(Exception t) {
        String errorMessage = "";
        if (t instanceof SocketException) {//请求异常
            errorMessage = "网络异常，请检查网络重试";
        } else if (t instanceof UnknownHostException) {//网络异常
            errorMessage = "请求失败，请稍后重试...";
        } else if (t instanceof SocketTimeoutException) {//请求超时
            errorMessage = "请求超时";
        } else if (t instanceof ServerException) {//服务器返回异常
            errorMessage = t.getMessage();
        } else if (t instanceof ConnectException) {
            errorMessage = "网络连接失败";
        } else if (t instanceof JsonSyntaxException) {
            errorMessage = "数据解析失败,联系管理员";
        } else if (t instanceof JSONException) {
            errorMessage = "数据转换失败,联系管理员";
        } else if (t instanceof Exception) {
            errorMessage = "系统异常，管理员正在维护";
        }
        return errorMessage;
    }


    public static String getErrorString(String json) {
        return JSONParser.getStringFromJsonString("error", JSONParser.getStringFromJsonString("datas", json));
    }

    public static String getValue(String key, String json) {
        return JSONParser.getStringFromJsonString(key, json);
    }

    public static <T> T getObject(String json, Class<T> c) {
        return JSONParser.JSON2Object(json, c);
    }

    public static <T> List<T> getObjectList(String json, Class<T> c) {
        return JSONParser.JSON2Array(json, c);
    }

    public static int getCode(String json) {
        return Integer.valueOf(JSONParser.getStringFromJsonString("code", json));
    }

    public static boolean getHasMore(String json) {
        return Boolean.valueOf(JSONParser.getStringFromJsonString("hasmore", json));
    }

    public static int getPageTotal(String json) {
        return Integer.valueOf(JSONParser.getStringFromJsonString("page_total", json));
    }

    public static String getDatasString(String json) {
        return JSONParser.getStringFromJsonString("datas", json);
    }


    public static boolean isUseHealth = false;
    public static boolean isUserPre = false;
    public static boolean isUsePrc = false;
    public static String codetype = "payment_code";
    public static Map<String, String> payment_code = new HashMap<>();
    public static PayData paydata;

    /**
     * 获取可用支付方式
     */
    public static void getPaymentListData(Context context, String pay_sn, final DataCallback callback) {

        Api.post(ApiService.MEMBER_BUY, new FormBody.Builder()
                .add("key", App.getApp().getKey())
                .add("pay_sn", pay_sn)
                .build(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ((Activity) context).runOnUiThread(() -> {
                    showToast(context, Utils.getErrorString(e));
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    if (Utils.getCode(json) == HttpErrorCode.HTTP_NO_ERROR) {
                        ((Activity) context).runOnUiThread(() -> {
                            callback.data(Utils.getDatasString(json));
                        });

                    } else {
                        ((Activity) context).runOnUiThread(() -> {
                            showToast(context, Utils.getErrorString(json));
                        });
                    }
                } catch (Exception e) {
                    ((Activity) context).runOnUiThread(() -> {
                        showToast(context, Utils.getErrorString(e));
                    });
                }
            }
        });

    }

    public static PopupWindow shopPayWindown(Context context, PayData data, String type, View.OnClickListener closelistener, CallBack callBack) {
        paydata = data;
        isUseHealth = false;
        isUsePrc = false;
        isUserPre = false;
        payment_code.put("payment_code", "alipay");
        View view = LayoutInflater.from(context).inflate(R.layout.payway1, null);
        ImageView close = (ImageView) view.findViewById(R.id.close);
        close.setOnClickListener(closelistener);
        View alipay = view.findViewById(R.id.alipay);
        View wxpay = view.findViewById(R.id.wxpay);
        View emptyview = view.findViewById(R.id.emptyview);

        View viewpay1 = view.findViewById(R.id.viewpay1);
        View viewpay2 = view.findViewById(R.id.viewpay2);
        View viewpay3 = view.findViewById(R.id.viewpay3);
        View viewpay4 = view.findViewById(R.id.viewpay4);
        View viewpay5 = view.findViewById(R.id.viewpay5);

        ToggleButton toggle1 = (ToggleButton) view.findViewById(R.id.toggle1);
        ToggleButton toggle2 = (ToggleButton) view.findViewById(R.id.toggle2);
        ToggleButton toggle3 = (ToggleButton) view.findViewById(R.id.toggle3);

        TextView number1 = (TextView) view.findViewById(R.id.number1);
        TextView number2 = (TextView) view.findViewById(R.id.number2);
        TextView number3 = (TextView) view.findViewById(R.id.number3);

        TextView text1 = (TextView) view.findViewById(R.id.text1);
        TextView text2 = (TextView) view.findViewById(R.id.text2);
        TextView text3 = (TextView) view.findViewById(R.id.text3);

        TextView money = (TextView) view.findViewById(R.id.money);

        EditText pwd = (EditText) view.findViewById(R.id.pwd);
        TextView nosetpwd = (TextView) view.findViewById(R.id.nosetpwd);
        money.setText(data.pay_info.pay_amount);
        if (!isUsePrc && !isUserPre && !isUseHealth) {//显示密码框
            viewpay4.setVisibility(View.GONE);
        } else {
            viewpay4.setVisibility(View.VISIBLE);
        }
        if (data.pay_info.member_paypwd) { //是否设置支付密码 显示输入密码框
            nosetpwd.setVisibility(View.GONE);
            pwd.setVisibility(View.VISIBLE);
        } else {
            nosetpwd.setVisibility(View.VISIBLE);
            pwd.setVisibility(View.GONE);
        }
        alipay.setSelected(true);
        boolean isOnlineAli = false;
        boolean isOnlineWx = false;


        boolean isShowPrc = false;
        boolean isShowPre = false;
        boolean isShowHealth = false;


        List<PayData.PayInfo.DataBean> paylist = data.pay_info.payment_list;
        if (data != null && data.pay_info != null && paylist != null && paylist.size() > 0) {
            for (int i = 0; i < paylist.size(); i++) {
                int index = i;
                if (paylist.get(i).payment_code.equals("alipay")) {/**支付宝*/
                    if (paylist.get(i).payment_status.equals("1")) {
                        alipay.setVisibility(View.VISIBLE);
                        isOnlineAli = true;
                    } else {
                        isOnlineAli = false;
                        alipay.setVisibility(View.GONE);
                    }

                    alipay.setOnClickListener(v -> {

                        wxpay.setSelected(false);
                        payment_code.put(codetype, paylist.get(index).payment_code);
                        alipay.setSelected(true);
                    });
                    continue;
                }
                if (paylist.get(i).payment_code.equals("wxpay")) {/**微信*/
                    if (paylist.get(i).payment_status.equals("1")) {
                        isOnlineWx = true;
                        wxpay.setVisibility(View.VISIBLE);
                    } else {
                        isOnlineWx = false;
                        wxpay.setVisibility(View.GONE);
                    }
                    wxpay.setOnClickListener(v -> {
                        alipay.setSelected(false);
                        wxpay.setSelected(true);
                        payment_code.put(codetype, paylist.get(index).payment_code);

                    });
                    continue;
                }

                if (paylist.get(i).payment_code.equals("healthbean_allow")) {/**健康豆*/
                    if (paylist.get(i).payment_status.equals("1")) {
                        viewpay1.setVisibility(View.VISIBLE);
                        isShowHealth = true;
                        number1.setText("可用健康豆余额 ￥" + data.pay_info.member_available_healthbean);
                        text1.setText(paylist.get(i).payment_name);
                        toggle1.setOnToggleChanged(on -> {
                            isUseHealth = on;
                            if (isUsePrc || isUserPre || isUseHealth) {
                                viewpay4.setVisibility(View.VISIBLE);
                            } else {
                                viewpay4.setVisibility(View.INVISIBLE);
                            }
                        });
                        continue;
                    }
                    isShowHealth = false;
                    viewpay1.setVisibility(View.GONE);
                    continue;
                }

                if (paylist.get(i).payment_code.equals("predeposit_allow")) {/**充值卡*/
                    if (paylist.get(i).payment_status.equals("1")) {
                        isShowPrc = true;
                        viewpay2.setVisibility(View.VISIBLE);
                        number2.setText("可用充值卡余额 ￥" + data.pay_info.member_available_pd);
                        text2.setText(paylist.get(i).payment_name);
                        toggle2.setOnToggleChanged(on -> {
                            isUserPre = on;
                            if (isUsePrc || isUserPre || isUseHealth) {
                                viewpay4.setVisibility(View.VISIBLE);
                            } else {
                                viewpay4.setVisibility(View.INVISIBLE);
                            }

                        });
                        continue;
                    }
                    isShowPrc = false;
                    viewpay2.setVisibility(View.GONE);
                    continue;
                }

                if (paylist.get(i).payment_code.equals("rc_balance_allow")) {/**预存款*/
                    if (paylist.get(i).payment_status.equals("1")) {
                        isShowPre = true;
                        viewpay3.setVisibility(View.VISIBLE);
                        number3.setText("可用预存款余额 ￥" + data.pay_info.member_available_rcb);
                        text3.setText(paylist.get(i).payment_name);
                        toggle3.setOnToggleChanged(on -> {
                            isUsePrc = on;
                            if (isUsePrc || isUserPre || isUseHealth) {
                                viewpay4.setVisibility(View.VISIBLE);
                            } else {
                                viewpay4.setVisibility(View.INVISIBLE);
                            }
                        });
                        continue;
                    }
                    isShowPre = false;
                    viewpay3.setVisibility(View.GONE);
                    continue;
                }

            }
        }

        boolean isOnlinePay = false;/**是否存在在线支付*/
        if (!isOnlineWx && !isOnlineAli) {/**如果没有在线支付*/
            viewpay5.setVisibility(View.GONE);
            isOnlinePay = false;

        } else {
            isOnlinePay = true;
            viewpay5.setVisibility(View.VISIBLE);
        }

        boolean isLocaPay = false;/**是否存在站内支付*/
        if (!isShowHealth && !isShowPrc && !isShowPre) {/**如果没有站内支付*/
            isLocaPay = false;
        } else {
            isLocaPay = true;
        }

        Button pay = (Button) view.findViewById(R.id.pay);
        boolean onlinepay = isOnlinePay;
        boolean locapay = isLocaPay;

        pay.setOnClickListener(v -> {
            if (!onlinepay && locapay) {//只有站内支付
                if (!isUsePrc && !isUserPre && !isUseHealth) {
                    showToast(context, "请选择支付方式");
                    return;
                }
                if (!data.pay_info.member_paypwd) {
                    showToast(context, "未设置支付密码");
                    loadMobile(context);
                    return;
                } else {
                    if (isUsePrc || isUserPre || isUseHealth) {
                        if (!Utils.isEmpty(pwd)) {
                            showToast(context, "请输入密码");
                            return;
                        }

                    }
                }
                CheackPassword(context, Utils.getEditViewText(pwd), type);
            } else if (onlinepay && !locapay) {//只有在线支付
                getData(context, "", type);
            } else if (onlinepay && locapay) {//两种都有
                if (isUsePrc || isUserPre || isUseHealth) {
                    if (!data.pay_info.member_paypwd) {
                        showToast(context, "未设置支付密码");
                        loadMobile(context);
                        return;
                    } else {
                        if (!Utils.isEmpty(pwd)) {
                            showToast(context, "请输入密码");
                            return;
                        }

                    }
                    CheackPassword(context, Utils.getEditViewText(pwd), type);
                    return;
                }
                getData(context, "", type);
            }
        });

        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOnDismissListener(() -> {
            WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
            lp.alpha = 1; //0.0-1.0
            ((Activity) context).getWindow().setAttributes(lp);
        });
        //设置可以获取焦点，否则弹出菜单中的EditText是无法获取输入的
        popupWindow.setFocusable(true);
        //防止虚拟软键盘被弹出菜单遮住
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        if (!popupWindow.isShowing())
            popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = 0.4f; //0.0-1.0
        ((Activity) context).getWindow().setAttributes(lp);
        return popupWindow;
    }

    /**
     * 验证支付密码
     *
     * @param password 支付密码
     */
    public static void CheackPassword(Context context, final String password, String type) {
        Api.post(ApiService.CHECK_PASSWORD,
                new FormBody.Builder()
                        .add("key", App.getApp().getKey())
                        .add("password", password)
                        .build(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        ((Activity) context).runOnUiThread(() -> {
                            showToast(context, Utils.getErrorString(e));
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String json = response.body().string();
                        try {
                            if (Utils.getCode(json) == HttpErrorCode.HTTP_NO_ERROR) {
                                if (Utils.getDatasString(json).equals("1")) {
                                    getData(context, password, type);
                                } else if (json.equals("2")) {
                                    loadMobile(context);
                                }
                            } else {
                                ((Activity) context).runOnUiThread(() -> {
                                    showToast(context, Utils.getErrorString(json));
                                });
                            }
                        } catch (Exception e) {
                            ((Activity) context).runOnUiThread(() -> {
                                showToast(context, Utils.getErrorString(e));
                            });
                        }
                    }
                });
    }

    private static void getData(Context context, String pwd, String type) {


        Api.get(ApiService.PAY_NEW +
                        "&key=" + App.getApp().getKey() +
                        "&pay_sn=" + paydata.pay_info.pay_sn +
                        "&password=" + pwd +
                        "&rcb_pay=" + (isUsePrc ? "1" : "0") +
                        "&pd_pay=" + (isUserPre ? "1" : "0") +
                        "&healthbean_pay=" + (isUseHealth ? "1" : "0") +
                        "&payment_code=" + payment_code.get(codetype) +
                        "&client=" + "android" +
                        "&random=" + new Random().nextInt(10),

                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        ((Activity) context).runOnUiThread(() -> {
                            showToast(context, Utils.getErrorString(e));
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String json = response.body().string();
                        try {
                            if (Utils.getCode(json) == HttpErrorCode.HTTP_NO_ERROR) {
                                PayInfos infos = getObject(Utils.getDatasString(json), PayInfos.class);
                                if (infos.payment_complete.equals("true")) {
                                    if (infos.payment_code.equals("alipay")) {
//                                loadingAlipayNativePaymentData(context, infos.pay_sn, "1", obj -> {
//                                    LogUtils.i(obj);
//                                });
                                        payData(context, infos.pay_sn, "alipay", type);
                                    } else if (infos.payment_code.equals("wxpay")) {
//                                loadingWXPaymentData(context, infos.pay_sn, "1");
                                        if (isWxAppInstalledAndSupported(context)) {
                                            payData(context, infos.pay_sn, "wxpay", type);
                                        } else {
                                            showToast(context, "请安装微信客户端");
                                        }
                                    }
                                } else {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(Constants.ORDERINDEX, 0);
                                    Intent intent = new Intent(context, OrderActivity.class);
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                    ((Activity) context).finish();
                                }
                            } else {
                                ((Activity) context).runOnUiThread(() -> {
                                    showToast(context, Utils.getErrorString(json));
                                });
                            }
                        } catch (Exception e) {
                            ((Activity) context).runOnUiThread(() -> {
                                showToast(context, Utils.getErrorString(e));
                            });
                        }
                    }
                });
    }

    private static boolean isWxAppInstalledAndSupported(Context context) {
        IWXAPI wxApi = WXAPIFactory.createWXAPI(context, null);
        wxApi.registerApp(Constants.WX_APP_ID);
        return wxApi.isWXAppInstalled() && wxApi.isWXAppInstalled();
    }

//      Api.post(ApiService.GET_MOBILE_INFO, new FormBody.Builder().add("key", App.getApp().getKey()).build(), new Callback() {
//        @Override
//        public void onFailure(Call call, IOException e) {
//            ((Activity) context).runOnUiThread(() -> {
//                showToast(context, Utils.getErrorString(e));
//            });
//        }
//
//        @Override
//        public void onResponse(Call call, Response response) throws IOException {
//            String json = response.body().string();
//            try {
//                if (Utils.getCode(json) == HttpErrorCode.HTTP_NO_ERROR) {
//
//                } else {
//                    ((Activity) context).runOnUiThread(() -> {
//                        showToast(context, Utils.getErrorString(json));
//                    });
//                }
//            } catch (Exception e) {
//                ((Activity) context).runOnUiThread(() -> {
//                    showToast(context, Utils.getErrorString(e));
//                });
//            }
//        }
//    });

    /**
     * 获取绑定手机信息
     */
    public static void loadMobile(Context context) {
        Api.post(ApiService.GET_MOBILE_INFO, new FormBody.Builder().add("key", App.getApp().getKey()).build(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ((Activity) context).runOnUiThread(() -> {
                    showToast(context, Utils.getErrorString(e));
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    if (Utils.getCode(json) == HttpErrorCode.HTTP_NO_ERROR) {
                        if (Boolean.valueOf(Utils.getValue("state", Utils.getDatasString(json)))) {
                            loadPaypwdInfo(context);
                        } else {
                            new AlertDialog.Builder(context)
                                    .setTitle("提示")
                                    .setMessage("为保证您的资金安全，请先绑定手机号码后，再设置支付密码")
                                    .setPositiveButton("绑定", (dialog, which) -> {
                                        dialog.dismiss();
//                                        ((Activity) context).startActivityForResult(
//                                                new Intent(context, BindMobileActivity.class)
//                                                        .putExtra("type", Constants.SETTINGPWD),
//                                                Constants.RESULT_FLAG_BIND_MOBILE);
                                    })
                                    .setNegativeButton("暂不", ((dialog, which) -> {
                                        dialog.dismiss();
                                    }))
                                    .create().show();
                        }
                    } else {
                        ((Activity) context).runOnUiThread(() -> {
                            showToast(context, Utils.getErrorString(json));
                        });
                    }
                } catch (Exception e) {
                    ((Activity) context).runOnUiThread(() -> {
                        showToast(context, Utils.getErrorString(e));
                    });
                }
            }
        });
    }

    /**
     * 是否设置支付密码信息
     */
    public static void loadPaypwdInfo(Context context) {

        Api.post(ApiService.GET_PAYPWD_INFO, new FormBody.Builder().add("key", App.getApp().getKey()).build(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ((Activity) context).runOnUiThread(() -> {
                    showToast(context, Utils.getErrorString(e));
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    if (Utils.getCode(json) == HttpErrorCode.HTTP_NO_ERROR) {
//                    String password = editPasswordID.getText().toString().trim();
//                            CheackPassword(context,password);
                    } else {
                        ((Activity) context).runOnUiThread(() -> {
                            showToast(context, Utils.getErrorString(json));
                        });
                    }
                } catch (Exception e) {
                    ((Activity) context).runOnUiThread(() -> {
                        showToast(context, Utils.getErrorString(e));
                    });
                }
            }
        });


//
//        HashMap<String, String> params = new HashMap<>();
//        params.put("key", MyShopApplication.getInstance().getLoginKey());
//        RemoteDataHandler.asyncLoginPostDataString(Constants.URL_MEMBER_ACCOUNT_GET_PAYPWD_INFO, params, MyShopApplication.getInstance(), new RemoteDataHandler.Callback() {
//            @Override
//            public void dataLoaded(ResponseData data) {
//                String json = data.getJson();
//                if (data.getCode() == HttpStatus.SC_OK) {
//                    try {
//                        JSONObject object = new JSONObject(json);
//                        if (object.optBoolean("state")) {  //设置了密码 直接验证
//                            String password = editPasswordID.getText().toString().trim();
//                            CheackPassword(password);
//                        } else { //没有设置密码
//                            CustomDialog.Builder builder = new CustomDialog.Builder(context);
//                            builder.setTitle("提示")
//                                    .setMessage("请设置支付密码")
//                                    .setPositiveButton("设置", (dialog, which) -> {
//                                        dialog.dismiss();
//                                        Intent intent = new Intent(context, ModifyPaypwdStep1Activity.class);
////                                        intent.putExtra("mobile", mobile);
//                                        intent.putExtra("type", Constants.SETTINGPWD);
//                                        ((Activity) context).startActivity(intent);
//                                    })
//                                    .setNegativeButton("暂不", ((dialog, which) -> {
//                                        dialog.dismiss();
//                                    }))
//                                    .create().show();
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    ShopHelper.showApiError(context, json);
//                }
//            }
//        });
    }


//    /**
//     * 通过上传的文件的完整路径生成RequestBody
//     * @param fileNames 完整的文件路径
//     * @return
//     */
//    private static RequestBody getRequestBody(List<String> fileNames) {
//        //创建MultipartBody.Builder，用于添加请求的数据
//        MultipartBody.Builder builder = new MultipartBody.Builder();
//        for (int i = 0; i < fileNames.size(); i++) { //对文件进行遍历
//            File file = new File(fileNames.get(i)); //生成文件
//            //根据文件的后缀名，获得文件类型
//            String fileType = getMimeType(file.getName());
//            builder.addFormDataPart( //给Builder添加上传的文件
//                    "image",  //请求的名字
//                    file.getName(), //文件的文字，服务器端用来解析的
//                    RequestBody.create(MediaType.parse(fileType), file) //创建RequestBody，把上传的文件放入
//            );
//        }
//        return builder.build(); //根据Builder创建请求
//    }
//    /**
//     * 获得Request实例
//     * @param url
//     * @param fileNames 完整的文件路径
//     * @return
//     */
//    private static Request getRequest(String url, List<String> fileNames) {
//        Request.Builder builder = new Request.Builder();
//        builder.url(url)
//                .post(getRequestBody(fileNames));
//        return builder.build();
//    }
//    /**
//     * 根据url，发送异步Post请求
//     * @param url 提交到服务器的地址
//     * @param fileNames 完整的上传的文件的路径名
//     * @param callback OkHttp的回调接口
//     */
//    public static void upLoadFile(String url,List<String> fileNames,Callback callback){
//        OkHttpClient okHttpClient = new OkHttpClient();
//        Call call = okHttpClient.newCall(getRequest(url,fileNames)) ;
//        call.enqueue(callback);
//    }

}
