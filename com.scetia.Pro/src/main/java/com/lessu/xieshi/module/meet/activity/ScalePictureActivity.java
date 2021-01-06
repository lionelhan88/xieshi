package com.lessu.xieshi.module.meet.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bm.library.PhotoView;
import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.GlideUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * created by ljs
 * on 2020/8/24
 */
public class ScalePictureActivity extends NavigationActivity {
    @BindView(R.id.scale_picture_image)
    PhotoView scalePictureImage;
    @BindView(R.id.scale_rl)
    RelativeLayout scaleBackground;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale_picture);
        navigationBar.setBackgroundColor(Color.BLACK);
        ButterKnife.bind(this);
        //启用放大图片
        scalePictureImage.enable();
        String photoUrl = getIntent().getStringExtra("detail_photo");
        GlideUtil.showImageViewNoCache(this,photoUrl,scalePictureImage);
        if(photoUrl!=null&&photoUrl.contains("Scetia_Meet_Gonggao")){
            navigationBar.setBackgroundColor(0xFF3598DC);
            setTitle("会议内容");
            scaleBackground.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }

    @OnClick(R.id.scale_picture_image)
    public void onViewClicked() {
        finish();
        overridePendingTransition(0,R.anim.acitvity_zoom_close);
    }
}
