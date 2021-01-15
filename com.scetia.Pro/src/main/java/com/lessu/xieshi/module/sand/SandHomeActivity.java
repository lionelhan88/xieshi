package com.lessu.xieshi.module.sand;

import android.os.Bundle;
import android.view.View;

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
        navigationBar.setVisibility(View.GONE);
    }
}
