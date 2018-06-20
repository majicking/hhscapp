package com.majick.guohanhealth.http;


import com.majick.guohanhealth.app.App;
import com.majick.guohanhealth.bean.BaseInfo;
import com.majick.guohanhealth.utils.Utils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Description: 网络请求帮助类处理。对请求结果进行了判断，对线程进行了处理
 */


public class RxHelper {

    /**
     * 处理请求结果 针对 有code message data 的json
     * 对请求状态吗进行分析，如果成功获取result 中的data
     */
    public static <T> ObservableTransformer<Result<T>, T> handleResult() {
        return upstream -> upstream.flatMap(result -> {
            if (result.code == HttpErrorCode.HTTP_NO_ERROR) {
                if (result.datas == null) {
                    Class<RxHelper> rxHelperClass = RxHelper.class;
                    Method method = rxHelperClass.getMethod("handleResult");
                    Type[] type = method.getGenericParameterTypes();
                    ParameterizedType t = (ParameterizedType) type[0];
                    Class<T> clazz = (Class) (t.getActualTypeArguments()[0]);//获取泛型class
                    result.datas = clazz.newInstance();
                }
                if (Utils.isEmpty("" + result.login) && "0".equals(result.login)) {
                    App.getApp().setKey("");
                }
                if (Utils.isEmpty(result.hasmore) && result.hasmore.equals("true")) {
                    App.getApp().setHasmore("true");
                } else {
                    App.getApp().setHasmore("false");
                }
                if (Utils.isEmpty("" + result.page_total) && result.page_total > 1) {
                    App.getApp().setPage_total(result.page_total);
                }
                return createSuccessData(result.datas);
            } else if (result.code == HttpErrorCode.ERROR_400) {
                BaseInfo error = null;
                try {
                    error = (BaseInfo) result.datas;
                } catch (Exception e) {
                    return Observable.error(new ServerException(result.code, e.getMessage()));
                }
                return Observable.error(new ServerException(result.code, error.error));
            } else {
                return Observable.error(new ServerException(result.code, "请求异常，请重试"));
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 处理没有data的Result
     */
    public static ObservableTransformer<Result, Result> handleOnlyResult() {
        return upstream -> upstream.flatMap(result -> {
            if (result.code == HttpErrorCode.HTTP_NO_ERROR) {
                return createSuccessData(result);
            } else {
                return Observable.error(new ServerException(result.code, ""));
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 创建成功返回的数据
     */
    private static <T> Observable<T> createSuccessData(final T t) {
        return Observable.create(emitter -> {
            try {
                emitter.onNext(t);
                emitter.onComplete();
            } catch (Exception ex) {
                emitter.onError(ex);
            }
        });
    }
}
