package com.lessu.xieshi.module.sand;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.R;

/**
 * created by ljs
 * on 2020/10/26
 */
public class SandHomeActivity extends NavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sand_home);
        setTitle("建设用砂管理");
        navigationBar.setVisibility(View.GONE);
    }
}
