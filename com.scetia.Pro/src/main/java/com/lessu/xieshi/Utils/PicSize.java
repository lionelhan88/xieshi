package com.lessu.xieshi.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by fhm on 2016/12/14.
 */

public class PicSize {
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
     * 图片质量压缩,压缩到指定大小以下
     */
    public  static File compress(Bitmap bitmap,int maxSize,String outPath) throws FileNotFoundException {
        int sapmleSize = 70;
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
        if(!file.isFile()) throw new FileNotFoundException("不是正确的文件路径");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(out.toByteArray());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            bitmap.recycle();
        }
        return file;
    }

    /**
     * 根据图片路径质量压缩图片，并输出到指定的路径
     * @param srcPath
     * @param outPath
     * @param maxSize
     */
    public static void compressAndOutPath(String srcPath,String outPath,int maxSize){
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath);
        try {
            compress(bitmap,maxSize,outPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
        Bitmap compressBitmap = BitmapFactory.decodeFile(srcPath, options);
        return compressBitmap;
    }
}
