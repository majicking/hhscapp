package com.majick.hhscapp.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.majick.hhscapp.app.App;
import com.majick.hhscapp.app.AppConfig;
import com.majick.hhscapp.app.Constants;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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
//        initHeadInterceptor();
//        initRequestInterceptor();
//        initLoggingInterceptor();
//        initCachePathAndSize();
//        initCacheTime();
        initOkHttpClient();
        initRetrofit();
    }

    private static class ApiHolder {
        static Api instance = new Api();
    }

    /**
     * 配置全局的头部信息
     */
    private void initHeadInterceptor() {

        mHeadInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .build();
                return chain.proceed(request);
            }

        };
    }

    /**
     * 配置网络请求缓存路径与大小
     */
    private void initCachePathAndSize() {
        File cacheFile = new File(App.getAppContext().getCacheDir(), "cache");
        mCache = new Cache(cacheFile, FILE_CACHE_SIZE);
    }

    /**
     * 配置全局请求参数
     */
    private void initRequestInterceptor() {

        mRequestInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request()
                        .newBuilder();
//                if (headers != null && headers.size() > 0) {
//                    Set<String> keys = headers.keySet();
//                    for (String headerKey : keys) {
//                        builder.addHeader(headerKey, headers.get(headerKey)).build();
//                    }
//                }
                return chain.proceed(builder.build());
            }
        };

//        mRequestInterceptor = chain -> {
//            HttpUrl.Builder builder = chain.request().url().newBuilder();
//
//            if (AppConfig.getLoginBean() != null) {//配置全局token
//                String key = AppConfig.getLoginBean().key;
//                builder.setEncodedQueryParameter("key", key);
//            }
//            Request request = chain.request().newBuilder()
//                    .url(builder.build())
//                    .build();
//            return chain.proceed(request);
//        };

    }

    /**
     * 配置网络请求日志打印
     */
    private void initLoggingInterceptor() {
        mHttpLoggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

//    /**
//     * 配置网络请求缓存路径与大小
//     */
//    private void initCachePathAndSize() {
//        File cacheFile = new File(App.getAppContext().getCacheDir(), "cache");
//        mCache = new Cache(cacheFile, FILE_CACHE_SIZE);
//    }

    /**
     * 配置缓存时间
     */
    private void initCacheTime() {
        mCacheInterceptor = chain -> {
            String originalCacheString = chain.request().cacheControl().toString();
            Response response = initResponseCacheTime(chain.proceed(chain.request()), originalCacheString);
            return response;
        };
    }


    /**
     * 配置http响应的缓存时间 有网就根据之前设置的缓存时间去拿缓存，没网就拿之前的缓存
     *
     * @param originalResponse    响应
     * @param originalCacheString 请求缓存设置
     * @return
     */
    private Response initResponseCacheTime(Response originalResponse, String originalCacheString) {
//        if (NetWorkUtils.isNetworkConnected(YZApplication.getAppContext())) {
//            return originalResponse.newBuilder()
//                    .header("Cache-Control", originalCacheString)
//                    .removeHeader("Pragma")
//                    .build();
//        } else {
//            return originalResponse.newBuilder()
//                    .header("Cache-Control", "public, only-if-cached, max-stale=" + FILE_CACHE_STALE)
//                    .build();
//        }
        return null;
    }

    private static final int DEFAULT_TIMEOUT = 5000;

    /**
     * 配置okHttp
     */
    private void initOkHttpClient() {
        mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
                .cache(mCache)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader("Content-Type", "application/json").build();
                        return chain.proceed(request);
                    }
                })
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request builder = chain.request()
                                .newBuilder().build();
//                if (headers != null && headers.size() > 0) {
//                    Set<String> keys = headers.keySet();
//                    for (String headerKey : keys) {
//                        builder.addHeader(headerKey, headers.get(headerKey)).build();
//                    }
//                }
                        return chain.proceed(builder);
                    }
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

}
