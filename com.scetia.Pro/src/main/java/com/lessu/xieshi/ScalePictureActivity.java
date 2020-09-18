package com.lessu.xieshi;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;

import com.bm.library.PhotoView;
import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.Utils.ImageloaderUtil;
import com.lessu.xieshi.Utils.PicSize;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale_picture);
        navigationBar.setBackgroundColor(Color.BLACK);
        ButterKnife.bind(this);
        //启用放大图片
        scalePictureImage.enable();
        String photoUrl = getIntent().getStringExtra("detail_photo");
        ImageLoader.getInstance().displayImage(photoUrl,scalePictureImage, ImageloaderUtil.imageconfigtianqi());
    }

    @OnClick(R.id.scale_picture_image)
    public void onViewClicked() {
        finish();
        overridePendingTransition(0,R.anim.acitvity_zoom_close);
    }
}
