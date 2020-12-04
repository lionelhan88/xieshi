package com.lessu.xieshi.Utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.lessu.xieshi.base.AppApplication;
import com.lessu.xieshi.Utils.LogUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by fhm on 2016/12/7.
 */

public  class FileUtil {

    public static String getTxtName() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);//设置日期格式
        return df.format(new Date());
    }

    /**
     * 缓存当前扫描的条码或读取的芯片
     * @param Tal
     * @param Xal
     */
    public static void baocunauto(ArrayList<String> Tal,ArrayList<String> Xal){
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/XIESHI");
        FileOutputStream fopauto = null;
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            File autosavefile = new File(file, "autosave.txt");
            System.out.println(autosavefile);

            fopauto = new FileOutputStream(autosavefile);

            LogUtil.showLogD("一开始写入文件222222");
            if (!autosavefile.exists()) {
                LogUtil.showLogD("一开始写入文件333333");
                autosavefile.createNewFile();
            }
            LogUtil.showLogD("写入文件");
            for (int i = 0; i < Tal.size(); i++) {
                byte[] contentInBytes = Tal.get(i).getBytes();
                fopauto.write(contentInBytes);
                fopauto.write(",".getBytes());
                fopauto.flush();
            }

            fopauto.write("xinpian".getBytes());
            for (int i = 0; i < Xal.size(); i++) {
                byte[] contentInBytes = Xal.get(i).getBytes();
                fopauto.write(contentInBytes);
                fopauto.write(",".getBytes());
                fopauto.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                assert fopauto != null;
                fopauto.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存扫码后的条文和读取的芯片内容
     * @param Tal 条码内容
     * @param Xal 芯片内容
     */
    public static void saveScanFile(ArrayList<String> Tal, ArrayList<String> Xal) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/XIESHI");
        FileOutputStream fop=null;
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            File cacheDirectory = new File(file, getTxtName()+".txt");
            Toast.makeText(AppApplication.getAppContext(), cacheDirectory.toString(), Toast.LENGTH_SHORT).show();
            System.out.println(cacheDirectory);

            fop = new FileOutputStream(cacheDirectory);
            LogUtil.showLogD("一开始写入文件222222");
            if (!cacheDirectory.exists()) {
                LogUtil.showLogD("一开始写入文件333333");
                cacheDirectory.createNewFile();
            }
            System.out.println("写入文件");
            for (int i = 0; i < Tal.size(); i++) {
                byte[] contentInBytes = Tal.get(i).getBytes();
                fop.write(contentInBytes);
                fop.write(",".getBytes());
                fop.flush();
            }

            fop.write("xinpian".getBytes());
            for (int i = 0; i < Xal.size(); i++) {
                byte[] contentInBytes = Xal.get(i).getBytes();
                fop.write(contentInBytes);
                fop.write(",".getBytes());
                fop.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                assert fop != null;
                fop.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取指定文件的内容
     * @param context
     * @param file
     * @return
     */
    public static String readFile(Context context, File file) {
        StringBuilder sb = new StringBuilder();
        String encoding = "GBK";
        try {
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    sb.append(lineTxt);
                }
                read.close();
            } else {
                Toast.makeText(context, "找不到指定的文件", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "读取文件内容出错", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 获取文件的uri
     * @param context
     * @param path
     * @return
     */
    public static Uri getFileUri(Context context,String path){
        File file = new File(path);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context,
                    context.getPackageName() + ".fileProvider",
                    file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }
}
