package com.lessu.xieshi.scan;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.lessu.xieshi.AppApplication;
import com.lessu.xieshi.Utils.LogUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by fhm on 2016/12/7.
 */

public  class Wenjian {

    public static String gettxtname() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return df.format(new Date());
    }

    private static FileOutputStream fop;
    private static FileOutputStream fopauto;


    public static void baocunauto(ArrayList<String> Tal,ArrayList<String> Xal){
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/XIESHI");
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
            //fopauto.write("tiaoma".getBytes());
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
        }
    }



    public static void baocun(ArrayList<String> Tal,ArrayList<String> Xal){
        //if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
        //{

            //Toast.makeText(AppApplication.getAppContext(),"无法保存",Toast.LENGTH_SHORT).show();
       // }else {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/XIESHI");
            if (!file.exists()) {
                file.mkdirs();
            }
            try {
                File cacheDirectory = new File(file, gettxtname() + ".txt");
                Toast.makeText(AppApplication.getAppContext(),cacheDirectory.toString(),Toast.LENGTH_SHORT).show();
                System.out.println(cacheDirectory);

                fop = new FileOutputStream(cacheDirectory);
                LogUtil.showLogD("一开始写入文件222222");
                if (!cacheDirectory.exists()) {
                    LogUtil.showLogD("一开始写入文件333333");
                    cacheDirectory.createNewFile();
                }
                System.out.println("写入文件");
                //fop.write("tiaoma".getBytes());
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
            }
        //}

    }


    public static String duqv(Context context,File file){
        StringBuffer sb=new StringBuffer();
        String encoding="GBK";
        try {
            if(file.isFile() && file.exists()){
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    sb.append(lineTxt);
                }
                read.close();
            }else{
                Toast.makeText(context,"找不到指定的文件",Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(context,"读取文件内容出错",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return sb.toString();
    }





}
