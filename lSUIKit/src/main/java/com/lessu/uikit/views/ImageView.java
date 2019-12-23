package com.lessu.uikit.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;

import com.lessu.uikit.R;

/**
 * Created by lessu on 14-7-2.
 */
public class ImageView extends android.widget.ImageView{
    private Paint mMaskPaint;
    private Path mMaskPath;
    private float mCornerRadius;

    public ImageView(Context context) {
        super(context);
        mCornerRadius = 0;
        init();
    }

    public ImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ImageView);
        mCornerRadius = array.getDimension(R.styleable.ImageView_corner_radius,0);
        init();
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void init() {

        this.mMaskPaint= new Paint(Paint.ANTI_ALIAS_FLAG);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        this.mMaskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    private void generateMaskPath(int width, int height) {
        this.mMaskPath = new Path();
        this.mMaskPath.addRoundRect(new RectF(0.0F, 0.0F, width, height), this.mCornerRadius, this.mCornerRadius, Path.Direction.CW);
        this.mMaskPath.setFillType(Path.FillType.INVERSE_WINDING);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);
        if ((w != oldw) || (h != oldh))
            generateMaskPath(w, h);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        // 保存当前layer的透明橡树到离屏缓冲区。并新创建一个透明度爲255的新layer
        int saveCount = canvas.saveLayerAlpha(0.0F, 0.0F, canvas.getWidth(), canvas.getHeight() , 255, Canvas.HAS_ALPHA_LAYER_SAVE_FLAG);
        super.onDraw(canvas);
        if (this.mMaskPath != null) {
            canvas.drawPath(this.mMaskPath, this.mMaskPaint);
        }
        canvas.restoreToCount(saveCount);

//        super.onDraw(canvas);
    }

    public float getCornerRadius() {
        return mCornerRadius;
    }

    public void setCornerRadius(float mCornerRadius) {
        this.mCornerRadius = mCornerRadius;
    }
}
