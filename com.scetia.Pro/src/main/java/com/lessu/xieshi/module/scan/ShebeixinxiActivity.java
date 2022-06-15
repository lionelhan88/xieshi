package com.lessu.xieshi.module.scan;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.lessu.xieshi.base.AppApplication;
import com.lessu.xieshi.R;
import com.raylinks.Function;
import com.raylinks.ModuleControl;
import com.scetia.Pro.common.Util.SPUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class ShebeixinxiActivity extends AppCompatActivity {
    private static ModuleControl moduleControl = new ModuleControl();
    Function fun = new Function();
    private byte flagCrc;
    private TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shebeixinxi);
        TextView tv = (TextView) findViewById(R.id.tv);
        tv1 = (TextView) findViewById(R.id.tv1);
        Intent intent=getIntent();
        String uidstr = intent.getStringExtra("uidstr");
        if(uidstr==null||uidstr.equals("")){
            uidstr = AppApplication.muidstr;
        }
        tv.setText("设备编号："+uidstr);
        if(uidstr!=null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String nameSpace = "http://tempuri.org/";
                    String methodName = "CheckHm";
                    String endPoint = "http://www.scetia.com/Scetia.SampleManage.WS/SampleManagement.asmx";
                    String soapAction = "http://tempuri.org/CheckHm";
                    SoapObject soapObject = new SoapObject(nameSpace, methodName);
                    soapObject.addProperty("hMIdStr", AppApplication.muidstr);
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                            SoapEnvelope.VER10);
                    envelope.bodyOut = soapObject;
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(soapObject);
                    HttpTransportSE transport = new HttpTransportSE(endPoint);
                    try {
                        transport.call(soapAction, envelope);
                    } catch (IOException | XmlPullParserException e) {
                        e.printStackTrace();
                    }
                    SoapObject object = (SoapObject) envelope.bodyIn;
                    if(object.toString().equals("CheckHmResponse{}")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ShebeixinxiActivity.this,"还未绑定会员编号",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        final String result = object.getProperty(0).toString();
                        SPUtil.setSPConfig("huiyuanhao", result);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv1.setText("会员号：" + result);
                            }
                        });
                    }

                }
            }).start();
        }

    }
}
