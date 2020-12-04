package com.lessu.xieshi.photo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.DateUtil;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Date;

/**
 * created by ljs
 * on 2020/11/24
 */
public class XXPhotoUtil {
    /**
     * 开启的操作
     */
    enum TakePhotoOperation{
        ONLY_CAMERA,
        ONLY_PICTURES,
        ALL
    }
    public static final int REQUEST_PHOTO_PICTURE = 1000;
    public static final int REQUEST_TAKE_PHOTO=10001;
    private String photoName;
    private String photoSavePath;
    private ImageSelectBottomMenu.ImageSelectListener listener;
    private  static XXPhotoUtil instance;
    private WeakReference<FragmentActivity> weakReference;
    private XXPhotoUtil(FragmentActivity context){
        weakReference = new WeakReference<>(context);
        //图片默认的名字
         photoName = "XS_"+ DateUtil.pictureDate(new Date());
        //图片默认的存储地址
        photoSavePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath()+"/XIESHI";
    }

    public static XXPhotoUtil with(FragmentActivity context){
        instance=new XXPhotoUtil(context);
        return instance;
    }

    /**
     * 获取到照片以后的回调
     * @param listener
     * @return
     */
    public XXPhotoUtil setListener(ImageSelectBottomMenu.ImageSelectListener listener) {
        this.listener = listener;
        return instance;
    }

    /**
     * 开启选择图片,拍照和相册选择同时开启
     */
    public void start(){
        ImageSelectBottomMenu menu =  ImageSelectBottomMenu.newInstance(photoName,photoSavePath,TakePhotoOperation.ALL);
        menu.setImageSelectListener(listener);
        menu.show(weakReference.get().getSupportFragmentManager(),"image_select_menu");
    }

    /**
     * 只打开相机拍照
     */
    public void startCamera(){
        ImageSelectBottomMenu menu =  ImageSelectBottomMenu.newInstance(photoName,photoSavePath,TakePhotoOperation.ONLY_CAMERA);
        menu.setImageSelectListener(listener);
        menu.show(weakReference.get().getSupportFragmentManager(),"image_select_menu");
    }

    /**
     * 只打开相册选择图片
     */
    public void startPictures(){
        ImageSelectBottomMenu menu =  ImageSelectBottomMenu.newInstance(photoName,photoSavePath,TakePhotoOperation.ONLY_PICTURES);
        menu.setImageSelectListener(listener);
        menu.show(weakReference.get().getSupportFragmentManager(),"image_select_menu");
    }

    /**
     * 自定义保存的文件名称，如果不设置，默认为当前的时间
     * @param photoName
     * @return
     */
    public XXPhotoUtil setPhotoName(String photoName) {
        this.photoName = photoName;
        return instance;
    }

    /**
     * 自定义图片保存的路径，如果不设置，默认保存在公共目录Pictures/XIESHI目录下
     * @param photoSavePath
     * @return
     */
    public XXPhotoUtil setPhotoSavePath(String photoSavePath) {
        this.photoSavePath = photoSavePath;
        return instance;
    }
}
