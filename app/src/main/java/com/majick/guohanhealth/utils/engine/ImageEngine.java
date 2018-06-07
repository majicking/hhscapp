package com.majick.guohanhealth.utils.engine;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import java.io.File;

/**
 * Author:  andy.xwt
 * Date:    2018/3/5 17:23
 * Description:
 */


public interface ImageEngine {


    /**
     * 加载缩略图
     *
     * @param context     上下文
     * @param resize      图片大小
     * @param placeholder 占位图
     * @param imageView   图片控件
     * @param uri         加载图片的uri
     */
    void loadThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri);

    /**
     * 加载gif缩略图
     *
     * @param context     上下文
     * @param resize      图片大小
     * @param placeholder 占位图
     * @param imageView   图片控件
     * @param uri         加载图片的uri
     */
    void loadGifThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri);

    /**
     * 加载图片
     *
     * @param context   上下文
     * @param resizeX   图片的宽
     * @param resizeY   图片的高
     * @param imageView 图片控件
     * @param uri       加载图片的uri
     */
    void loadImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri);

    /**
     * 加载图片
     *
     * @param context   上下文
     * @param resize    图片大小
     * @param imageView 图片控件
     * @param uri       加载图片的uri
     */
    void loadImage(Context context, int resize, ImageView imageView, Uri uri);

    /**
     * 加载图片
     *
     * @param context   上下文
     * @param imageView 图片控件
     * @param uri       加载图片的uri
     */
    void loadImage(Context context, ImageView imageView, Uri uri);

    /**
     * 加载图片
     *
     * @param context   上下文
     * @param resizeX   图片的宽
     * @param resizeY   图片的高
     * @param imageView 图片控件
     * @param path      请求地址
     */
    void loadImage(Context context, int resizeX, int resizeY, ImageView imageView, String path);


    /**
     * 加载图片
     *
     * @param context     上下文
     * @param resize      图片大小
     * @param placeholder 占位图
     * @param imageView   图片控件
     * @param path        请求地址
     */
    void loadImage(Context context, int resize, Drawable placeholder, ImageView imageView, String path);

    /**
     * 加载图片
     *
     * @param context   上下文
     * @param resize    图片大小
     * @param imageView 图片控件
     * @param path      请求地址
     */
    void loadImage(Context context, int resize, ImageView imageView, String path);

    /**
     * 加载图片
     *
     * @param context   上下文
     * @param imageView 图片控件
     * @param path      请求地址
     */
    void loadImage(Context context, ImageView imageView, String path);

    /**
     * 加载圆形图片
     *
     * @param context     上下文
     * @param resizeX     图片的宽
     * @param resizeY     图片的高
     * @param placeholder 占位图
     * @param imageView   图片控件
     * @param path        请求地址
     */
    void loadCircleImage(Context context, int resizeX, int resizeY, Drawable placeholder, ImageView imageView, String path);

    /**
     * 加载圆形图片
     *
     * @param context     上下文
     * @param resize      图片大小
     * @param placeholder 占位图
     * @param imageView   图片控件
     * @param path        请求地址
     */
    void loadCircleImage(Context context, int resize, Drawable placeholder, ImageView imageView, String path);

    /**
     * 加载圆形图片
     *
     * @param context       上下文
     * @param resize        图片大小
     * @param placeHolderId 占位图id
     * @param imageView     图片控件
     * @param path          请求地址
     */
    void loadCircleImage(Context context, int resize, @DrawableRes int placeHolderId, ImageView imageView, String path);


    /**
     * 加载圆形图片
     *
     * @param context       上下文
     * @param resize        图片大小
     * @param data          图片字节数组
     * @param placeHolderId 占位图id
     * @param imageView     图片控件
     */
    void loadCircleImage(Context context, int resize, byte[] data, @DrawableRes int placeHolderId, ImageView imageView);

    /**
     * 加载gif缩略图
     *
     * @param context   上下文
     * @param resizeX   图片的宽
     * @param resizeY   图片的高
     * @param imageView 图片控件
     * @param uri       加载图片的uri
     */
    void loadGifImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri);


    /**
     * 加载文件图片
     *
     * @param context   上下文
     * @param file      文件
     * @param imageView
     */
    void loadImageFromFile(Context context, File file, int placeHolderId, ImageView imageView);


    /**
     * 是否支持gif
     */
    boolean supportAnimatedGif();
}
