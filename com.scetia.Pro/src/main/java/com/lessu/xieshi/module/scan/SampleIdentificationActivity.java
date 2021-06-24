package com.lessu.xieshi.module.scan;
import android.widget.TextView;
import com.lessu.navigation.NavigationActivity;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.scan.bean.ReceiveSampleInfoBean;
import com.lessu.xieshi.module.scan.util.BluetoothHelper;
import com.lessu.xieshi.module.scan.util.HandleScanData;
import com.lessu.xieshi.utils.ToastUtil;
import com.scetia.Pro.common.Util.SPUtil;
import com.scetia.Pro.network.bean.ExceptionHandle;
import com.scetia.Pro.network.conversion.ResponseObserver;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SampleIdentificationActivity extends NavigationActivity {
    private  HandleScanData handleScanData;
    private TextView tv_rukuchakan;
    private TextView tv_chakan;
    private TextView tv_saomiaobianhao;
    private TextView tv_hetongdengjihao;
    private TextView tv_weituobianhao;
    private TextView tv_yangpinbianhao;
    private TextView tv_biaoshibianhao;
    private TextView tv_baogaobianhao;
    private TextView tv_baojianbianhao;
    private TextView tv_yangpinzhuangtai;
    private TextView tv_jiancejieguo;
    private TextView tv_rukugongchengmingchen;
    private TextView tv_gongchenbuwei;
    private TextView tv_gongchendizhi;
    private TextView tv_suoshuqvxian;
    private TextView tv_jiancezhonglei;
    private TextView tv_jiancexiangmu;
    private TextView tv_rukuyangpiningchen;
    private TextView tv_chanpinbiaozhun;
    private TextView tv_jiancecanshu;
    private TextView tv_guigemingchen;
    private TextView tv_qiangdudengji;
    private TextView tv_shigongdanwei;
    private TextView tv_jianshedanwei;
    private TextView tv_jianlidanwei;
    private TextView tv_jiancedanwei;
    private TextView tv_beianzhenghao;
    private TextView tv_shengchanchangjia;
    private TextView tv_zhizuoriqi;
    private TextView tv_linqi;
    private TextView tv_dengjiriqi;
    private TextView tv_weituoriqi;
    private TextView tv_baogaoriqi;
    private TextView tv_beizhu;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_yangpinshibie;
    }

    @Override
    protected void initView() {
        setTitle("样品识别");
        tv_saomiaobianhao = (TextView) findViewById(R.id.tv_saomiaobianhao);
        tv_hetongdengjihao = (TextView) findViewById(R.id.tv_hetongdengjihao);
        tv_weituobianhao = (TextView) findViewById(R.id.tv_weituobianhao);
        tv_yangpinbianhao = (TextView) findViewById(R.id.tv_yangpinbianhao);
        tv_biaoshibianhao = (TextView) findViewById(R.id.tv_biaoshibianhao);
        tv_baogaobianhao = (TextView) findViewById(R.id.tv_baogaobianhao);
        tv_baojianbianhao = (TextView) findViewById(R.id.tv_baojianbianhao);
        tv_yangpinzhuangtai = (TextView) findViewById(R.id.tv_yangpinzhuangtai);
        tv_jiancejieguo = (TextView) findViewById(R.id.tv_jiancejieguo);
        tv_rukugongchengmingchen = (TextView) findViewById(R.id.tv_rukugongchengmingchen);
        tv_gongchenbuwei = (TextView) findViewById(R.id.tv_gongchenbuwei);
        tv_gongchendizhi = (TextView) findViewById(R.id.tv_gongchendizhi);
        tv_suoshuqvxian = (TextView) findViewById(R.id.tv_suoshuqvxian);
        tv_jiancezhonglei = (TextView) findViewById(R.id.tv_jiancezhonglei);
        tv_jiancexiangmu = (TextView) findViewById(R.id.tv_jiancexiangmu);
        tv_rukuyangpiningchen = (TextView) findViewById(R.id.tv_rukuyangpiningchen);
        tv_chanpinbiaozhun = (TextView) findViewById(R.id.tv_chanpinbiaozhun);
        tv_jiancecanshu = (TextView) findViewById(R.id.tv_jiancecanshu);
        tv_guigemingchen = (TextView) findViewById(R.id.tv_guigemingchen);
        tv_qiangdudengji = (TextView) findViewById(R.id.tv_qiangdudengji);
        tv_shigongdanwei = (TextView) findViewById(R.id.tv_shigongdanwei);
        tv_jianshedanwei = (TextView) findViewById(R.id.tv_jianshedanwei);
        tv_jianlidanwei = (TextView) findViewById(R.id.tv_jianlidanwei);
        tv_jiancedanwei = (TextView) findViewById(R.id.tv_jiancedanwei);
        tv_beianzhenghao = (TextView) findViewById(R.id.tv_beianzhenghao);
        tv_shengchanchangjia = (TextView) findViewById(R.id.tv_shengchanchangjia);
        tv_zhizuoriqi = (TextView) findViewById(R.id.tv_zhizuoriqi);
        tv_linqi = (TextView) findViewById(R.id.tv_linqi);
        tv_dengjiriqi = (TextView) findViewById(R.id.tv_dengjiriqi);
        tv_weituoriqi = (TextView) findViewById(R.id.tv_weituoriqi);
        tv_baogaoriqi = (TextView) findViewById(R.id.tv_baogaoriqi);
        tv_beizhu = (TextView) findViewById(R.id.tv_beizhu);
        tv_rukuchakan = (TextView) findViewById(R.id.tv_rukuchakan);
        tv_chakan = (TextView) findViewById(R.id.tv_chakan);
    }

    @Override
    protected void initData() {
        //通过蓝牙地址，尝试去连接设备
        LSAlert.showProgressHud(this,"正在连接设备...");
        Observable.just(getDeviceAddress())
                .subscribeOn(Schedulers.io())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        BluetoothHelper.getInstance().getBluetoothDevice(getDeviceAddress());
                        //执行连接,获取设备名称
                        String deviceName =BluetoothHelper.getInstance().connectDevice();
                        if(deviceName==null){
                            throw  new ExceptionHandle.ResultException(ExceptionHandle.UNKNOWN,"连接失败，请返回重新尝试连接！");
                        }
                        return deviceName+"连接成功！";
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<String>() {
                    @Override
                    public void success(String message) {
                        LSAlert.dismissProgressHud();
                        ToastUtil.showShort(message);
                        //蓝牙连接成功，开始读取数据
                        startReadData();
                    }

                    @Override
                    public void failure(ExceptionHandle.ResponseThrowable throwable) {
                        ToastUtil.showShort(throwable.message);
                        SPUtil.setSPConfig(SPUtil.BLUETOOTH_DEVICE, "");
                        LSAlert.dismissProgressHud();
                    }
                });
    }
    /**
     * 开始读取数据
     */
    private  void startReadData(){
        handleScanData = new HandleScanData();
        handleScanData.startReadingByNetWork2(new HandleScanData.CallBackListener() {
            @Override
            public void success(ReceiveSampleInfoBean readData) {
                showInfo(readData,readData.getCodeNumber());
            }

            @Override
            public void failure(String msg) {
                String[] split = msg.split(",");
                tv_rukuchakan.setText(split[1]);
                tv_chakan.setText(split[0]);

            }
        });
    }

    private String getDeviceAddress() {
        String deviceAdd = SPUtil.getSPConfig(SPUtil.BLUETOOTH_DEVICE, "");
        //是否存在已经缓存的地址，如果没有缓存的地址，重新获取上一个页面传来的地址
        if (deviceAdd.isEmpty()) {
            deviceAdd = getIntent().getStringExtra("deviceAddress");
            SPUtil.setSPConfig(SPUtil.BLUETOOTH_DEVICE, deviceAdd);
        }
        return deviceAdd;
    }

    /**
     * 显示数据内容
     * @param info ReceiveSampleInfoBean
     * @param codeNumber 条码/芯片编号
     */
    private void showInfo(ReceiveSampleInfoBean info, String codeNumber) {
        //tv_chakan.setText("识别完成");
        //tv_rukuchakan.setText(codeNumber);
        tv_saomiaobianhao.setText(codeNumber);
        tv_hetongdengjihao.setText(info.getContract_SignNo());
        tv_weituobianhao.setText(info.getConSign_ID());
        tv_yangpinbianhao.setText(info.getSample_ID());
        tv_biaoshibianhao.setText(info.getSample_BsId());
        tv_baogaobianhao.setText(info.getReportNumber());
        tv_baojianbianhao.setText(info.getBuildingReportNumber());
        tv_yangpinzhuangtai.setText(info.getSample_Status());
        tv_jiancejieguo.setText(info.getExam_Result());
        tv_rukugongchengmingchen.setText(info.getProjectName());
        tv_gongchenbuwei.setText(info.getProJect_Part());
        tv_gongchendizhi.setText(info.getProjectAddress());
        tv_suoshuqvxian.setText(info.getAreaKey());
        tv_jiancezhonglei.setText(info.getKindName());
        tv_jiancexiangmu.setText(info.getItemName());
        tv_rukuyangpiningchen.setText(info.getSampleName());
        tv_chanpinbiaozhun.setText(info.getSampleJudge());
        tv_jiancecanshu.setText(info.getExam_Parameter_Cn());
        tv_guigemingchen.setText(info.getSpecName());
        tv_qiangdudengji.setText(info.getGradeName());
        tv_shigongdanwei.setText(info.getBuildUnitName());
        tv_jianshedanwei.setText(info.getConstructUnitName());
        tv_jianlidanwei.setText(info.getSuperviseUnitName());
        tv_jiancedanwei.setText(info.getDetectionUnitName());
        tv_beianzhenghao.setText(info.getRecord_Certificate());
        tv_shengchanchangjia.setText(info.getProduce_Factory());
        tv_zhizuoriqi.setText(info.getMolding_Date());
        tv_linqi.setText(info.getAgeTime());
        tv_dengjiriqi.setText(info.getCreateDateTime());
        tv_weituoriqi.setText(info.getDetectonDate());
        tv_baogaoriqi.setText(info.getReportDate());
        tv_beizhu.setText(info.getMemo());
    }

    @Override
    protected void onDestroy() {
        if(handleScanData!=null) {
            handleScanData.stopRead();
        }
        BluetoothHelper.getInstance().closeInputStream();
        BluetoothHelper.getInstance().closeSocket();
        super.onDestroy();
    }
}

