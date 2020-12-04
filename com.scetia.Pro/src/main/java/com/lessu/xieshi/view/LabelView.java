package com.lessu.xieshi.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.lessu.foundation.DensityUtil;
import com.lessu.xieshi.R;

/**
 * created by ljs
 * on 2020/11/12
 */
public class LabelView extends AppCompatTextView {
    private int defWidth ;
    private int backgroundColor;
    private int angel;
    private Paint mbPaint;
    private Paint mTextPaint;
    private String contentText="";
    private int mContentTextColor;
    private float contentTextSize;
    public LabelView(Context context) {
        this(context,null);
    }

    public LabelView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LabelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.LabelView);
        defWidth = (int) arr.getDimension(R.styleable.LabelView_label_length, DensityUtil.dp2px(context,50));
        angel = arr.getInteger(R.styleable.LabelView_label_angle,2);
        backgroundColor = arr.getColor(R.styleable.LabelView_label_background_color,getResources().getColor(R.color.red_dark));
        contentText = arr.getString(R.styleable.LabelView_label_content_text);
        mContentTextColor =arr.getColor(R.styleable.LabelView_label_content_text_color,getResources().getColor(R.color.white));
        contentTextSize = arr.getDimension(R.styleable.LabelView_label_content_text_size,DensityUtil.dp2px(context,13));
        arr.recycle();
        init();
    }

    private void init(){
        mbPaint = new Paint();
        //获取显示的字体相关属性
        mTextPaint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(defWidth,defWidth);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        switch (angel){
            case 1:
                //左上角
                canvas.rotate(-90,defWidth/2,defWidth/2);
                break;
            case 3:
                //右下角
                canvas.rotate(90,defWidth/2,defWidth/2);
                break;
            case 4:
                //左下角
                canvas.rotate(180,defWidth/2,defWidth/2);
                break;
        }
        drawShape(canvas);
        drawLabelText(canvas);

    }


    /**
     * 开始绘制图形
     */
    private void drawShape(Canvas canvas) {
        mbPaint.setColor(backgroundColor);
        //绘制三角形背景
        Path path = new Path();
        path.moveTo(0,0);
        path.lineTo(defWidth,0);
        path.lineTo(defWidth,defWidth);
        //封闭图形
        path.close();
        canvas.drawPath(path,mbPaint);
    }
    /**
     * 绘制标签要显示的文字
     * @param canvas
     */
    private void drawLabelText(Canvas canvas) {
        mTextPaint.setColor(mContentTextColor);
        mTextPaint.setTextSize(contentTextSize);
        //开始绘制text
        if(angel==1||angel==2){
            //左上角或者右上角
            canvas.rotate(45,defWidth/2,defWidth/2);
            canvas.translate(0,-(defWidth/2-getTextSize()));
        }else{
            //右下角或者左下角
            canvas.rotate(-135,defWidth/2,defWidth/2);
            canvas.translate(0,defWidth/2-getTextSize());
        }
        int strWidth = (int) mTextPaint.measureText(contentText);
        //需要文字居中，减去文字宽度后除以2得到x坐标
        canvas.drawText(contentText,(defWidth-strWidth)/2,defWidth/2+getPaddingTop(),mTextPaint);
    }

    /**
     * 设置背景颜色
     * @param mBackgroundColor
     */
    public void setLabelBackground(@ColorRes int mBackgroundColor){
        backgroundColor  = getResources().getColor(mBackgroundColor);
    }

    /**
     * 要显示的文字
     * @param contextText
     */
    public void setLabelContextText(String contextText){
        this.contentText = contextText;
    }
}
