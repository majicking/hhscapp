package com.guohanhealth.shop.base;


import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 创建基础程序的管理，实现对所有的activity进行管理
 */

public class BaseAppManager {

    private static BaseAppManager mInstance;

    private static List<Activity> sActivityList = new ArrayList<>();//当前应用的activity集合

    private BaseAppManager() {

    }

    /**
     * 懒汉式只会在两个线程进入创建过程的时候才管同步这个问题
     *
     * @return
     */
    public static BaseAppManager getInstance() {
        if (null == mInstance) {
            synchronized (BaseAppManager.class) {
                if (null == mInstance) {
                    mInstance = new BaseAppManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 返回当前activity的个数
     */
    public int size() {
        return sActivityList.size();
    }

    /**
     * 获得前一个activity
     */
    public synchronized Activity getForwardActivity() {
        return size() > 0 ? sActivityList.get(size() - 1) : null;
    }

    /**
     * 添加相应的activity到相应的集合中
     */
    public synchronized void addActivity(Activity activity) {
        sActivityList.add(activity);
    }

    /**
     * 从集合中移除相应的activity
     */
    public synchronized void removeActivity(Activity activity) {
        if (sActivityList.contains(activity)) {
            sActivityList.remove(activity);
        }
    }

    /**
     * 清除所有的activity
     */
    public synchronized void clear() {
        for (int i = sActivityList.size() - 1; i > -1; i--) {
            Activity activity = sActivityList.get(i);
            removeActivity(activity);
            activity.finish();
            i = sActivityList.size();

        }
    }

    /**
     * 只要最上面的activity
     */
    public synchronized void clearToTop() {
        for (int i = sActivityList.size() - 2; i > -1; i--) {
            Activity activity = sActivityList.get(i);
            removeActivity(activity);
            activity.finish();
            i = sActivityList.size() - 1;
        }
    }
}
