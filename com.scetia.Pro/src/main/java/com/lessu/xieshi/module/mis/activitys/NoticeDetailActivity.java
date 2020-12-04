package com.lessu.xieshi.module.mis.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.R;

public class NoticeDetailActivity extends NavigationActivity {

    private BarButtonItem handleButtonItem;
    private TextView tv_tznr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tzdetail);
        navigationBar.setBackgroundColor(0xFF3598DC);
        this.setTitle("通知内容");
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
        tv_tznr = (TextView) findViewById(R.id.tv_tznr);
    }

    private void initData() {
        Intent getintent=getIntent();
        String tongzhi = getintent.getStringExtra("tongzhi");
        tv_tznr.setText(tongzhi);

    }
}
