package com.lessu.xieshi.module.meet.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.R;
import com.scetia.Pro.common.Util.GlideUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * created by ljs
 * on 2020/8/24
 */
public class ScalePictureActivity extends NavigationActivity {
    @BindView(R.id.scale_picture_image)
    ImageView scalePictureImage;
    @BindView(R.id.scale_rl)
    RelativeLayout scaleBackground;
    @BindView(R.id.content_loading)
    ImageView contentLoading;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scale_picture;
    }

    @Override
    protected void initView() {
        navigationBar.setBackgroundColor(Color.BLACK);
        //启用放大图片
        String photoUrl = getIntent().getStringExtra("detail_photo");
        String commissionDetailPhoto = getIntent().getStringExtra("commission_detail_photo");
        if (photoUrl != null) {
            if (photoUrl.contains("Scetia_Meet_Gonggao")) {
                navigationBar.setBackgroundColor(ContextCompat.getColor(this, R.color.top_bar_background));
                setTitle("会议内容");
                scaleBackground.setBackgroundColor(getResources().getColor(R.color.white));
            }
            GlideUtil.showImageViewNoCache(this, photoUrl, scalePictureImage);
        }
        if (commissionDetailPhoto != null) {
            //设置图片正在加载动画
            Glide.with(this).
                    asGif().
                    load(R.drawable.ic_image_loading).
                    diskCacheStrategy(DiskCacheStrategy.RESOURCE).
                    into(contentLoading);
            Glide.with(this).load(commissionDetailPhoto).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            //图片加载完成，加载动画隐藏
                            contentLoading.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(scalePictureImage);
        }

    }

    @OnClick(R.id.scale_picture_image)
    public void onViewClicked() {
        back();
    }

    private void back() {
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
