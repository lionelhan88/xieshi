package com.lessu.xieshi.module.meet.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.R;
import com.scetia.Pro.common.Util.GlideUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * created by ljs
 * on 2020/8/24
 */
public class ScalePictureActivity extends NavigationActivity {
    @BindView(R.id.scale_picture_image)
    ImageView scalePictureImage;
    @BindView(R.id.scale_rl)
    FrameLayout scaleBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale_picture);
        navigationBar.setBackgroundColor(Color.BLACK);
        ButterKnife.bind(this);
        //启用放大图片
        String photoUrl = getIntent().getStringExtra("detail_photo");
        if (photoUrl != null && photoUrl.contains("Scetia_Meet_Gonggao")) {
            navigationBar.setBackgroundColor(ContextCompat.getColor(this,R.color.top_bar_background));
            setTitle("会议内容");
            scaleBackground.setBackgroundColor(getResources().getColor(R.color.white));
        }
        scalePictureImage.post(()->{
            GlideUtil.showImageViewNoCache(this,photoUrl,scalePictureImage);
        });
    }

    @OnClick(R.id.scale_picture_image)
    public void onViewClicked() {
        back();
    }

    private void back(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
            overridePendingTransition(0, R.anim.acitvity_zoom_close);
        }
    }

    @Override
    protected void leftNavBarClick() {
      back();
    }
}
