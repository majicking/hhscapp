package com.guohanhealth.shop.event;

public interface CallBack<T> {
    void success(T t);

    void error(String error);
}
