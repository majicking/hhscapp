package com.guohanhealth.shop.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.guohanhealth.shop.R;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;
import com.guohanhealth.shop.bean.WxPayInfo;
import com.guohanhealth.shop.custom.CustomPopuWindow;
import com.guohanhealth.shop.event.Functions;
import com.guohanhealth.shop.event.ObjectEvent;
import com.guohanhealth.shop.event.RxBus;
import com.guohanhealth.shop.http.Api;
import com.guohanhealth.shop.http.ApiService;
import com.guohanhealth.shop.http.HttpErrorCode;
import com.guohanhealth.shop.http.Result;
import com.guohanhealth.shop.http.ServerException;
import com.guohanhealth.shop.ui.WebViewActivity;
import com.guohanhealth.shop.ui.goods.goodsdetailed.activity.GoodsDetailsActivity;
import com.guohanhealth.shop.ui.login.LoginActivity;
import com.guohanhealth.shop.ui.main.spec.SpecActivity;
import com.guohanhealth.shop.ui.search.SearchActivity;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;

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


}
