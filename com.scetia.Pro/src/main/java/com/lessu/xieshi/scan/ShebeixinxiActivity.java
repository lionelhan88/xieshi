package com.lessu.xieshi.scan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.lessu.xieshi.AppApplication;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.Shref;
import com.raylinks.Function;
import com.raylinks.ModuleControl;

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
        System.out.println("uidstr........"+uidstr);
        tv.setText("设备编号："+uidstr);
        if(AppApplication.muidstr!=null) {
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
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (XmlPullParserException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    SoapObject object = (SoapObject) envelope.bodyIn;
                    System.out.println("啦啦啦。。。"+object.toString());
                    if(object.toString().equals("CheckHmResponse{}")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ShebeixinxiActivity.this,"还未绑定会员编号",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        final String result = object.getProperty(0).toString();
                        Shref.setString(ShebeixinxiActivity.this, "huiyuanhao", result);
                        System.out.println("object..." + object);
                        System.out.println("result...." + result);
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
