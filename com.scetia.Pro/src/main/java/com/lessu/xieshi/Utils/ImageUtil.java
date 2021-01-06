package com.lessu.xieshi.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by fhm on 2016/12/14.
 */

public class ImageUtil {
    public static Double getPicSize(int w, int h){
        int ww=1024;
        double be = 0;
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be=w/ww;
        } else if (w < h && h > ww) {//如果高度高的话根据宽度固定大小缩放
            be=h/ww;
        }
        if (be <= 0) be = 1;
        return be;
    }

    /**
     * 先尺寸大小压缩再进行质量压缩
     */
    public static String scaleAndCompress(String srcPath,String outPath){
        Bitmap bitmap = sizeCompress(srcPath, 400, 800);
        //有些手机拍照后会自动旋转90度，这里我们再反转回来
        Bitmap reviewedBitmap = reviewPicRotate(bitmap, srcPath);
        File compress = outBitmapToPath(reviewedBitmap, outPath);
        return compress.getAbsolutePath();
    }


    /**
     * 获取图片文件的信息，是否旋转了90度，如果是则反转
     * @param bitmap 需要旋转的图片
     * @param path   图片的路径
     */
    public static Bitmap reviewPicRotate(Bitmap bitmap,String path){
        int degree = getPicRotate(path);
        if(degree!=0){
            Matrix m = new Matrix();
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            m.setRotate(degree); // 旋转angle度
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,m, true);// 从新生成图片
        }
        return bitmap;
    }

    /**
     * 按照宽高比例缩放图片
     * @param srcPath
     * @param pixWidth
     * @param pixHeight
     * @return
     */
    public static Bitmap sizeCompress(String srcPath, float pixWidth, float pixHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        //只读取宽高，不读入内存当中
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        BitmapFactory.decodeFile(srcPath,options);
        //获取图片的真实宽高
        int width = options.outWidth;
        int height = options.outHeight;
        int be = 1;//表示不缩放
        if(width>pixWidth||height>pixHeight){
            final int heightRatio = Math.round((float)height/pixHeight);
            final int widthRatio = Math.round((float)width/pixWidth);
            be = Math.min(heightRatio, widthRatio);
        }
        if(be<=0){
            be=1;
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = be;
        return BitmapFactory.decodeFile(srcPath, options);
    }

    /**
     * 读取图片文件旋转的角度
     * @param path 图片绝对路径
     * @return 图片旋转的角度
     */
    public static int getPicRotate(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 图片质量压缩,压缩到指定大小以下
     */
    public  static File outBitmapToPath(Bitmap bitmap, String outPath) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //进行质量压缩，sampleSize如果为100表示不压缩
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
        File file;
        if(outPath==null) {
            String fileName = new Date().toString();
            file = new File(Environment.getExternalStorageDirectory(), fileName + ".jpg");
        }else{
            file = new File(outPath);
        }
        if(!file.getParentFile().exists()){
            file.mkdirs();
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(out.toByteArray());
            fos.flush();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            bitmap.recycle();
        }
        return file;
    }
}
