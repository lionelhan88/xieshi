package com.lessu.xieshi.mis.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.R;
import com.lessu.xieshi.mis.bean.Miszhengshu;

import java.util.List;

public class ZsdetailActivity extends NavigationActivity {

    private BarButtonItem handleButtonItem;
    private TextView tv_miszszhuangtai;
    private TextView tv_miszsname;
    private TextView tv_miszssex;
    private TextView tv_miszsdanweiname;
    private TextView tv_miszsidcard;
    private TextView tv_miszsmobile;
    private TextView tv_miszsscholl;
    private TextView tv_miszszhuanye;
    private TextView tv_miszsxueli;
    private TextView tv_miszszhichen;
    private TextView tv_miszsxiangmu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zsdetail);
        navigationBar.setBackgroundColor(0xFF3598DC);
        this.setTitle("证书信息");
        handleButtonItem = new BarButtonItem(this , R.drawable.back );
        handleButtonItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        navigationBar.setLeftBarItem(handleButtonItem);
        initView();

        initData();
    }
    private void initView() {
        tv_miszszhuangtai = (TextView) findViewById(R.id.tv_miszszhuangtai);
        tv_miszsname = (TextView) findViewById(R.id.tv_miszsname);
        tv_miszssex = (TextView) findViewById(R.id.tv_miszssex);
        tv_miszsdanweiname = (TextView) findViewById(R.id.tv_miszsdanweiname);
        tv_miszsidcard = (TextView) findViewById(R.id.tv_miszsidcard);
        tv_miszsmobile = (TextView) findViewById(R.id.tv_miszsmobile);
        tv_miszsscholl = (TextView) findViewById(R.id.tv_miszsscholl);
        tv_miszszhuanye = (TextView) findViewById(R.id.tv_miszszhuanye);
        tv_miszsxueli = (TextView) findViewById(R.id.tv_miszsxueli);
        tv_miszszhichen = (TextView) findViewById(R.id.tv_miszszhichen);
        tv_miszsxiangmu = (TextView) findViewById(R.id.tv_miszsxiangmu);

    }
    private void initData() {
        Intent getintent=getIntent();
        Miszhengshu miszhengshu = (Miszhengshu) getintent.getSerializableExtra("miszhengshu");
        Miszhengshu.DataBean data = miszhengshu.getData();
            String certificateState = data.getCertificateState();//状态
            String name = data.getName();//姓名
            String sex = data.getSex();//性别
            String memberName = data.getMemberName();//单位名称
            String identityCardNumber = data.getIdentityCardNumber();//身份证
            String mobilePhoneNumber = data.getMobilePhoneNumber();//手机
            String schoolName = data.getSchoolName();//学校
            String speciality = data.getSpeciality();//专业
            String educationBackground = data.getEducationBackground();//学历
            String titleOfATechnicalPost = data.getTitleOfATechnicalPost();//职称
            List<Miszhengshu.DataBean.CertificateItemItemNamesBean> certificateItemItemNames = data.getCertificateItemItemNames();
            String certificateItemName="" ;
            for (int i = 0; i < certificateItemItemNames.size(); i++) {
                if(i==0){
                    certificateItemName= certificateItemItemNames.get(i).getCertificateItemName();
                }else {
                    certificateItemName = certificateItemName +  "\n"+certificateItemItemNames.get(i).getCertificateItemName();
                }
            }
            System.out.println(certificateItemName);

        tv_miszszhuangtai.setText(certificateState);
        tv_miszsname.setText(name);
        tv_miszssex.setText(sex);
        tv_miszsdanweiname.setText(memberName);
        tv_miszsidcard.setText(identityCardNumber);
        tv_miszsmobile.setText(mobilePhoneNumber);
        tv_miszsscholl.setText(schoolName);
        tv_miszszhuanye.setText(speciality);
        tv_miszsxueli.setText(educationBackground);
        tv_miszszhichen.setText(titleOfATechnicalPost);
        tv_miszsxiangmu.setText(certificateItemName);

    }


}
