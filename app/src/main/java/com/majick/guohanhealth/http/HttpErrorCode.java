package com.majick.guohanhealth.http;


public class HttpErrorCode {

    /**
     * 请求的服务不存在
     */
    public static final int ERROR_404 = 404;

    /**
     * 系统内部错误
     */
    public static final int ERROR_500 = 500;

    /**
     * 程序内部异常
     */
    public static final int ERROR_998 = 998;

    /**
     * 未知错误
     */
    public static final int ERROR_999 = 999;
    /**
     * 请求成功
     */
    public static final int HTTP_NO_ERROR = 200;
    /**
     * 请求失败
     */
    public static final int ERROR_400 = 400;

    /**
     * 自定义异常
     */
    public static final int USER_NOT_EXIT = 123321;
}
