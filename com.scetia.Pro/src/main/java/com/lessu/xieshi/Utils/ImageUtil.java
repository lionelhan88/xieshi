package com.lessu.xieshi.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.core.content.FileProvider;

import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.mis.activitys.Content;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by fhm on 2016/12/14.
 */

public class ImageUtil {
    public static Double getpicsize(int w,int h){
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
     * 根据图片路径质量压缩图片，并输出到指定的路径
     * @param srcPath
     * @param outPath
     * @param maxSize
     */
    public static void compressAndOutPath(String srcPath,String outPath,int maxSize){
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath);
        compress(reviewPicRotate(bitmap, srcPath),maxSize,outPath);
    }

    /**
     * 先尺寸大小压缩再进行质量压缩
     */
    public static String scaleAndCompress(Context context,String srcPath,String outPath,int maxSize){
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath);
        //有些手机拍照后会自动旋转90度，这里我们再反转回来
        Bitmap reviewedBitmap = reviewPicRotate(bitmap, srcPath);
        Bitmap bitmap1 = scaleCompress(reviewedBitmap, 1080, 1440);
        File compress = compress(bitmap1, maxSize, outPath);
        return compress.getAbsolutePath();
    }

    /**
     * 图片质量压缩,压缩到指定大小以下
     */
    public  static File compress(Bitmap bitmap,int maxSize,String outPath) {
        int sapmleSize = 100;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //进行质量压缩，sampleSize如果为100表示不压缩
        bitmap.compress(Bitmap.CompressFormat.JPEG,sapmleSize,out);
        int size = out.toByteArray().length;
        //判断如果当前大小大于指定大小，则继续压缩
        while (size/1024>maxSize){
            sapmleSize-=20;
            out.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG,sapmleSize,out);
            size = out.toByteArray().length;
        }
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
     * 按照宽高比例缩放图片
     * @param srcPath
     * @param pixWidth
     * @param pixHeight
     * @return
     */
    public static Bitmap ScaleCompress(String srcPath,float pixWidth,float pixHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        //只读取宽高，不读入内存当中
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,options);
        //获取图片的真实宽高
        int w = options.outWidth;
        int h = options.outHeight;
        int be = 1;//表示不缩放
        if(w>h&&w>pixWidth){
            //宽度大，按照宽度比例来压缩
            be = (int) (w/pixWidth);
        }else if(h>w&&h>pixHeight){
            be = (int) (h/pixHeight);
        }
        if(be<=0){
            be=1;
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = be;
        return BitmapFactory.decodeFile(srcPath, options);
    }
    /**
     * 按照宽高比例缩放图片
     * @param pixWidth
     * @param pixHeight
     * @return
     */
    public static Bitmap scaleCompress(Bitmap bitmap,float pixWidth,float pixHeight){
        //获取图片的真实宽高
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        float scaleWidth =  pixWidth/w;
        float scaleHeight =  pixHeight/h;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);
        return Bitmap.createBitmap(bitmap,0,0,w,h,matrix,true);
    }

    /**
     * 根据不同的版本返回不同的uri地址
     * @return
     */
    private static Uri createUri(Context context,File file,Intent intent){
        Uri uri= null;
        //大于7.0时获取uri的方式
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M){
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }else{
            uri = Uri.fromFile(file);
        }
        return uri;
    }
}
