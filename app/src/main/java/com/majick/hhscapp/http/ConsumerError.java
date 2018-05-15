package com.majick.hhscapp.http;



import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.functions.Consumer;



public abstract class ConsumerError<T extends Throwable> implements Consumer<T> {

    @Override
    public void accept(T t) throws Exception {
        String errorMessage = "";
        int errorCode = 0;
        if (t instanceof SocketException) {//请求异常
            errorMessage = "网络异常，请检查网络重试";
        } else if (t instanceof UnknownHostException) {//网络异常
            errorMessage = "请求失败，请稍后重试...";
        } else if (t instanceof SocketTimeoutException) {//请求超时
            errorMessage = "请求超时";
        } else if (t instanceof ServerException) {//服务器返回异常
            errorMessage = t.getMessage();
            errorCode = ((ServerException) t).getErrorCode();
        }
        onError(errorCode, errorMessage);
    }

    public abstract void onError(int errorCode, String message);
}
