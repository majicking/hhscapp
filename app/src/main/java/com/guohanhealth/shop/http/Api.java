package com.guohanhealth.shop.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.guohanhealth.shop.app.App;
import com.guohanhealth.shop.app.Constants;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author:  andy.xwt
 * Date:    2017/12/20 10:27
 * Description:
 */


public class Api {


    /**
     * Retrofit 配置相关
     */
    private Retrofit mRetrofit;
    private static ApiService sApiService;
    private OkHttpClient mOkHttpClient;

    private static final int READ_TIME_OUT = 5000;// 读取超过时间 单位:毫秒
    private static final int WRITE_TIME_OUT = 5000;//写超过时间 单位:毫秒

    private Interceptor mHeadInterceptor;//头部拦截
    private Interceptor mCacheInterceptor;//缓存拦截
    private Interceptor mRequestInterceptor;//请求拦截
    private HttpLoggingInterceptor mHttpLoggingInterceptor;//请求日志拦截


    /**
     * 缓存相关
     */
    private Cache mCache;
    private static final int FILE_CACHE_SIZE = 1024 * 1024 * 100;//缓存大小100Mb
    private static final long FILE_CACHE_STALE = 60 * 60 * 24 * 2;//缓存有效期为2天


    private Api() {
        initCachePathAndSize();
        initOkHttpClient();
        initRetrofit();
    }

    private static class ApiHolder {
        static Api instance = new Api();
    }


    /**
     * 配置网络请求缓存路径与大小
     */
    private void initCachePathAndSize() {
        File cacheFile = new File(App.getApp().getCacheDir(), "cache");
        mCache = new Cache(cacheFile, FILE_CACHE_SIZE);
    }

    private static final int DEFAULT_TIMEOUT = 5;


    public static OkHttpClient getOkHttp() {
        return ApiHolder.instance.mOkHttpClient;
    }

    /**
     * 配置okHttp
     */
    private void initOkHttpClient() {
        mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
                .cache(mCache)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)//连接失败后是否重新连接
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("Content-Type", "application/json").build();
                    return chain.proceed(request);
                })
                .addInterceptor(chain -> {
                    Request builder = chain.request()
                            .newBuilder().build();
                    Headers headers = builder.headers();
//                if (headers != null && headers.size() > 0) {
//                    Set<String> keys = headers.keySet();
//                    for (String headerKey : keys) {
//                        builder.addHeader(headerKey, headers.get(headerKey)).build();
//                    }
//                }
                    return chain.proceed(builder);
                })
                .addNetworkInterceptor(chain -> {
                    Request request = chain.request();
                    Response response = chain.proceed(request);
                    int maxAge = 60;
                    return response.newBuilder()
                            .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                            .removeHeader("Cache-Control")
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .build();
                })
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
                .build();
    }

    /**
     * 配置retrofit
     */
    private void initRetrofit() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .create();
        mRetrofit = new Retrofit.Builder()
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constants.URLHEAD)
                .build();
        sApiService = mRetrofit.create(ApiService.class);
    }

    public static ApiService getDefault() {
        return ApiHolder.instance.sApiService;
    }

    public static void get(String url, Callback callback) {
        getOkHttp().newCall(new Request.Builder().url(Constants.URLHEAD+ url).get().build()).enqueue(callback);
    }

    public static void post(String url, RequestBody body, Callback callback) {
        getOkHttp().newCall(new Request.Builder().url(Constants.URLHEAD + url).post(body).build()).enqueue(callback);
    }

}
