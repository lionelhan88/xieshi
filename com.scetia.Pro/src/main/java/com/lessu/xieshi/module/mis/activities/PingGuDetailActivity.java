package com.lessu.xieshi.module.mis.activities;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.net.ApiError;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.xieshi.R;
import com.lessu.xieshi.utils.GsonUtil;
import com.lessu.xieshi.module.mis.bean.Pgdetailbean;
import com.scetia.Pro.common.Util.Constants;

import java.util.HashMap;

public class PingGuDetailActivity extends NavigationActivity {

    private BarButtonItem handleButtonItem;
    private TextView tv_pg_bianhao;
    private TextView tv_pg_danweiname;
    private TextView tv_pg_shenqing;
    private TextView tv_pg_dqzt;
    private TextView tv_pg_pglx;
    private TextView tv_pg_finishdate;
    private TextView tv_pg_slname;
    private TextView tv_pg_sldate;
    private TextView tv_pg_pszz;
    private TextView tv_pg_zxdate;
    private TextView tv_pg_sh;
    private TextView tv_pg_shdate;
    private TextView tv_pg_pz;
    private TextView tv_pg_pzdate;
    private TextView tv_kuoxiangitem;
    private View pg_xian;
    private LinearLayout ll_kuoxiangitem;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pgdetail;
    }

    @Override
    protected void initView() {
        this.setTitle("评估信息");
        handleButtonItem = new BarButtonItem(this , R.drawable.back );
        handleButtonItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        navigationBar.setLeftBarItem(handleButtonItem);
        tv_pg_bianhao = (TextView) findViewById(R.id.tv_pg_bianhao);//编号
        tv_pg_danweiname = (TextView) findViewById(R.id.tv_pg_danweiname);//单位名称
        tv_pg_shenqing = (TextView) findViewById(R.id.tv_pg_shenqing); //申请人
        tv_pg_dqzt = (TextView) findViewById(R.id.tv_pg_dqzt);//当前状态
        tv_pg_pglx = (TextView) findViewById(R.id.tv_pg_pglx);//评估类型

        ll_kuoxiangitem = (LinearLayout) findViewById(R.id.ll_kuoxiangitem);
        tv_kuoxiangitem = (TextView) findViewById(R.id.tv_kuoxiangitem);
        pg_xian = findViewById(R.id.pg_xian);

        tv_pg_finishdate = (TextView) findViewById(R.id.tv_pg_finishdate); //完成日期
        tv_pg_slname = (TextView) findViewById(R.id.tv_pg_slname);//受理人
        tv_pg_sldate = (TextView) findViewById(R.id.tv_pg_sldate);//受理日期
        tv_pg_pszz = (TextView) findViewById(R.id.tv_pg_pszz); //评审组长
        tv_pg_zxdate = (TextView) findViewById(R.id.tv_pg_zxdate);//执行日期
        tv_pg_sh = (TextView) findViewById(R.id.tv_pg_sh);//审核人
        tv_pg_shdate = (TextView) findViewById(R.id.tv_pg_shdate);//审核日期
        tv_pg_pz = (TextView) findViewById(R.id.tv_pg_pz);//批准人
        tv_pg_pzdate = (TextView) findViewById(R.id.tv_pg_pzdate);//批准日期
    }

    @Override
    protected void initData() {
        Intent getintent=getIntent();
        String id = getintent.getExtras().getString("Id");
        System.out.println(id);
        String token =  Constants.User.GET_TOKEN();
        HashMap<String, Object> params = new HashMap<>();
        params.put("Token", token);
        params.put("s1", id);
        System.out.println(params);
        EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceMis.asmx/PGDetail "), params, new EasyAPI.ApiFastSuccessFailedCallBack() {
            @Override
            public void onSuccessJson(JsonElement result) {
                // TODO Auto-generated method stub
                System.out.println(result);
                Pgdetailbean pgdetailbean = GsonUtil.JsonToObject(result.toString(), Pgdetailbean.class);
                Pgdetailbean.DataBean data = pgdetailbean.getData();
               // String memberCode = data.getMemberCode();//编号
                String innerId = data.getInnerId();//编号
                String memberName = data.getMemberName();//单位名称
                String applicant = data.getApplicant();//申请人
                String status = data.getStatus();//当前状态
                String applicationType = data.getApplicationType();//评估类型

                String items = data.getItems();
                if(items.equals("")){
                    ll_kuoxiangitem.setVisibility(View.GONE);
                    pg_xian.setVisibility(View.GONE);
                    tv_kuoxiangitem.setVisibility(View.GONE);
                }else{
                    ll_kuoxiangitem.setVisibility(View.VISIBLE);
                    pg_xian.setVisibility(View.VISIBLE);
                    tv_kuoxiangitem.setVisibility(View.VISIBLE);
                    tv_kuoxiangitem.setText(items);
                }

                String assessFinishDate = data.getAssessFinishDate();//完成日期
                String accepterName = data.getAccepterName();//受理人
                String applicationDate = data.getApplicationDate();//受理日期
                String assessLeaderName = data.getAssessLeaderName();//评审组长
                String assessExecuteDate = data.getAssessExecuteDate();//执行日期
                String checkerName = data.getCheckerName();//审核人
                String checkDate = data.getCheckDate();//审核日期
                String approverName = data.getApproverName();//批准人
                String approveDate = data.getApproveDate();//批准日期

                tv_pg_bianhao.setText(innerId);
                tv_pg_danweiname.setText(memberName);
                tv_pg_shenqing.setText(applicant);
                tv_pg_dqzt.setText(status);
                tv_pg_pglx.setText(applicationType);
                tv_pg_finishdate.setText(assessFinishDate);
                tv_pg_slname.setText(accepterName);
                tv_pg_sldate.setText(applicationDate);
                tv_pg_pszz.setText(assessLeaderName);
                tv_pg_zxdate.setText(assessExecuteDate);
                tv_pg_sh.setText(checkerName);
                tv_pg_shdate.setText(checkDate);
                tv_pg_pz.setText(approverName);
                tv_pg_pzdate.setText(approveDate);
            }
            @Override
            public String onFailed(ApiError error) {
                return null;
            }
        });
    }
}
