package com.guohanhealth.shop.custom;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by snm on 2016/6/1.
 */
public class MyWebView extends WebView {

    private OnScrollListener onScrollListener;
    /**
     * 主要是用在用户手指离开MyScrollView，MyScrollView还在继续滑动，我们用来保存Y的距离，然后做比较
     */
    private int lastScrollY;

    //手指向右滑动时的最小速度
    private static final int XSPEED_MIN = 200;

    //手指向右滑动时的最小距离
    private static final int XDISTANCE_MIN = 150;

    //记录手指按下时的横坐标。
    private float xDown;

    //记录手指移动时的横坐标。
    private float xMove;

    //用于计算手指滑动的速度。
    private VelocityTracker mVelocityTracker;

    Context mContext;

    private OnScrollToTopListener onScrollToTop; //判断是否滑动到顶部了
    public MyWebView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        init();
    }

    /**
     * 设置滚动接口
     * @param onScrollListener
     */
    public void setOnScrollListener(OnScrollListener onScrollListener){
        this.onScrollListener = onScrollListener;
    }
    /**
     * 用于用户手指离开MyScrollView的时候获取MyScrollView滚动的Y距离，然后回调给onScroll方法中
     */
    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            int scrollY = MyWebView.this.getScrollY();

            //此时的距离和记录下的距离不相等，在隔5毫秒给handler发送消息
            if(lastScrollY != scrollY){
                lastScrollY = scrollY;
                handler.sendMessageDelayed(handler.obtainMessage(), 5);
            }
            if(onScrollListener != null){
                onScrollListener.onScroll(scrollY);
            }

        };

    };

    private void init() {

//        setScrollBarStyle(0);// 设置滚动条的宽度
//        getSettings().setJavaScriptEnabled(true);
//        setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        getSettings().setDefaultTextEncodingName("UTF-8");
//        getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//        getSettings().setBuiltInZoomControls(false);
//        getSettings().setSupportMultipleWindows(true);
//        getSettings().setUseWideViewPort(true);
//        getSettings().setLoadWithOverviewMode(true);
//        getSettings().setSupportZoom(false);
//        getSettings().setPluginState(WebSettings.PluginState.ON);
//        getSettings().setDomStorageEnabled(true);
//        getSettings().setLoadsImagesAutomatically(true);
//
//        //设置webView适应屏幕显示
//        getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        WebSettings webSettings=getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);  //默认模式、读缓存可用并不过时，否则网络请求
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setDisplayZoomControls(false);//不显示缩放按钮
        setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);//不显示滚动条
        setWebViewClient(new WebViewClient());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(onScrollListener != null){
            onScrollListener.onScroll(lastScrollY = this.getScrollY());
        }
        createVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handler.sendMessageDelayed(handler.obtainMessage(), 20);
                xDown = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                xMove = event.getRawX();
                //活动的距离
                int distanceX = (int) (xMove - xDown);
                //获取顺时速度
                int xSpeed = getScrollVelocity();
                //当滑动的距离大于我们设定的最小距离且滑动的瞬间速度大于我们设定的速度时，返回到上一个activity
                // if(distanceX > XDISTANCE_MIN && xSpeed > XSPEED_MIN) {
                if(distanceX > XDISTANCE_MIN) {
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                recycleVelocityTracker();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 创建VelocityTracker对象，并将触摸content界面的滑动事件加入到VelocityTracker当中。
     *
     * @param event
     *
     */
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 回收VelocityTracker对象。
     */
    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    /**
     * 获取手指在content界面滑动的速度。
     *
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        return Math.abs(velocity);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if(scrollY <= 100 && null != onScrollToTop){
            onScrollToTop.onScrollTopListener(true);
        }else{
            onScrollToTop.onScrollTopListener(false);
        }
    }


    /**
     * 滚动的回调接口
     */
    public interface OnScrollListener{
        /**
         * 回调方法， 返回MyScrollView滑动的Y方向距离
         */
        public void onScroll(int scrollY);
    }
    public interface OnScrollToTopListener{
        public void onScrollTopListener(boolean isTop);
    }
    public void setOnScrollToTopLintener(OnScrollToTopListener listener){
        onScrollToTop = listener;
    }
}
