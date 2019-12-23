package com.lessu.xieshi.tianqi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * 一周天气预报
 * 按文字算高度18行
 * 文字设置为12，高度是216dp
 * @author fhm
 *
 */
public class DailyForecastView extends View {

	private int width, height;
	private float percent = 0f;
	private final float density;
	private ArrayList<DailyForecast> forecastList=new ArrayList<>();
	private Path tmpMaxPath = new Path();
	private Path tmpMinPath = new Path();
	private Data[] datas;
	private final TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private final TextPaint paintbig = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private DailyForecast forecast;
	private Scroller scroller;
	private float dW;
	private Paint circlePaint;
	private float y;
	private float lastY;
	private int zhexianheight;

	public class Data {
		public float minOffsetPercent, maxOffsetPercent;// 差值%
		public int tmp_max, tmp_min;
		public String date;
		public String wind_sc;
		public String cond_txt_d;

		public int picup;
		public int picdown;

	}

	public DailyForecastView(Context context) {
		this(context,null);
	}
	public DailyForecastView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	public DailyForecastView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		scroller = new Scroller(context);
		viewConfiguration = ViewConfiguration.get(context);
		screenWidth = getResources().getDisplayMetrics().widthPixels;
		screenHeight = getResources().getDisplayMetrics().heightPixels;

		density = context.getResources().getDisplayMetrics().density;
		if(isInEditMode()){
			return ;
		}
		init(context);
	}

	public void resetAnimation(){
		percent = 0f;
		invalidate();
	}

	private void init(Context context) {
		System.out.println("density......"+density);
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(1f * density);
		//paint.setTextSize(12f * density);
		paint.setStyle(Style.FILL);
		paint.setTextAlign(Align.CENTER);
		//paint.setTypeface(MainActivity.getTypeface(context));



		paintbig.setColor(Color.WHITE);
		paintbig.setStrokeWidth(1f * density);
		//paintbig.setTextSize(16f * density);
		paintbig.setStyle(Style.FILL);
		paintbig.setTextAlign(Align.CENTER);

		circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		circlePaint.setStrokeWidth(1f * density);
		circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
		circlePaint.setColor(Color.WHITE);
	}
	//216dp 18hang
	@Override
	protected void onDraw(Canvas canvas) {
		// super.onDraw(canvas);
		if(isInEditMode()){
			return ;
		}
		paint.setStyle(Style.FILL);
		//一共需要 顶部文字2(+图占8行)+底部文字2 + 【间距1 + 日期1 + 间距0.5 +　晴1 + 间距0.5f + 微风1 + 底部边距1f 】 = 18行
		//                                  12     13       14      14.5    15.5      16      17       18

		//一共需要 间距4+周日2+间距1+日期1.2+间距4+上方图标2+上方文字2+ (图占8行)+下方文字2+下方图标2+间距5+东风1.2+间距1.8+3级1.2+间距17  =54.4行
		//           4     6    7   8.2   12.2     14.2     16.2    24.2     26.2     28.2   33.2  34.4     36.2  37.4    54.4
		//final float textSize = this.height / 18f;//改 111111
		final float textSize = this.height / 54.4f;//改 111111
		//如果高度大于宽度，textsize取一个定制，否则取动态的
		if(screenWidth>this.height){
			paint.setTextSize(textSize);
			paintbig.setTextSize((textSize/density+2)*density);
		}else{
			paint.setTextSize(13*density);
			paintbig.setTextSize(16*density);
		}


		//paint.setTextSize(textSize);
		final float textOffset = getTextPaintOffset(paint);
		final float dH = textSize * 8f;
		//final float dCenterY = textSize * 6f ;//中点加上上方的字的总距离   改2222222222
		final float dCenterY = textSize * 20.2f ;//中点加上上方的字的总距离   改2222222222
		if (datas == null || datas.length <= 1) {
			canvas.drawLine(0, dCenterY, this.width, dCenterY, paint);//没有数据的情况下只画一条线
			return;
		}
		//final float dW = this.width * 1f / datas.length;
		//dW = this.width * 1f /7;
		dW=viewWidth*1f/forecastList.size();
		tmpMaxPath.reset();
		tmpMinPath.reset();
		final int length = datas.length;
		float[] x = new float[length];
		float[] yMax = new float[length];
		float[] yMin = new float[length];

		final float textPercent = (percent >= 0.6f) ? ((percent - 0.6f) / 0.4f) : 0f;
		final float pathPercent = (percent >= 0.6f) ? 1f : (percent / 0.6f);

		//画底部的三行文字和标注最高最低温度
		//paint.setAlpha((int) (255 * textPercent));
		for (int i = 0; i < length; i++) {
			final Data d = datas[i];
			x[i] = i * dW + dW / 2f;
			yMax[i] = dCenterY - d.maxOffsetPercent * dH;
			yMin[i] = dCenterY - d.minOffsetPercent * dH;

//改333333333333333333333333333333333333
//			canvas.drawText(d.tmp_max + "°", x[i], yMax[i] - textSize + textOffset, paint);// - textSize
//			canvas.drawText(d.tmp_min + "°", x[i], yMin[i] + textSize  + textOffset, paint);
//			canvas.drawText(ApiManager.prettyDate(d.date), x[i], textSize * 13.5f + textOffset, paint);//日期d.date.substring(5)
//			canvas.drawText(d.cond_txt_d + "", x[i], textSize * 15f + textOffset, paint);//“晴"
//			canvas.drawText(d.wind_sc, x[i],textSize * 16.5f + textOffset, paint);//微风

			int picup = d.picup;
			InputStream is = getResources().openRawResource(picup);
			Bitmap bitmapup = BitmapFactory.decodeStream(is);

			int picdown = d.picdown;
			InputStream is1 = getResources().openRawResource(picdown);
			Bitmap bitmapdown = BitmapFactory.decodeStream(is1);
			canvas.drawBitmap(bitmapup, x[i]-bitmapup.getWidth()/2, yMax[i] - 7*textSize + textOffset,  paint);
			canvas.drawBitmap(bitmapdown, x[i]-bitmapup.getWidth()/2, yMin[i] + 3*textSize + textOffset,  paint);

			canvas.drawText(d.tmp_max + "°", x[i], yMax[i] - 2*textSize + textOffset, paint);// - textSize
			canvas.drawText(d.tmp_min + "°", x[i], yMin[i] + 2*textSize  + textOffset, paint);
			canvas.drawText(ApiManager.prettyDate(d.date), x[i], textSize * 5f + textOffset, paintbig);//日期d.date.substring(5)
			String data=d.date;
			if(d.date.contains("-")){
				String[] split = d.date.split("-");
				String month = split[1];
				String day = split[2];
				data=month+"/"+day;
			}
			String fenstring=d.cond_txt_d;
			String jistring=d.wind_sc;
if(fenstring.length()>=5){
	fenstring=fenstring.substring(0,5);
}
			if(jistring.length()>=5){
				jistring=jistring.substring(0,5);
			}
			canvas.drawText(data, x[i], textSize * 7.6f + textOffset, paint);//9/27
			//canvas.drawText(fenstring + "", x[i], textSize * 33.8f + textOffset, paint);//“东风"
			//canvas.drawText(jistring, x[i],textSize * 36.8f + textOffset, paint);//3级
			StaticLayout sl = new StaticLayout(d.cond_txt_d,paint, (int) (screenWidth/6.2), Layout.Alignment.ALIGN_NORMAL,1.0f,0.0f,true);
//从0,0开始绘制
			canvas.save();
			canvas.translate( x[i],textSize * 33.8f + textOffset);
			sl.draw(canvas);
			canvas.restore();
			StaticLayout sl1 = new StaticLayout(d.wind_sc,paint, (int) (screenWidth/6.2), Layout.Alignment.ALIGN_NORMAL,1.0f,0.0f,true);
//从0,0开始绘制
			canvas.save();
			canvas.translate( x[i],textSize * 38.8f + textOffset);
			sl1.draw(canvas);
			canvas.restore();

		}
		paint.setAlpha(255);
		for (int i = 0; i < (length - 1); i++) {
			final float midX = (x[i] + x[i + 1]) / 2f;
			final float midYMax = (yMax[i] + yMax[i + 1]) / 2f;
			final float midYMin = (yMin[i] + yMin[i + 1]) / 2f;
			if(i == 0){
				tmpMaxPath.moveTo(x[i], yMax[i]);
				tmpMinPath.moveTo(x[i], yMin[i]);
			}
			tmpMaxPath.cubicTo(x[i], yMax[i], x[i], yMax[i], x[i + 1], yMax[i + 1]);
			tmpMinPath.cubicTo(x[i], yMin[i], x[i], yMin[i], x[i + 1], yMin[i + 1]);
		}
//画点
		for (int i = 0; i < (length ); i++) {
			canvas.drawCircle(x[i], yMax[i],
					8 ,
					circlePaint);
			canvas.drawCircle(x[i], yMin[i],
					8 ,
					circlePaint);

		}

		//draw max_tmp and min_tmp path
		paint.setStyle(Style.STROKE);
		final boolean needClip = pathPercent < 1f;
		if(needClip){
			canvas.save();
			canvas.clipRect( 0 , 0, this.width * pathPercent, this.height);
		}
		canvas.drawPath(tmpMaxPath, paint);
		canvas.drawPath(tmpMinPath, paint);
		if(needClip){
			canvas.restore();
		}
		if(percent < 1){
			percent += 0.025f;// 0.025f;
			percent = Math.min(percent, 1f);
			//ViewCompat.postInvalidateOnAnimation(this);
		}


	}

	public void setData(ArrayList<DailyForecast> mforecastList,int zhexianheight) {
		this.zhexianheight=zhexianheight;
		this.forecastList=mforecastList;
		if (this.forecastList == null && this.forecastList.size() == 0) {
			return;
		}
		// this.points = new PointF[forecastList.size()];
		datas = new Data[this.forecastList.size()];
		try {
			int all_max = Integer.MIN_VALUE;
			int all_min = Integer.MAX_VALUE;
			for (int i = 0; i < this.forecastList.size(); i++) {
				DailyForecast forecast =  this.forecastList.get(i);
//				int max = Integer.valueOf(forecast.maxtemp);
//				int min = Integer.valueOf(forecast.mintemp);
				int max = forecast.maxtemp;
				int min = forecast.mintemp;
				if (all_max < max) {
					all_max = max;
				}
				if (all_min > min) {
					all_min = min;
				}
				final Data data = new Data();
				data.tmp_max = max;
				data.tmp_min = min;
				data.date = forecast.date;
				data.wind_sc = forecast.wind;
				data.cond_txt_d = forecast.tianqi;
				data.picup = forecast.picup;
				data.picdown = forecast.picdown;

				datas[i] = data;
			}
			float all_distance = Math.abs(all_max - all_min);
			float average_distance = (all_max + all_min) / 2f;
//		toast("all->" + all_distance + " aver->" + average_distance);
			for (Data d : datas) {
				d.maxOffsetPercent = (d.tmp_max - average_distance) / all_distance;
				d.minOffsetPercent = (d.tmp_min - average_distance) / all_distance;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		percent = 0f;
		invalidate();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		System.out.println("w............"+w);
		this.width = w;
		this.height = h;
	}

	public static float getTextPaintOffset(Paint paint) {
		FontMetrics fontMetrics = paint.getFontMetrics();
		return -(fontMetrics.bottom - fontMetrics.top) / 2f - fontMetrics.top;
	}



	private float lastX = 0;
	private float x = 0;
	private VelocityTracker velocityTracker;
	private int viewHeight;
	private int viewWidth;
	private int screenWidth;
	private int screenHeight;
	//private int minViewHeight=216;
	private int minViewHeight=zhexianheight;
	//这里改成240  216/18*20
	private ViewConfiguration viewConfiguration;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		getParent().requestDisallowInterceptTouchEvent(true);
		if (velocityTracker == null) {
			velocityTracker = VelocityTracker.obtain();
		}
		velocityTracker.addMovement(event);
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (!scroller.isFinished()) {  //fling还没结束
					scroller.abortAnimation();
				}
				lastX = x = event.getX();
				lastY = event.getY();
				return true;
			case MotionEvent.ACTION_MOVE:
				getParent().requestDisallowInterceptTouchEvent(true);

				//System.out.println("move................................");
				x = event.getX();
				y=event.getY();
				if(y-lastY!=0){
					getParent().requestDisallowInterceptTouchEvent(true);
				}else{
					getParent().requestDisallowInterceptTouchEvent(false);
				}

				int deltaX = (int) (lastX - x);
				if (getScrollX() + deltaX < 0) {    //越界恢复
					scrollTo(0, 0);
					return true;//
				} else if (getScrollX() + deltaX > viewWidth - screenWidth) {
					scrollTo(viewWidth - screenWidth, 0);
					return true;
				}
				scrollBy(deltaX, 0);
				lastX = x;

				break;
			case MotionEvent.ACTION_UP:
				x = event.getX();
				velocityTracker.computeCurrentVelocity(1000);  //计算1秒内滑动过多少像素
				int xVelocity = (int) velocityTracker.getXVelocity();
				if (Math.abs(xVelocity) > viewConfiguration.getScaledMinimumFlingVelocity()) {  //滑动速度可被判定为抛动
					scroller.fling(getScrollX(), 0, -xVelocity, 0, 0, viewWidth - screenWidth, 0, 0);
					invalidate();
				}
				break;
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void computeScroll() {
		super.computeScroll();
		if (scroller.computeScrollOffset()) {
			scrollTo(scroller.getCurrX(), scroller.getCurrY());
			invalidate();
		}
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec,  heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		if (heightMode == MeasureSpec.EXACTLY) {
			viewHeight = Math.max(heightSize, minViewHeight);
		} else {
			viewHeight = minViewHeight;
		}
		int totalWidth = 0;
		//System.out.println("width........."+width);
		//System.out.println("forecastList.size()........."+forecastList.size());
		if (forecastList.size() > 1) {
			//totalWidth =  (this.width/6*forecastList.size());
			totalWidth =  (screenWidth/6*forecastList.size());
		}else{
			totalWidth =(screenWidth/6*10);
		}
		System.out.println(screenWidth+"SADDDDDDDSDSDSD........"+totalWidth+"------------"+dW);
		viewWidth = Math.max(screenWidth, totalWidth);  //默认控件最小宽度为屏幕宽度
		setMeasuredDimension(viewWidth, viewHeight);
		setMeasuredDimension(viewWidth, viewHeight);
	}
}
