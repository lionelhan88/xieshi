package com.lessu.xieshi.view;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.scetia.Pro.common.Util.DensityUtil;
import com.lessu.xieshi.R;
import com.scetia.Pro.common.Util.GlideUtil;

/**
 * created by ljs
 * on 2020/10/27
 */
public class ImageViewWithDelete extends RelativeLayout {
    private ImageView srcImageView, closeImg;
    private Uri imageUri;
    private CloseImageViewClick closeImageViewClick;

    public void setCloseImageViewClick(CloseImageViewClick closeImageViewClick) {
        this.closeImageViewClick = closeImageViewClick;
    }

    public ImageViewWithDelete(Context context) {
        super(context);
    }

    public ImageViewWithDelete(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public interface CloseImageViewClick {
        void closeListener();

        void srcClickListener(View v);
    }

    private void init() {
        initSrcImageView();
        initCloseImageView();
        //加载默认的图片
        GlideUtil.showImageViewNoCache(this, R.drawable.picture_bg, imageUri, srcImageView);
        srcImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeImageViewClick.srcClickListener(v);
            }
        });
        closeImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除图片，清除已经存在的imageUri和图片base64字符串
                imageUri = null;
                closeImageViewClick.closeListener();
            }
        });
    }

    /**
     * 初始化内容显示区域
     */
    private void initSrcImageView() {
        srcImageView = new ImageView(getContext());
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.setMargins(
                DensityUtil.dip2px(getContext(), 10f), DensityUtil.dip2px(getContext(), 10f),
                DensityUtil.dip2px(getContext(), 10f), DensityUtil.dip2px(getContext(), 10f));
        srcImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        addView(srcImageView, lp);
    }

    /**
     * 初始化清除按钮
     */
    private void initCloseImageView() {
        closeImg = new ImageView(getContext());
        closeImg.setImageResource(R.drawable.del_pic_bg);
        closeImg.setScaleType(ImageView.ScaleType.FIT_XY);
        LayoutParams lp2 = new LayoutParams(DensityUtil.dip2px(getContext(), 20f), DensityUtil.dip2px(getContext(), 20f));
        //默认隐藏view
        closeImg.setVisibility(View.GONE);
        addView(closeImg, lp2);
    }

    /**
     * 加载网络图片
     *
     * @param path
     */
    public void setImageUrl(String path) {
        closeImg.setVisibility(path.equals("") ? View.GONE : VISIBLE);
        if(TextUtils.isEmpty(path)){
            Glide.with(this).load(R.drawable.icon_take_photo).into(srcImageView);
        }else{
            GlideUtil.showImageViewNoCache(this,R.drawable.ic_image_loading,path,srcImageView);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() == 0) {
            setMeasuredDimension(0, 0);
        } else {
            int relWidth = getWidthSize(widthSpecSize, widthSpecMode);
            int relHeight = getHeightSize(heightSpecSize, heightSpecMode);
            setMeasuredDimension(relWidth, relHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        MarginLayoutParams mp = (MarginLayoutParams) srcImageView.getLayoutParams();
        closeImg.layout(srcImageView.getWidth() + mp.leftMargin - closeImg.getWidth() / 2,
                mp.topMargin - closeImg.getHeight() / 2,
                srcImageView.getWidth() + mp.leftMargin + closeImg.getWidth() / 2,
                mp.topMargin + closeImg.getWidth() / 2);
    }

    /**
     * 计算出当前viewGroup实际的宽度
     */
    private int getWidthSize(int defSize, int mode) {
        int width = 0;
        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                width = srcImageView.getMeasuredWidth();
                break;
            case MeasureSpec.EXACTLY:
                width = defSize;
                break;
            case MeasureSpec.AT_MOST:
                width = defSize;
                break;
        }
        return width;
    }

    /**
     * 计算测量出viewGroup实际的高度
     */
    private int getHeightSize(int defSize, int mode) {
        int height = 0;
        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                height = srcImageView.getMeasuredWidth();
            case MeasureSpec.EXACTLY:
                height = defSize;
                break;
            case MeasureSpec.AT_MOST:
                height = defSize;
                break;
        }
        return height;
    }

    public boolean getCloseImgVisible() {
        return closeImg.getVisibility() == View.VISIBLE;
    }
}
