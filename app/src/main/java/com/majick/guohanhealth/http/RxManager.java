package com.majick.guohanhealth.http;


import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Description: 用于管理单个presenter生命周期中RxJava相关代码的生命周期处理
 */


public class RxManager {


    /**
     * 管理Observables和Subscribers订阅
     * 注意一旦取消订阅，重新创建对象
     */
    private CompositeDisposable mSerialDisposable = new CompositeDisposable();


    /**
     * 单纯的Observables和Subscribers管理
     */
    public void add(Disposable disposable) {
        mSerialDisposable.add(disposable);
    }


    /**
     * 单个presenter 生命周期结束,取消订阅
     */
    public void clear() {
        mSerialDisposable.dispose();
        mSerialDisposable = new CompositeDisposable();//这里清除绑定的时候需要重新创建对象。
    }

}
