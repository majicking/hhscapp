package com.majick.hhscapp.http;

/**
 * Author:  andy.xwt
 * Date:    2017/6/11 15:27
 * Description: 服务器请求异常
 */


public class ServerException extends Exception {

    private int errorCode;

    public ServerException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}