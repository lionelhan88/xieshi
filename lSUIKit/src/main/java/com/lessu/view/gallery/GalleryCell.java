package com.lessu.view.gallery;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scetia.Pro.common.Util.DensityUtil;
import com.lessu.uikit.easy.EasyUI;

public class GalleryCell extends RelativeLayout{

    private boolean isHighlight;

    public static interface GalleryCellOnClickListener{
        public void onClick(GalleryCell cell);
    }
    
    private ImageView imageView;
    private TextView textView;

    protected int index;
    protected String imageUrl;


    protected String titleString;

    protected GalleryCellOnClickListener onClickListener;
    
    private boolean showTitle;
    
    public GalleryCell(Context context) {
        super(context);
        createView();
    }

    public void createView(){
        this.setBackgroundColor(Color.WHITE);
        this.setLayoutParams( EasyUI.fillParentLayout );

        imageView = new ImageView(getContext());
        imageView.setLayoutParams(EasyUI.fillParentLayout);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        this.addView(imageView);

        textView = new TextView(getContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, DensityUtil.dip2px(getContext(), 25));
        layoutParams.alignWithParent = true;
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        textView.setLayoutParams(layoutParams);

        textView.setBackgroundColor(0x55000000);
        textView.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
        textView.setTextColor(0xFFFFFFFF);

        this.addView(textView);

        final GalleryCell self = this;

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(self);
                }
            }
        });
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        self.onButtonTouchDown();
                        //			       setImageResource(m_touch_state_image);
                        break;
                    case MotionEvent.ACTION_UP:
                        self.onButtonTouchUp();
                        //			       setImageResource(m_normal_state_image);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        self.onButtonTouchUp();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    public void onButtonTouchDown(){
        isHighlight = true;
        this.postInvalidate();
    }

    public void onButtonTouchUp(){
        isHighlight = false;
        this.postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isHighlight) {
            canvas.drawColor(0x55000000);
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTitleString() {
        return titleString;
    }

    public void setTitleString(String titleString) {
        this.titleString = titleString;
        textView.setText(titleString);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        Glide.with(this).load(imageUrl).into(imageView);
        //ImageLoader.getInstance().displayImage(imageUrl,imageView);
    }

    public void setOnClickListener(GalleryCellOnClickListener galleryCellOnClickListener) {
        this.onClickListener = galleryCellOnClickListener;
    }

	public boolean isShowTitle() {
		return showTitle;
	}

	public void setShowTitle(boolean showTitle) {
		this.showTitle = showTitle;
		if(showTitle){
			textView.setVisibility(View.VISIBLE);
		}else{
			textView.setVisibility(View.GONE);
		}
	}

}