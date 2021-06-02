package com.lessu.xieshi.module.scan.util;

import com.lessu.xieshi.utils.Decrypt;
import com.lessu.xieshi.utils.LongString;
import com.lessu.xieshi.module.scan.bean.ReceiveSampleInfoBean;
import com.scetia.Pro.baseapp.uitls.LogUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import java.io.InputStream;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * created by Lollipop
 * on 2021/4/1
 */
public class HandleScanData {
    private boolean isReading = true;
    private Disposable disposable;
    private String oldData="";

    /**
     * 通过网络查询条码获取芯片编号的内容详情
     * @param callBackListener
     */
    public void startReadingByNetWork(CallBackListener callBackListener) {
        Observable.create((ObservableOnSubscribe<Object>) emitter -> {
            String receiveData = "";
            String receiveDataFirst = "";
            String receiveDataSecond = "";
            InputStream inputStream = BluetoothHelper.getInstance().getInputStream();
            byte[] buffer = new byte[1024];
            byte[] buffer2;
            //这里会一直等待读取
            while (isReading) {
                try {
                    int bytes;
                    do {
                        bytes = inputStream.read(buffer);
                    } while (bytes <= 0);
                    buffer2 = buffer.clone();
                    //清空缓存
                    Arrays.fill(buffer, (byte) 0);
                    //将缓冲区读取的字节数组转为字符串
                    String datas = formatData(buffer2);
                    if (datas.isEmpty()) {
                        continue;
                    }
                    if (datas.matches("[0-9]*")) {
                        //扫描的条形码
                        //这里注意，扫描后读取的蓝牙数据有可能使分两段返回的，所以需要拼接
                        switch (datas.length()) {
                            case 1:
                                //如果读取到的数据是1位，则是头数据
                                receiveDataFirst = datas;
                                receiveData = receiveDataFirst + receiveDataSecond;
                                break;
                            case 9:
                                //如果获取的数据是9位则是尾数据
                                receiveDataSecond = datas;
                                receiveData = receiveDataFirst + receiveDataSecond;
                                break;
                            case 10:
                                //如果获取到的数据是10位则是正常数据
                                receiveData = datas;
                                break;
                        }
                    } else {
                        //芯片
                        String s = LongString.bytes2HexString(buffer2);
                        receiveData = Decrypt.decodeChip(s);
                        //这里也要注意，读取芯片时会多次读取，为了防止多次重复访问服务，这里记录oldData如何和当前
                        //读取的芯片字符串相同，则跳过不再请求网络数据
                        if (oldData.equals(receiveData)) {
                            receiveData = "";
                            receiveDataFirst = "";
                            receiveDataSecond = "";
                            continue;
                        }
                    }
                    LogUtil.showLogE("r===>"+receiveData);
                    if (receiveData != null && receiveData.length() == 10) {
                        String finalReceiveData = receiveData;
                        emitter.onNext("正在识别...");
                        ReceiveSampleInfoBean readData = getReadData(finalReceiveData);
                        if (readData == null) {
                            emitter.onNext("未能获取数据");
                            //旧数据清空
                            oldData = "";
                        } else {
                            readData.setCodeNumber(receiveData);
                            emitter.onNext(readData);
                            //记录旧数据
                            oldData = receiveData;
                        }
                        receiveData = "";
                        receiveDataFirst = "";
                        receiveDataSecond = "";
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Object bean) {
                        if(bean instanceof  String){
                            callBackListener.failure((String) bean);
                        }else if(bean instanceof ReceiveSampleInfoBean){
                            callBackListener.success((ReceiveSampleInfoBean) bean);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    /**
     * 读取数据
     * @param s
     * @return
     */
    private ReceiveSampleInfoBean getReadData(String s){
        String nameSpace1 = "http://tempuri.org/";
        String methodName1 = "CheckSamplesForProject ";
        String endPoint1 = "http://www.scetia.com/Scetia.SampleManage.WS/SampleManagement.asmx";
        String soapAction1= "http://tempuri.org/CheckSamplesForProject ";
        SoapObject soapObject1 = new SoapObject(nameSpace1, methodName1);
        soapObject1.addProperty("coreCodeStr", s);
        SoapSerializationEnvelope envelope1 = new SoapSerializationEnvelope(SoapEnvelope.VER10);
        envelope1.bodyOut = soapObject1;
        envelope1.dotNet = true;
        envelope1.setOutputSoapObject(soapObject1);
        //设置超时时间为40s
        HttpTransportSE transport1 = new HttpTransportSE(endPoint1,40000);
        SoapObject shujvsoap = null;
        try {
            transport1.call(soapAction1, envelope1);
            SoapObject object1 = (SoapObject) envelope1.bodyIn;
            if(object1==null) return null;
            shujvsoap = (SoapObject) object1.getProperty(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        System.out.println("waimian...."+shujvsoap.toString());
        SoapObject soap3=(SoapObject)shujvsoap.getProperty(0);
        ReceiveSampleInfoBean info=new ReceiveSampleInfoBean();
        if(soap3.toString().contains("Contract_SignNo")){
            info.setContract_SignNo(soap3.getProperty("Contract_SignNo").toString());
        }
        if(soap3.toString().contains("ConSign_ID")){
            info.setConSign_ID(soap3.getProperty("ConSign_ID").toString());
        }
        if(soap3.toString().contains("Sample_ID")){
            info.setSample_ID(soap3.getProperty("Sample_ID").toString());
        }
        if(soap3.toString().contains("Sample_BsId")){
            info.setSample_BsId(soap3.getProperty("Sample_BsId").toString());
        }
        if(soap3.toString().contains("；ReportNumber")){
            info.setReportNumber(soap3.getProperty("ReportNumber").toString());
        }
        if(soap3.toString().contains("BuildingReportNumber")){
            info.setBuildingReportNumber(soap3.getProperty("BuildingReportNumber").toString());
        }
        if(soap3.toString().contains("Sample_Status")){
            info.setSample_Status(soap3.getProperty("Sample_Status").toString());
        }
        if(soap3.toString().contains("Exam_Result")){
            info.setExam_Result(soap3.getProperty("Exam_Result").toString());
        }
        if(soap3.toString().contains("ProjectName")){
            info.setProjectName(soap3.getProperty("ProjectName").toString());
        }
        if(soap3.toString().contains("ProJect_Part")){
            info.setProJect_Part(soap3.getProperty("ProJect_Part").toString());
        }
        if(soap3.toString().contains("ProjectAddress")){
            info.setProjectAddress(soap3.getProperty("ProjectAddress").toString());
        }
        if(soap3.toString().contains("AreaKey")){
            info.setAreaKey(soap3.getProperty("AreaKey").toString());
        }
        if(soap3.toString().contains("KindName")){
            info.setKindName(soap3.getProperty("KindName").toString());
        }
        if(soap3.toString().contains("ItemName")){
            info.setItemName(soap3.getProperty("ItemName").toString());
        }
        if(soap3.toString().contains("SampleName")){
            info.setSampleName(soap3.getProperty("SampleName").toString());
        }
        if(soap3.toString().contains("SampleJudge")){
            info.setSampleJudge(soap3.getProperty("SampleJudge").toString());
        }
        if(soap3.toString().contains("Exam_Parameter_Cn")){
            info.setExam_Parameter_Cn(soap3.getProperty("Exam_Parameter_Cn").toString());
        }
        if(soap3.toString().contains("SpecName")){
            info.setSpecName(soap3.getProperty("SpecName").toString());
        }
        if(soap3.toString().contains("GradeName")){
            info.setGradeName(soap3.getProperty("GradeName").toString());
        }
        if(soap3.toString().contains("BuildUnitName")){
            info.setBuildUnitName(soap3.getProperty("BuildUnitName").toString());
        }
        if(soap3.toString().contains("ConstructUnitName")){
            info.setConstructUnitName(soap3.getProperty("ConstructUnitName").toString());
        }
        if(soap3.toString().contains("SuperviseUnitName")){
            info.setBuildUnitName(soap3.getProperty("SuperviseUnitName").toString());
        }
        if(soap3.toString().contains("DetectionUnitName")){
            info.setDetectionUnitName(soap3.getProperty("DetectionUnitName").toString());
        }
        if(soap3.toString().contains("Record_Certificate")){
            info.setRecord_Certificate(soap3.getProperty("Record_Certificate").toString());
        }
        if(soap3.toString().contains("Produce_Factory")){
            info.setProduce_Factory(soap3.getProperty("Produce_Factory").toString());
        }
        if(soap3.toString().contains("Molding_Date")){
            info.setMolding_Date(soap3.getProperty("Molding_Date").toString());
        }
        if(soap3.toString().contains("AgeTime")){
            info.setAgeTime(soap3.getProperty("AgeTime").toString());
        }
        if(soap3.toString().contains("CreateDateTime")){
            info.setCreateDateTime(soap3.getProperty("CreateDateTime").toString());
        }
        if(soap3.toString().contains("DetectonDate")){
            info.setDetectonDate(soap3.getProperty("DetectonDate").toString());
        }
        if(soap3.toString().contains("ReportDate")){
            info.setReportDate(soap3.getProperty("ReportDate").toString());
        }
        if (soap3.toString().contains("Memo")) {
            info.setMemo(soap3.getProperty("Memo").toString());
        }
        return  info;
    }
    public void stopRead(){
        isReading = false;
        disposable.dispose();
    }

    public interface CallBackListener{
        void success(ReceiveSampleInfoBean readData );
        void failure(String msg);
    }

    public static String formatData(byte[] buffer){
        String datas = new String(buffer, 0, buffer.length);
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(datas);
        datas = m.replaceAll("").trim();
        return datas;
    }

}
