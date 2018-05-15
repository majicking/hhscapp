package com.majick.hhscapp.utils.engine;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

/**
 * Author:  andy.xwt
 * Date:    2018/3/5 17:32
 * Description:
 */


public class GlideEngine implements ImageEngine {


    private GlideEngine() {

    }

    public static GlideEngine getInstance() {
        return GlideEngineHolder.instance;
    }

    private static final class GlideEngineHolder {
        private static final GlideEngine instance = new GlideEngine();
    }

    @Override
    public void loadThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
        RequestOptions options = new RequestOptions()
                .placeholder(placeholder)
                .centerCrop()
                .skipMemoryCache(false)
                .override(resize);
        Glide.with(context).asBitmap().load(uri).apply(options).into(imageView);
    }

    @Override
    public void loadGifThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
        RequestOptions options = new RequestOptions()
                .placeholder(placeholder)
                .skipMemoryCache(false)
                .override(resize);
        Glide.with(context).asGif().load(uri).apply(options).into(imageView);
    }

    @Override
    public void loadImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        RequestOptions options = new RequestOptions()
                .priority(Priority.HIGH)
                .skipMemoryCache(false)
                .override(resizeX, resizeY);
        Glide.with(context).asBitmap().load(uri).apply(options).into(imageView);
    }

    @Override
    public void loadImage(Context context, ImageView imageView, Uri uri) {
        RequestOptions options = new RequestOptions()
                .priority(Priority.HIGH)
                .skipMemoryCache(false);
        Glide.with(context).asBitmap().load(uri).apply(options).into(imageView);
    }

    @Override
    public void loadImage(Context context, int resizeX, int resizeY, ImageView imageView, String path) {
        RequestOptions options = new RequestOptions()
                .priority(Priority.HIGH)
                .skipMemoryCache(false)
                .override(resizeX, resizeY);
        Glide.with(context).asBitmap().load(path).apply(options).into(imageView);
    }

    @Override
    public void loadImage(Context context, int resize, Drawable placeholder, ImageView imageView, String path) {
        RequestOptions options = new RequestOptions()
                .priority(Priority.HIGH)
                .skipMemoryCache(false)
                .placeholder(placeholder)
                .override(resize);
        Glide.with(context).asBitmap().load(path).apply(options).into(imageView);
    }

    @Override
    public void loadImage(Context context, int resize, ImageView imageView, String path) {
        RequestOptions options = new RequestOptions()
                .priority(Priority.HIGH)
                .skipMemoryCache(false)
                .override(resize);

        Glide.with(context).asBitmap().load(path).apply(options).into(imageView);
    }

    @Override
    public void loadImage(Context context, int resize, ImageView imageView, Uri uri) {
        RequestOptions options = new RequestOptions()
                .priority(Priority.HIGH)
                .centerCrop()
                .skipMemoryCache(false)
                .override(resize);
        Glide.with(context).asBitmap().load(uri).apply(options).into(imageView);
    }

    @Override
    public void loadImage(Context context, ImageView imageView, String path) {
        Glide.with(context).asBitmap().load(path).into(imageView);
    }

    @Override
    public void loadCircleImage(Context context, int resizeX, int resizeY, Drawable placeholder, ImageView imageView, String path) {
        RequestOptions options = new RequestOptions()
                .priority(Priority.HIGH)
                .placeholder(placeholder)
                .circleCrop()
                .skipMemoryCache(false)
                .override(resizeX, resizeY);
        Glide.with(context).asBitmap().load(path).apply(options).into(imageView);
    }

    @Override
    public void loadCircleImage(Context context, int resize, Drawable placeholder, ImageView imageView, String path) {
        RequestOptions options = new RequestOptions()
                .priority(Priority.HIGH)
                .placeholder(placeholder)
                .circleCrop()
                .skipMemoryCache(false)
                .override(resize);
        Glide.with(context).asBitmap().load(path).apply(options).into(imageView);
    }

    @Override
    public void loadCircleImage(Context context, int resize, int placeHolderId, ImageView imageView, String path) {
        RequestOptions options = new RequestOptions()
                .priority(Priority.HIGH)
                .placeholder(placeHolderId)
                .circleCrop()
                .skipMemoryCache(false)
                .override(resize);
        Glide.with(context).asBitmap().load(path).apply(options).into(imageView);
    }

    @Override
    public void loadCircleImage(Context context, int resize, byte[] data, int placeHolderId, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .priority(Priority.HIGH)
                .placeholder(placeHolderId)
                .circleCrop()
                .skipMemoryCache(false)
                .override(resize);
        Glide.with(context).asBitmap().load(data).apply(options).into(imageView);
    }

    @Override
    public void loadGifImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        RequestOptions options = new RequestOptions()
                .priority(Priority.HIGH)
                .skipMemoryCache(false)
                .override(resizeX, resizeY);
        Glide.with(context).asGif().load(uri).apply(options).into(imageView);
    }

    @Override
    public void loadImageFromFile(Context context, File file, int placeHolderId, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .priority(Priority.HIGH)
                .placeholder(placeHolderId)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false);

        Glide.with(context).load(file).apply(options).into(imageView);
    }

    public void clearMemory(Context context) {
        Glide.get(context).clearMemory();
    }

    @Override
    public boolean supportAnimatedGif() {
        return true;
    }
}
