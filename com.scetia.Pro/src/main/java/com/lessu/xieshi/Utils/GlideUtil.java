package com.lessu.xieshi.Utils;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.lessu.xieshi.R;

/**
 * created by ljs
 * on 2020/10/27
 */
public class GlideUtil {

    /**
     * 加载图片不需要缓存，不需要设置错误图片
     * @param view
     * @param placeHolder
     * @param url
     * @param imgeview
     */
    public static void showImageViewNoCache(View view, int placeHolder,String url,
                                            ImageView imgeview) {
        Glide.with(view).load(url)// 加载图片
                .skipMemoryCache(true)//跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过硬盘缓存
                .placeholder(placeHolder)// 设置占位图
                .into(imgeview);
    }
    /**
     * 加载图片不需要缓存，不需要占位图
     * @param view
     * @param imgBytes 加载图片的二进制
     * @param imgeview
     */
    public static void showImageViewNoCache(View view, int placeHolder, byte[] imgBytes,
                                            ImageView imgeview) {
        Glide.with(view).load(imgBytes)// 加载图片
                .placeholder(placeHolder)
                .skipMemoryCache(true)//跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过硬盘缓存
                .into(imgeview);
    }
    /**
     * 加载图片不需要缓存，不需要占位图
     * @param view
     * @param uri
     * @param imgeview
     */
    public static void showImageViewNoCache(View view, int placeHolder, Uri uri,
                                            ImageView imgeview) {
        Glide.with(view).load(uri)// 加载图片
                .placeholder(placeHolder)
                .skipMemoryCache(true)//跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过硬盘缓存
                .into(imgeview);
    }
    /**
     * 加载图片不需要缓存，不需要占位图
     * @param context
     * @param imgBytes 加载图片的二进制
     * @param imgeview
     */
    public static void showImageViewNoCache(Context context,int placeHolder, byte[] imgBytes,
                                            ImageView imgeview) {
        Glide.with(context).load(imgBytes)// 加载图片
                .placeholder(placeHolder)
                .skipMemoryCache(true)//跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过硬盘缓存
                .into(imgeview);
    }

    /**
     * 加载网络图片
     * @param context
     * @param url
     * @param imgeview
     */
    public static void showImageViewNoCache(Context context, String url,
                                            ImageView imgeview) {
        Glide.with(context).load(url)// 加载图片
                .skipMemoryCache(true)//跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过硬盘缓存
                .into(imgeview);
    }

    public static void showImageView(Context context,String url,ImageView imageView){
        Glide.with(context).load(url)// 加载图片
                .placeholder(imageView.getDrawable())
                .into(imageView);
    }

    /**
     * 加载图片不需要缓存，不需要占位图
     * @param context
     * @param uri
     * @param imgeview
     */
    public static void showImageViewNoCacheCircle(Context context,int placeHolder, Uri uri,
                                            ImageView imgeview) {
        Glide.with(context).load(uri)// 加载图片
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .placeholder(placeHolder)
                .skipMemoryCache(true)//跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过硬盘缓存
                .into(imgeview);
    }
    /**
     * 加载图片不需要缓存，不需要占位图
     * @param context
     * @param url
     * @param imgeview
     */
    public static void showImageViewNoCacheCircle(Context context,int placeHolder, String url,
                                                  ImageView imgeview) {
        Glide.with(context).load(url)// 加载图片
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .placeholder(placeHolder)
                .skipMemoryCache(true)//跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过硬盘缓存
                .into(imgeview);
    }

    public static void showDrawableResourceId(Context context,int resourceId,ImageView imageView){
        Glide.with(context).load(resourceId).into(imageView);
    }

}
