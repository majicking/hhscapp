package com.majick.guohanhealth.ui.goods.goodsdetailed.fragment.goodsdetail;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.majick.guohanhealth.R;
import com.majick.guohanhealth.app.Constants;
import com.majick.guohanhealth.base.BaseFragment;
import com.majick.guohanhealth.custom.MyWebView;
import com.majick.guohanhealth.event.OnFragmentInteractionListener;
import com.majick.guohanhealth.http.Api;
import com.majick.guohanhealth.http.ApiService;
import com.majick.guohanhealth.http.HttpErrorCode;
import com.majick.guohanhealth.ui.goods.goodsdetailed.activity.GoodsDetailsActivity;
import com.majick.guohanhealth.utils.JSONParser;
import com.majick.guohanhealth.utils.Logutils;
import com.majick.guohanhealth.utils.Utils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class GoodsDetailFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.webView)
    WebView webView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public GoodsDetailFragment() {


    }

    public static GoodsDetailFragment newInstance(String param1, String param2) {
        GoodsDetailFragment fragment = new GoodsDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void WebSetting() {
        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        //优先使用缓存:
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //缓存模式如下：
        //LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        //LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        //LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        //LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。

//        if (SystemHelper.isConnected(getApplicationContext())) {
//            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//根据cache-control决定是否从网络上取数据。
//        } else {
//            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//没网，则从本地获取，即离线加载
//        }

        webSettings.setDomStorageEnabled(true); // 开启 DOM storage API 功能
        webSettings.setDatabaseEnabled(true);   //开启 database storage API 功能
        webSettings.setAppCacheEnabled(true);//开启 Application Caches 功能

//        String cacheDirPath = getFilesDir().getAbsolutePath() + Constants.CACHE_DIR;
//        webSettings.setAppCachePath(cacheDirPath); //设置  Application Caches 缓存目录
        //支持自动加载图片
        if (Build.VERSION.SDK_INT >= 19) {
            webSettings.setLoadsImagesAutomatically(true);
        } else {
            webSettings.setLoadsImagesAutomatically(false);
        }
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //作用：开始载入页面调用的，我们可以设定一个loading的页面，告诉用户程序在等待网络响应
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            //在页面加载结束时调用。我们可以关闭loading 条，切换程序动作。
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!webView.getSettings().getLoadsImagesAutomatically()) {
                    webView.getSettings().setLoadsImagesAutomatically(true);
                }
            }

            //加载页面的服务器出现错误时（如404）调用。
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

                webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
//                showErrorPage();
            }

            //处理https请求
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();    //表示等待证书响应
                // handler.cancel();      //表示挂起连接，为默认方式
                // handler.handleMessage(null);    //可做其他处理
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Logutils.i("pro= " + newProgress);
                if (newProgress < 100) {
                    String progress = newProgress + "%";

                } else {

                }
            }

            //作用：获取Web页中的标题
            @Override
            public void onReceivedTitle(WebView view, String title) {

            }
        });

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_goods_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        WebSetting();
        ((GoodsDetailsActivity) mContext).setOnChangeGoodsInfoListener((type, data) -> {
            if (type.equals("updata_goods_id")) {
//                getData(((GoodsDetailsActivity)mContext).getGoods_id());
                getData(data);
            }

        });
    }

    public Object onButtonPressed(String key, Object value) {
        if (mListener != null) {
            return mListener.doSomeThing(key, value);
        }
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Logutils.i("onResume");
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getData((String) onButtonPressed(Constants.GETGOODSID, ""));
        }
    }

    public void getData(String goods_id) {
        Logutils.i("刷新UI");
        try {
            Api.get(ApiService.GOODS_BODY + "&goods_id=" + goods_id, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Logutils.i(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.code() == HttpErrorCode.HTTP_NO_ERROR) {
                        String data = response.body().string();
                        ((GoodsDetailsActivity) mContext).runOnUiThread(() -> initWebView(data));
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initWebView(String goodsBody) {
        String s = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "\t<style type=\"text/css\">\n" +
                "\t\t.img-ks-lazyload{\n" +
                "\t\t\ttext-align: center;\n" +
                "\t\t}\n" +
                "\t\t.img-ks-lazyload img{\n" +
                "\t\t\twidth: 100% !important;\n" +
                "\t\t}\n" +
                "\t</style>\n" +
                "</head>\n" +
                "<body>" +
                "<div class=\"img-ks-lazyload\">" +
                goodsBody +
                "</div>" +
                "</body>\n" +
                "</html>";
        webView.loadDataWithBaseURL(null, s, "text/html", "utf-8", null);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void faild(String msg) {

    }

}
