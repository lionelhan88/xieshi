package com.scetia.Pro.common.photo;

import android.os.Environment;

import androidx.fragment.app.FragmentActivity;

import com.scetia.Pro.common.Util.DateUtil;

import java.lang.ref.SoftReference;
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
    private String photoName;
    private String photoSavePath;
    private boolean isCompress;
    private ImageSelectBottomMenu.ImageSelectListener listener;
    private SoftReference<FragmentActivity> weakReference;
    private XXPhotoUtil(FragmentActivity context){
        weakReference = new SoftReference<>(context);
        //图片默认的名字
         photoName = "XS_"+ DateUtil.FORMAT_UNDERLINE_YMDHMS(new Date());
        //图片默认的存储地址
        photoSavePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath()+"/XIESHI";
    }

    public static XXPhotoUtil with(FragmentActivity context){
        return new XXPhotoUtil(context);
    }


    /**
     * 获取到照片以后的回调
     * @param listener
     * @return
     */
    public XXPhotoUtil setListener(ImageSelectBottomMenu.ImageSelectListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * 开启选择图片,拍照和相册选择同时开启
     */
    public void start(){
        ImageSelectBottomMenu menu =  ImageSelectBottomMenu.newInstance(photoName,photoSavePath, TakePhotoOperation.ALL);
        menu.setCompress(isCompress);
        menu.setImageSelectListener(listener);
        menu.show(weakReference.get().getSupportFragmentManager(),"image_select_menu");
    }

    /**
     * 只打开相机拍照
     */
    public void startCamera(){
        ImageSelectBottomMenu menu =  ImageSelectBottomMenu.newInstance(photoName,photoSavePath, TakePhotoOperation.ONLY_CAMERA);
        menu.setCompress(isCompress);
        menu.setImageSelectListener(listener);
        menu.show(weakReference.get().getSupportFragmentManager(),"image_select_menu");
    }

    /**
     * 只打开相册选择图片
     */
    public void startPictures(){
        ImageSelectBottomMenu menu =  ImageSelectBottomMenu.newInstance(photoName,photoSavePath, TakePhotoOperation.ONLY_PICTURES);
        menu.setCompress(isCompress);
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
        return this;
    }

    /**
     * 自定义图片保存的路径，如果不设置，默认保存在公共目录Pictures/XIESHI目录下
     * @param photoSavePath
     * @return
     */
    public XXPhotoUtil setPhotoSavePath(String photoSavePath) {
        this.photoSavePath = photoSavePath;
        return this;
    }

    public XXPhotoUtil setCompress(boolean compress){
        isCompress = compress;
        return this;
    }
}
