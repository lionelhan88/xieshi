package com.lessu.xieshi.mis.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
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
import com.lessu.xieshi.QRCodeActivity;
import com.lessu.xieshi.R;
import com.lessu.xieshi.SettingActivity;
import com.lessu.xieshi.Utils.Common;
import com.lessu.xieshi.Utils.Shref;
import com.lessu.xieshi.login.LoginActivity;
import com.lessu.xieshi.mis.bean.Misguidebean;
import com.lessu.xieshi.scan.ScanActivity;
import com.lessu.xieshi.tianqi.activitys.TianqiActivity;

import java.util.ArrayList;
import java.util.HashMap;



public class MisguideActivity extends NavigationActivity {
    private BarButtonItem handleButtonItem;
    private LinearLayout ll_addparent;
    private BarButtonItem handleButtonItem1;
    private BarButtonItem handleButtonItem2;

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
        handleButtonItem2 = new BarButtonItem(this, R.drawable.icon_scan_white);
        handleButtonItem1.setOnClickMethod(this, "tianqi");
        navigationBar.addRightBarItem(handleButtonItem2);
        navigationBar.addRightBarItem(handleButtonItem1);
        //检查软件是否需要更新
        getUpdate();
        initView();
        //初始化数据
        initData();
    }

    private void initView() {
        ll_addparent = (LinearLayout) findViewById(R.id.ll_addparent);
        handleButtonItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Shref.getString(MisguideActivity.this, Common.USERID,"").equals("")){
                    //如果userid是"",则要提示用户重新登录才能获取到userId
                    LSAlert.showDialog(MisguideActivity.this, "提示", "使用此功能需要重新登录\n是否现在登录？",
                            "确定", "取消", new LSAlert.DialogCallback() {
                                @Override
                                public void onConfirm() {
                                    //退出登录
                                    loginOut();
                                }

                                @Override
                                public void onCancel() {

                                }
                            });
                }else{
                    startActivity(new Intent((MisguideActivity.this),ScanActivity.class));
                }
            }
        });
    }

    private void getUpdate() {
        HashMap<String, Object> updateparams = new HashMap<String, Object>();
        updateparams.put("PlatformType", "1");//1为安卓
        updateparams.put("SystemType", "2");//2为内部版
        EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceUST.asmx/GetAppVersion"), updateparams, new EasyAPI.ApiFastSuccessCallBack() {
            @Override
            public void onSuccessJson(JsonElement result) {
                String versionName = null;
                try {
                    versionName = getPackageManager().getPackageInfo("com.scetia.Pro", 0).versionName;
                    System.out.println("versionName.." + versionName);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                JsonObject json = result.getAsJsonObject().get("Data").getAsJsonArray().get(0).getAsJsonObject();
                String serviceVersion = json.get("Version").getAsString();
                //是否强制更新标识
                final int isMustBeUpdate = json.get("Update_Flag").getAsInt();
                String[] localVersionArray = versionName.split("\\.");
                String[] serviceVersionArray = serviceVersion.split("\\.");
                int localCount = localVersionArray.length;
                int serviceCount = serviceVersionArray.length;
                int count = localCount;
                if (localCount > serviceCount) {
                    count = serviceCount;
                }
                boolean updateFlag = false;
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
                    String description="";
                    if(isMustBeUpdate==1){
                        //强制更新
                        description = "更新内容:\r\n" + json.get("Description").getAsString()
                                +"\r\n此更新为强制更新，否则不可用！\r\n"+ "是否立即前往更新？";
                    }else{
                        description = "更新内容:\r\n" + json.get("Description").getAsString() + "是否立即前往更新？";
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(MisguideActivity.this)
                            .setTitle("检查到新版本")
                            .setMessage(description)
                            .setCancelable(false)
                            .setPositiveButton("确定",null)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(isMustBeUpdate==1) {
                                AppApplication.exit();
                            }
                        }
                    });
                    final AlertDialog dialog = builder.create();
                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialogInterface) {
                            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    downLoadFile(urlString);
                                }
                            });
                        }
                    });
                    dialog.show();
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
        Misguidebean huiyuanbean = new Misguidebean(R.drawable.huiyuanxinxi, "会员信息查询", MisHysearchActivity.class);
        Misguidebean zhenshubean = new Misguidebean(R.drawable.zhengshuxinxi, "证书信息查询", MisZssearchActivity.class);
        Misguidebean pinguchaxunbean = new Misguidebean(R.drawable.pingguchaxun, "评估信息查询", MisPinguActivity.class);
        Misguidebean xinxibean = new Misguidebean(R.drawable.xinxitongzhi, "信息通知", MistongzhiActivity.class);
        Misguidebean nianjiashenqinbean = new Misguidebean(R.drawable.nianjiashenqing, "年假管理", MisnianjiaActivity.class);
        //2018-10-16新增功能模块 “比对审批”
        Misguidebean comparisonApproval = new Misguidebean(R.drawable.shujubidui,"比对审批",MisComparisonAprovalActivity.class);

        Misguidebean settingbean = new Misguidebean(R.drawable.shezhimis, "系统设置", SettingActivity.class);
        Misguidebean loginbean = new Misguidebean(R.drawable.chongxindenglu, "重新登陆", LoginActivity.class);
        //Misguidebean scanBean = new Misguidebean(R.drawable.icon_scan,"扫一扫",ScanActivity.class);

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
                        loginOut();
                    } else {
                        startActivity(new Intent(MisguideActivity.this, al.get(finalI).clazz));
                    }
                }
            });
            ll_addparent.addView(view);
        }
    }

    /**
     * 退出登录
     */
    private void loginOut(){
        Intent intentTUICHU = new Intent();
        intentTUICHU.setClass(MisguideActivity.this, LoginActivity.class);
        intentTUICHU.putExtra("exit", true);
        intentTUICHU.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentTUICHU);
        finish();
    }
    public void tianqi() {
        startActivity(new Intent(MisguideActivity.this, TianqiActivity.class));
    }
}
