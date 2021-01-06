package com.lessu.xieshi.module.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.R;

/**
 * created by ljs
 * on 2020/12/3
 */
public class SplashActivity extends NavigationActivity {
    private Runnable runnable = () -> {
        startOtherActivity(LoginActivity.class);
        finish();
    };
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_layout);
        //避免每次点击home建后，点击图标会重新创建启动页
        if (!this.isTaskRoot()) {
            //如果你就放在launcher Activity中话，这里可以直接return了
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }
        navigationBar.setVisibility(View.GONE);
        handler=new Handler();
        handler.postDelayed(runnable,1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
