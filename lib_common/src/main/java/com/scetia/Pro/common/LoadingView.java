package com.scetia.Pro.common;

import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleObserver;

public class LoadingView extends View  {
    private Paint paint;
    private RectF rectF;
    private int paintStorkWidth = 10;
    private final int animateTime = 800;
    private float progress;
    private boolean isStart = false;

    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private  void init(){
        paint = new Paint();
        //线条
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(paintStorkWidth);
        rectF = new RectF();
    }

    public void setColor(@ColorInt int color){
        paint.setColor(color);
    }

    public void setColor(String color){
        paint.setColor(Color.parseColor(color));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rectF.set(paintStorkWidth,paintStorkWidth,getWidth()-paintStorkWidth,getHeight()-paintStorkWidth);
        canvas.drawArc(rectF,10+(80*progress),160-(80*progress*2),false,paint);
        canvas.drawArc(rectF,190+(80*progress),160-(80*progress*2),false,paint);
        //因为不断的刷新重绘，这里保证动画只开启一次
        if(!isStart){
            isStart = true;
            startRotateWithScale();
        }
    }

    private void startRotateWithScale() {
        final AnimatorSet rotateAnimationSet = new AnimatorSet();
        //旋转动画，这里使用的是旋转当前整个view
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(this, "rotation", 0, 360);
        rotationAnimator.setDuration(animateTime);
        rotationAnimator.setInterpolator(new LinearInterpolator());
        //让动画不断的重复执行
        rotationAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        rotationAnimator.setRepeatMode(ObjectAnimator.RESTART);

        Keyframe keyframe1 = Keyframe.ofFloat(0f, 1.0f);
        Keyframe keyframe2 = Keyframe.ofFloat(0.9f, 1.0f);
        Keyframe keyframe3 = Keyframe.ofFloat(1f, 0f);
        PropertyValuesHolder frameHodlerX = PropertyValuesHolder.ofKeyframe("scaleX",keyframe1,keyframe2,keyframe3);
        ObjectAnimator animatorX = ObjectAnimator.ofPropertyValuesHolder(this, frameHodlerX);
        animatorX.setDuration(2000);
        animatorX.setRepeatCount(ValueAnimator.INFINITE);
        animatorX.setRepeatMode(ValueAnimator.REVERSE);

        PropertyValuesHolder frameHodlerY = PropertyValuesHolder.ofKeyframe("scaleY",keyframe1,keyframe2,keyframe3);
        ObjectAnimator animatorY = ObjectAnimator.ofPropertyValuesHolder(this, frameHodlerY);
        animatorY.setDuration(2000);
        animatorY.setRepeatCount(ValueAnimator.INFINITE);
        animatorY.setRepeatMode(ValueAnimator.REVERSE);

        //圆弧缩短的动画
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.setDuration(animateTime);
        //让动画执行完后，反向执行
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        rotateAnimationSet.playTogether(
                valueAnimator, rotationAnimator,animatorX,animatorY
        );
        rotateAnimationSet.start();
    }

}
