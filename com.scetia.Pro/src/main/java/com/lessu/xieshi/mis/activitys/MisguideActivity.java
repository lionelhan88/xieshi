package com.lessu.xieshi.mis.activitys;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.AppApplication;
import com.lessu.xieshi.R;
import com.lessu.xieshi.SettingActivity;
import com.lessu.xieshi.login.LoginActivity;
import com.lessu.xieshi.mis.bean.Misguidebean;
import com.lessu.xieshi.tianqi.activitys.TianqiActivity;

import java.util.ArrayList;
import java.util.HashMap;


public class MisguideActivity extends NavigationActivity {
    private BarButtonItem handleButtonItem;
    private LinearLayout ll_addparent;
    private BarButtonItem handleButtonItem1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_misguideactivity);
        navigationBar.setBackgroundColor(0xFF3598DC);
        this.setTitle("内部管理");
        handleButtonItem = new BarButtonItem(this, R.drawable.back);
        handleButtonItem.setOnClickMethod(this, "houtui");
        navigationBar.setLeftBarItem(null);

        handleButtonItem1 = new BarButtonItem(this, R.drawable.duoyund);
        handleButtonItem1.setOnClickMethod(this, "tianqi");
        navigationBar.setRightBarItem(handleButtonItem1);
        //检查软件是否需要更新
        getUpdate();
        initView();
        //初始化数据
        initData();
    }

    private void initView() {
        ll_addparent = (LinearLayout) findViewById(R.id.ll_addparent);
    }

    private void getUpdate() {
        HashMap<String, Object> updateparams = new HashMap<String, Object>();
        updateparams.put("PlatformType", "1");//1为安卓
        updateparams.put("SystemType", "2");//2为内部版
        EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceUST.asmx/GetAppVersion"), updateparams, new EasyAPI.ApiFastSuccessCallBack() {
            @Override
            public void onSuccessJson(JsonElement result) {
                // TODO Auto-generated method stub
                String versionName = null;
                try {
                    versionName = getPackageManager().getPackageInfo("com.scetia.Pro", 0).versionName;
                    System.out.println("versionName.." + versionName);
                } catch (PackageManager.NameNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                JsonObject json = result.getAsJsonObject().get("Data").getAsJsonArray().get(0).getAsJsonObject();
                String serviceVersion = json.get("Version").getAsString();
                String[] localVersionArray = versionName.split("\\.");
                String[] serviceVersionArray = serviceVersion.split("\\.");
                int localCount = localVersionArray.length;
                int serviceCount = serviceVersionArray.length;
                int count = localCount;
                if (localCount > serviceCount) {
                    count = serviceCount;
                }
                Boolean updateFlag = false;
                try {
                    for (int i = 0; i < count; i++) {
                        if (Integer.parseInt(localVersionArray[i]) < Integer.parseInt(serviceVersionArray[i])) {
                            updateFlag = true;
                            AppApplication.isupdate = true;
                        }
                    }
                } catch (Exception e) {
                    updateFlag = false;
                    AppApplication.isupdate = false;
                }
                if (updateFlag) {
                    final String urlString = json.get("Update_Url").getAsString();
                    String description = "更新内容:\r\n" + json.get("Description").getAsString() + "是否立即前往更新";
                    LSAlert.showDialog(MisguideActivity.this, "检查到新版本", description, "确定", "取消", new LSAlert.DialogCallback() {

                        @Override
                        public void onConfirm() {
                            downLoadFile(urlString);
//								if (updatefile == null){
//									LSAlert.showAlert(SettingActivity.this, "未成功下载更新软件，请稍后再试");
//									return;
//								}
//								else{
//									installApk(updatefile);
//								}
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
            }

        });
    }

    protected void downLoadFile(String httpUrl) {
        final Uri uri = Uri.parse(httpUrl);
        final Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
        String shortuserpower = intent.getStringExtra("shortuserpower");
        System.out.println("shortuserpower..." + shortuserpower);
        //shortuserpower="11111";//这里是测试用的测试测试测试！！！

        Misguidebean huiyuanbean = new Misguidebean(R.drawable.huiyuanxinxi, "会员信息查询", MisHysearchActivity.class);
        Misguidebean zhenshubean = new Misguidebean(R.drawable.zhengshuxinxi, "证书信息查询", MisZssearchActivity.class);
        Misguidebean pinguchaxunbean = new Misguidebean(R.drawable.pingguchaxun, "评估信息查询", MisPinguActivity.class);
        Misguidebean xinxibean = new Misguidebean(R.drawable.xinxitongzhi, "信息通知", MistongzhiActivity.class);
        Misguidebean nianjiashenqinbean = new Misguidebean(R.drawable.nianjiashenqing, "年假管理", MisnianjiaActivity.class);
        //2018-10-16新增功能模块 “比对审批”
        Misguidebean comparisonApproval = new Misguidebean(R.drawable.shujubidui,"比对审批",MisComparisonAprovalActivity.class);

        Misguidebean settingbean = new Misguidebean(R.drawable.shezhimis, "系统设置", SettingActivity.class);
        Misguidebean loginbean = new Misguidebean(R.drawable.chongxindenglu, "重新登陆", LoginActivity.class);


        final ArrayList<Misguidebean> al = new ArrayList();
        String s1 = shortuserpower.substring(0, 1);
        String s2 = shortuserpower.substring(1, 2);
        String s3 = shortuserpower.substring(2, 3);
        String s4 = shortuserpower.substring(3, 4);
        String s5 = shortuserpower.substring(4, 5);
        String s6="";
        if(shortuserpower.length()==6) {
             s6 = shortuserpower.substring(0, 1);
             s1 = shortuserpower.substring(1, 2);
             s2 = shortuserpower.substring(2, 3);
             s3 = shortuserpower.substring(3, 4);
             s4 = shortuserpower.substring(4, 5);
             s5 = shortuserpower.substring(5, 6);
        }
        if (s1.equals("1")) {
            al.add(huiyuanbean);
        }
        if (s2.equals("1")) {
            al.add(zhenshubean);
        }
        if (s3.equals("1")) {
            al.add(pinguchaxunbean);
        }
        if (s4.equals("1")) {
            al.add(xinxibean);
        }
        if (s5.equals("1")) {
            al.add(nianjiashenqinbean);
        }
        if(s6.equals("1")) {
            //新增的权限“比对审批”
            al.add(comparisonApproval);
        }
        al.add(settingbean);
        al.add(loginbean);

        for (int i = 0; i < al.size(); i++) {
            View view = View.inflate(this, R.layout.misaddguide_item, null);
            LinearLayout ll_additem = (LinearLayout) view.findViewById(R.id.ll_additem);
            ImageView iv_additem = (ImageView) view.findViewById(R.id.iv_additem);
            TextView tv_additem = (TextView) view.findViewById(R.id.tv_additem);
            iv_additem.setImageResource(al.get(i).pic);
            tv_additem.setText(al.get(i).text);
            final int finalI = i;

            ll_additem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (finalI == al.size() - 1) {
                        Intent intentTUICHU = new Intent();
                        intentTUICHU.setClass(MisguideActivity.this, LoginActivity.class);
                        intentTUICHU.putExtra("exit", true);
                        intentTUICHU.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intentTUICHU);
                        finish();
                    } else {
                        startActivity(new Intent(MisguideActivity.this, al.get(finalI).clazz));
                    }
                }
            });
            ll_addparent.addView(view);
        }
    }


    public void tianqi() {
        startActivity(new Intent(MisguideActivity.this, TianqiActivity.class));
    }
}
