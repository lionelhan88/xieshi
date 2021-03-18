package com.scetia.Pro.network.util;

import com.scetia.Pro.network.CommonService;
import com.scetia.Pro.network.base.BaseRetrofitManage;
import com.scetia.Pro.network.bean.ExceptionHandle;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.Pro.network.manage.XSRetrofit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * created by Lollipop
 * on 2021/3/3
 */
public class DownloadUtil {
    public static  void download(String url,String fileName,String savePath,ResponseObserver<File> responseObserver){
        XSRetrofit.getInstance().getService(CommonService.class)
                .download(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, File>() {
                    @Override
                    public File apply(ResponseBody responseBody) throws Exception {
                        InputStream inputStream = responseBody.byteStream();
                        return writeFile(inputStream,fileName,savePath);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseObserver);
                
    }

    private static File writeFile(InputStream inputStream,String fileName,String savePath){
        FileOutputStream outputStream = null;
        byte[] buffer = new byte[1024];
        int length = 0;
        File file = new File(savePath,fileName);
        if(file.exists()){
            file.delete();
        }
        try {
            outputStream = new FileOutputStream(file);
            while ((length=inputStream.read(buffer))!=-1){
                outputStream.write(buffer,0,length);
            }
            outputStream.flush();
            return file;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;


    }
}
