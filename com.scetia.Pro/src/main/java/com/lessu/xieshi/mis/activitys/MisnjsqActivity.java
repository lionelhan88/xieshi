package com.lessu.xieshi.mis.activitys;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.net.ApiError;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.MyToast;

import java.util.Calendar;
import java.util.HashMap;

public class MisnjsqActivity extends NavigationActivity implements View.OnClickListener {

    private BarButtonItem handleButtonItem;
    private CheckBox cb_njsq;
    private TextView tv_starttime;
    private TextView tv_endtime;
    private TextView tv_alltime;
    private EditText et_shiyou;
    private Calendar cal;
    private int startyear;
    private int startmonth;
    private int startday;
    private TextView tv_startstart;
    private TextView tv_endend;
    private int endyear;
    private int endmonth;
    private int endday;
    private boolean starttimeisok;
    private boolean endtimeisok;
    private int startmonthnew;
    private int endmonthnew;
    private double getbetweendatas;
    private boolean bantian;
    private Button bt_njsq_tijiao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_misnjsq);
        navigationBar.setBackgroundColor(0xFF3598DC);
        this.setTitle("年假申请");
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
        cb_njsq = (CheckBox) findViewById(R.id.cb_njsq);
        tv_starttime = (TextView) findViewById(R.id.tv_starttime);
        tv_endtime = (TextView) findViewById(R.id.tv_endtime);
        tv_startstart = (TextView) findViewById(R.id.tv_startstart);
        tv_endend = (TextView) findViewById(R.id.tv_endend);

        tv_alltime = (TextView) findViewById(R.id.tv_alltime);
        et_shiyou = (EditText) findViewById(R.id.et_shiyou);

        bt_njsq_tijiao = (Button) findViewById(R.id.bt_njsq_tijiao);

        tv_starttime.setOnClickListener(this);
        tv_endtime.setOnClickListener(this);
        bt_njsq_tijiao.setOnClickListener(this);


        cb_njsq.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    tv_endtime.setText("");
                    tv_starttime.setText("");
                    tv_alltime.setText("");
                    endtimeisok=false;
                    starttimeisok=false;

                    tv_endtime.setOnClickListener(null);
                    tv_startstart.setTextColor(0xff268beb);
                    tv_starttime.setTextColor(0xff666666);
                    tv_endend.setTextColor(0xff999999);
                    tv_endtime.setTextColor(0xff999999);
                    bantian = true;
                }else{
                    tv_alltime.setText("");
                    tv_endtime.setText("");
                    tv_starttime.setText("");
                    tv_endtime.setOnClickListener(MisnjsqActivity.this);
                    bantian = false;
                }
            }
        });

    }

    private void initData() {
        getDate();
    }
    private void getDate() {
        cal = Calendar.getInstance();
        startyear = cal.get(Calendar.YEAR);       //获取年月日时分秒
        startmonth = cal.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        startday = cal.get(Calendar.DAY_OF_MONTH);

        endyear = cal.get(Calendar.YEAR);       //获取年月日时分秒
        endmonth = cal.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        endday = cal.get(Calendar.DAY_OF_MONTH);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_starttime:
                tv_startstart.setTextColor(0xff268beb);
                tv_starttime.setTextColor(0xff666666);
                tv_endend.setTextColor(0xff999999);
                tv_endtime.setTextColor(0xff999999);
                DatePickerDialog datePicker=new DatePickerDialog(MisnjsqActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
//                        // TODO Auto-generated method stub
//                        if(endyear<year){
//                            MyToast.showShort("请选择结束以前的日期");
//                            return;
//                        }else if(endyear>=year&&endmonth<monthOfYear){
//                            MyToast.showShort("请选择结束以前的日期");
//                            return;
//                        }else if(endyear>=year&&endmonth==monthOfYear&&endday<dayOfMonth){
//                            MyToast.showShort("请选择结束以前的日期");
//                            return;
//                        }else{
                        tv_starttime.setText(year+"年"+(++monthOfYear)+"月"+dayOfMonth+"日");
                        startyear=year;
                        startmonth=--monthOfYear;
                        startday=dayOfMonth;
                        starttimeisok = true;
                        startmonthnew = startmonth+1;
                        if(bantian){
                            getbetweendatas=0.5;
                            tv_alltime.setText(String.valueOf(getbetweendatas));
                            tv_endtime.setText(year+"年"+(++monthOfYear)+"月"+dayOfMonth+"日");
                        }else {
                            if (endtimeisok) {

                                getbetweendatas = Content.getbetweendatas(getokdata(startyear) + getokdata(startmonthnew) + getokdata(startday), getokdata(endyear) + getokdata(endmonthnew) + getokdata(endday));
                                tv_alltime.setText(String.valueOf(getbetweendatas));
                            }
                        }

//                        }
                    }
                }, startyear, startmonth, startday);
                datePicker.show();
                break;

            case R.id.tv_endtime:
                tv_startstart.setTextColor(0xff999999);
                tv_starttime.setTextColor(0xff999999);
                tv_endend.setTextColor(0xff268beb);
                tv_endtime.setTextColor(0xff666666);

                DatePickerDialog datePicker1=new DatePickerDialog(MisnjsqActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
//                        if(year<startyear){
//                            MyToast.showShort("请选择开始时间以后或者今天以后的的日期");
//                            return;
//                        }else if(year>=startyear&&monthOfYear<startmonth){
//                            MyToast.showShort("请选择开始时间以后或者今天以后的的日期");
//                            return;
//                        }else if(year>=startyear&&monthOfYear==startmonth&&dayOfMonth<startday){
//                            MyToast.showShort("请选择开始时间以后或者今天以后的的日期");
//                            return;
//                        }else{
                        tv_endtime.setText(year+"年"+(++monthOfYear)+"月"+dayOfMonth+"日");
                        endyear=year;
                        endmonth=--monthOfYear;
                        endday=dayOfMonth;
                        endtimeisok = true;
                        endmonthnew = endmonth+1;
                        if(starttimeisok){

                            getbetweendatas =  Content.getbetweendatas(getokdata(startyear) + getokdata(startmonthnew) + getokdata(startday), getokdata(endyear) + getokdata(endmonthnew) + getokdata(endday));
                            tv_alltime.setText(String.valueOf(getbetweendatas));

                        }

//                        }
                    }
                }, endyear, endmonth, endday);
                datePicker1.show();

                break;

            case R.id.bt_njsq_tijiao:
                if(getbetweendatas<0){
                    MyToast.showShort("拜托，请假时间不可以这样选好吗");
                }else {
                    String token = Content.gettoken();
                    HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("Token", token);
                    if (bantian) {
                        params.put("isBt", 1);
                        params.put("Days", 0.5);
                    } else {
                        params.put("isBt", 0);
                        params.put("Days", getbetweendatas);
                    }
                    String textstart = tv_starttime.getText().toString();
                    String s1 = textstart.replace("年", "-");
                    String s2 = s1.replace("月", "-");
                    String startstring = s2.replace("日", "");
                    String textend = tv_endtime.getText().toString();
                    String s3 = textend.replace('年', '-');
                    String s4 = s3.replace("月", "-");
                    String endstring = s4.replace("日", "");
                    params.put("LeaveTime", startstring);
                    params.put("LeaveTime2", endstring);
                    params.put("LeaveReason", et_shiyou.getText().toString());


                    System.out.println(params);
                    EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceMis.asmx/NJApply "), params, new EasyAPI.ApiFastSuccessFailedCallBack() {
                        @Override
                        public void onSuccessJson(JsonElement result) {
                            // TODO Auto-generated method stub
                            System.out.println(result);
                            boolean success = result.getAsJsonObject().get("Success").getAsBoolean();
                            String message = result.getAsJsonObject().get("Message").getAsString();
                            if(!success){
                                LSAlert.showAlert(MisnjsqActivity.this,message);
                            }else{
                                LSAlert.showAlert(MisnjsqActivity.this,"年假申请成功^_^");
                            }
                          //  MyToast.showShort("年假申请成功^_^)");

                        }

                        @Override
                        public String onFailed(ApiError error) {
                            System.out.println("失败了。。。。。。。。" + error.errorMeesage);
                            MyToast.showShort(error.errorMeesage);
                            return null;
                        }
                    });
                }
                break;

        }

    }
    private String getokdata(int monthorday){
        if(monthorday<10){
            return "0"+monthorday;
        }else{
            return String.valueOf(monthorday);
        }
    }
}
