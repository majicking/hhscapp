package com.guohanhealth.shop.http;

import java.io.Serializable;

/**
 * Description: 封装服务器返回数据
 */


public class Result<T> implements Serializable {


    /** 状态码:200 | 304 | 404 | 500 */
    public int code;

    public int page_total=1;
    /** 是否有下一页 */
    public boolean hasmore;
    /** JSON格式的字符串 */
    public String json;
    /** 字符串结果 */
    public String result;
    /** 总记录数 */
    public long count;

    public int login = -1;
    public T datas;

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", hasmore=" + hasmore +
                ", json='" + json + '\'' +
                ", result='" + result + '\'' +
                ", count=" + count +
                ", login=" + login +
                ", datas='" + datas + '\'' +
                '}';
    }
}
