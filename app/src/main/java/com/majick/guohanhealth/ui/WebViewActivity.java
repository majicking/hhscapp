package com.majick.guohanhealth.ui;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.majick.guohanhealth.R;
import com.majick.guohanhealth.base.BaseActivity;
import com.majick.guohanhealth.utils.Logutils;
import com.wang.avi.AVLoadingIndicatorView;

import static android.view.KeyEvent.KEYCODE_BACK;

public class WebViewActivity extends BaseActivity {
    private WebView webView;
    private AVLoadingIndicatorView loadingIndicatorView;
    private TextView webtitle;
    private ImageView back;
    private ProgressBar progressBar;
    private View restartview;
    private View mErrorView;
    private TextView close;
    private String url;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initView();
        WebSetting();
        loadWebview();
    }

    String newurl;

    private void initView() {
        url = getIntent().getStringExtra("url");
        webView = (WebView) findViewById(R.id.webView);
        back = (ImageView) findViewById(R.id.back);
        close = (TextView) findViewById(R.id.close);
        back.setOnClickListener(v -> {
            if (webView.canGoBack() && !newurl.equals(url)) {
                webView.goBack();
            } else {
                finish();
            }
        });
        close.setOnClickListener(v -> finish());
        webtitle = (TextView) findViewById(R.id.title);
        progressBar = (ProgressBar) findViewById(R.id.pb);
        loadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.loading);
        restartview = findViewById(R.id.restartview);
        restartview.setOnClickListener(c -> {
            loadWebview();
            restartview.setVisibility(View.GONE);
        });
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
                newurl = url;
                view.loadUrl(url);
                return true;
            }

            //作用：开始载入页面调用的，我们可以设定一个loading的页面，告诉用户程序在等待网络响应
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                loadingIndicatorView.setVisibility(View.VISIBLE);
            }

            //在页面加载结束时调用。我们可以关闭loading 条，切换程序动作。
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!webView.getSettings().getLoadsImagesAutomatically()) {
                    webView.getSettings().setLoadsImagesAutomatically(true);
                }
                progressBar.setVisibility(View.GONE);
                loadingIndicatorView.setVisibility(View.GONE);
            }

            //加载页面的服务器出现错误时（如404）调用。
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

                webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
                restartview.setVisibility(View.VISIBLE);
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
                    progressBar.setVisibility(View.VISIBLE);
                    loadingIndicatorView.setVisibility(View.VISIBLE);
                    try {
//                        Toast.makeText(WebViewActivity.this, progress, Toast.LENGTH_SHORT).show();
                        progressBar.setProgress(newProgress);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    loadingIndicatorView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
            }

            //作用：获取Web页中的标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
//                webtitle.setText(TextUtils.isEmpty(title) ? getResources().getString(R.string.app_name) : title);

            }
        });

    }

    boolean mIsErrorPage;

    protected void showErrorPage() {
        ViewGroup webParentView = (ViewGroup) webView.getParent();
        initErrorPage();//初始化自定义页面
        while (webParentView.getChildCount() > 1) {
            webParentView.removeViewAt(0);
        }
        @SuppressWarnings("deprecation")
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewPager.LayoutParams.FILL_PARENT, ViewPager.LayoutParams.FILL_PARENT);
        webParentView.addView(mErrorView, 0, lp);
        mIsErrorPage = true;
    }

    /****
     * 把系统自身请求失败时的网页隐藏
     */
    protected void hideErrorPage() {
        ViewGroup webParentView = (ViewGroup) webView.getParent();
        mIsErrorPage = false;
        while (webParentView.getChildCount() > 1) {
            webParentView.removeViewAt(0);
        }
    }

    /***
     * 显示加载失败时自定义的网页
     */
    protected void initErrorPage() {
        if (mErrorView == null) {
            mErrorView = View.inflate(this, R.layout.activity_error, null);
            RelativeLayout layout = (RelativeLayout) mErrorView.findViewById(R.id.online_error_btn_retry);
            layout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    webView.reload();
                    hideErrorPage();
                }
            });
            mErrorView.setOnClickListener(null);
        }
    }

    public void loadWebview() {
        if (TextUtils.isEmpty(url)) {
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
                    "数据为空" +
                    "</div>" +
                    "</body>\n" +
                    "</html>";
            webView.loadDataWithBaseURL(null, s, "text/html", "utf-8", null);

        } else {
            webView.loadUrl(url);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
