package com.lessu.xieshi.Utils;

import android.graphics.Bitmap;

import com.lessu.xieshi.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;

/**
 * Created by fhm on 2016/9/20.
 */
public class ImageloaderUtil {
    public static DisplayImageOptions imageconfig() {
        DisplayImageOptions options;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.touxiang) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.touxiang)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.touxiang)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                // .displayer(new RoundedBitmapDisplayer(80))//是否设置为圆角，弧度为多少
                .displayer(new CircleBitmapDisplayer())
                .imageScaleType(ImageScaleType.NONE)
                //.displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build();//构建完成
        return options;
    }


    public static DisplayImageOptions imageconfigtianqi() {
        DisplayImageOptions options;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .build();//构建完成
        return options;
    }
    public static DisplayImageOptions MeetingImageOptions() {
        DisplayImageOptions options;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .bitmapConfig(Bitmap.Config.ARGB_8888)//设置图片的解码类型//
                .build();//构建完成
        return options;
    }
}
