package com.lessu.xieshi.scan;

import android.os.Bundle;

import com.lessu.xieshi.R;
import com.lessu.xieshi.XieShiSlidingMenuActivity;

public class ScanActivity extends XieShiSlidingMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        navigationBar.setBackgroundColor(0xFF3598DC);
        this.setTitle("新加功能");


    }
}
