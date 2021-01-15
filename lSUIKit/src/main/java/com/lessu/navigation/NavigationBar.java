package com.lessu.navigation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scetia.Pro.common.Util.DensityUtil;

/**
 * Created by lessu on 14-7-31.
 */
public class NavigationBar extends RelativeLayout {
    protected int barTintColor;
    protected int tintColor;
    private LinearLayout leftBarLayout;
    private LinearLayout middleBarLayout;
    private LinearLayout rightBarLayout;
    private TextView titleView;
    private Paint paint;

    public NavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NavigationBar(Context context) {
        super(context);
        init();

    }

    public void init(){
        this.barTintColor = 0xFFF0F0F0;
        this.tintColor    = Color.WHITE;

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(getContext(), 49));
        //layoutParams.bottomMargin = 4;
        this.setLayoutParams(layoutParams);
        this.setBackgroundColor(this.barTintColor);


        leftBarLayout = new LinearLayout(getContext());
        middleBarLayout = new LinearLayout(getContext());
        rightBarLayout = new LinearLayout(getContext());

        LayoutParams leftLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        leftLayoutParams.alignWithParent = true;
        leftLayoutParams.addRule(ALIGN_PARENT_LEFT);

        leftBarLayout.setLayoutParams(leftLayoutParams);
        leftBarLayout.setMinimumWidth(DensityUtil.dip2px(getContext(), 15));
        leftBarLayout.setGravity(Gravity.CENTER_HORIZONTAL);


        LayoutParams middleLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        middleBarLayout.setLayoutParams(middleLayoutParams);

        LayoutParams rightLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rightLayoutParams.alignWithParent = true;
        rightLayoutParams.addRule(ALIGN_PARENT_RIGHT);
        rightBarLayout.setLayoutParams(rightLayoutParams);
        rightBarLayout.setMinimumWidth(DensityUtil.dip2px(getContext(),15));
        rightBarLayout.setGravity(Gravity.CENTER_HORIZONTAL);

        this.addView(leftBarLayout);
        this.addView(middleBarLayout);
        this.addView(rightBarLayout);


        titleView = new TextView(getContext());
        titleView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        titleView.setTextColor(tintColor);
        titleView.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
        titleView.setTextSize(18);

        middleBarLayout.addView(titleView);

        paint = new Paint();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       /* paint.setColor(0xFFAAAAAA);
        paint.setStrokeWidth(1);
        canvas.drawLine(0,canvas.getHeight() -1 ,canvas.getWidth(),canvas.getHeight() - 1,paint);
        paint.setColor(0xFFFFFFFF);
        paint.setStrokeWidth(1);
        canvas.drawLine(0,canvas.getHeight() -2 ,canvas.getWidth(),canvas.getHeight() - 2,paint);*/
    }

    CharSequence        title;
    public void setTitle(CharSequence text){
    	if(titleView.getParent() != middleBarLayout){
    		middleBarLayout.removeAllViews();
    		middleBarLayout.addView(titleView);
    	}
        title = text;
        titleView.setText(text);
    }
    public CharSequence getTitle() {
        return title;
    }
    View centerTitleView;
    public void setTitleView(View titleView){
    	centerTitleView = titleView;
    	middleBarLayout.removeAllViews();
    	middleBarLayout.addView(titleView);
    }
    public View getTitleView(){
    	return centerTitleView;
    }
    public void setLeftBarItem(BarButtonItem item){
    	if(item == null){
    		leftBarLayout.removeAllViews();
    	}else{
    		leftBarLayout.removeAllViews();
	        leftBarLayout.addView(item);
	        item.setTintColor(tintColor);
    	}
    }
    public void setMidBarItem(View item){
        if(item == null){
            middleBarLayout.removeAllViews();
        }else{
            middleBarLayout.removeAllViews();
            middleBarLayout.addView(item);
        }
    }
	public void setRightBarItem(BarButtonItem item){
		if(item == null){
			rightBarLayout.removeAllViews();
		}else{
			rightBarLayout.removeAllViews();
	        rightBarLayout.addView(item);
	        item.setTintColor(tintColor);
		}
	}
    public void addRightBarItem(BarButtonItem item){
        if(item == null){
            rightBarLayout.removeAllViews();
        }else{
            rightBarLayout.addView(item);
            item.setTintColor(tintColor);
        }
    }

//    @property(nonatomic,assign) UIBarStyle barStyle;
//    @property(nonatomic,assign) id delegate;


//    @property(nonatomic,assign,getter=isTranslucent) BOOL translucent NS_AVAILABLE_IOS(3_0); // Default is NO on iOS 6 and earlier. Always YES if barStyle is set to UIBarStyleBlackTranslucent


//    - (void)pushNavigationItem:(UINavigationItem *)item animated:(BOOL)animated;
//    - (UINavigationItem *)popNavigationItemAnimated:(BOOL)animated; // Returns the item that was popped.

//    @property(nonatomic,readonly,retain) UINavigationItem *topItem;
//    @property(nonatomic,readonly,retain) UINavigationItem *backItem;

//    @property(nonatomic,copy) NSArray *items;
//    - (void)setItems:(NSArray *)items animated:(BOOL)animated; // If animated is YES, then simulate a push or pop depending on whether the new top item was previously in the stack.

//    - (void)setBackgroundImage:(UIImage *)backgroundImage forBarPosition:(UIBarPosition)barPosition barMetrics:(UIBarMetrics)barMetrics NS_AVAILABLE_IOS(7_0) UI_APPEARANCE_SELECTOR;
//    - (UIImage *)backgroundImageForBarPosition:(UIBarPosition)barPosition barMetrics:(UIBarMetrics)barMetrics NS_AVAILABLE_IOS(7_0) UI_APPEARANCE_SELECTOR;

//    - (void)setBackgroundImage:(UIImage *)backgroundImage forBarMetrics:(UIBarMetrics)barMetrics NS_AVAILABLE_IOS(5_0) UI_APPEARANCE_SELECTOR;
//    - (UIImage *)backgroundImageForBarMetrics:(UIBarMetrics)barMetrics NS_AVAILABLE_IOS(5_0) UI_APPEARANCE_SELECTOR;

//    @property(nonatomic,retain) UIImage *shadowImage NS_AVAILABLE_IOS(6_0) UI_APPEARANCE_SELECTOR;

/* You may specify the font, text color, and shadow properties for the title in the text attributes dictionary, using the keys found in NSAttributedString.h.
 */
//    @property(nonatomic,copy) NSDictionary *titleTextAttributes NS_AVAILABLE_IOS(5_0) UI_APPEARANCE_SELECTOR;

//    - (void)setTitleVerticalPositionAdjustment:(CGFloat)adjustment forBarMetrics:(UIBarMetrics)barMetrics NS_AVAILABLE_IOS(5_0) UI_APPEARANCE_SELECTOR;
//    - (CGFloat)titleVerticalPositionAdjustmentForBarMetrics:(UIBarMetrics)barMetrics NS_AVAILABLE_IOS(5_0) UI_APPEARANCE_SELECTOR;

/*
 The back indicator image is shown beside the back button.
 The back indicator transition mask image is used as a mask for content during push and pop transitions
 Note: These properties must both be set if you want to customize the back indicator image.
 */
//    @property(nonatomic,retain) UIImage *backIndicatorImage NS_AVAILABLE_IOS(7_0) UI_APPEARANCE_SELECTOR;
//    @property(nonatomic,retain) UIImage *backIndicatorTransitionMaskImage NS_AVAILABLE_IOS(7_0) UI_APPEARANCE_SELECTOR;

//    @protocol UINavigationBarDelegate <UIBarPositioningDelegate>
//
//    @optional
//
//    - (BOOL)navigationBar:(UINavigationBar *)navigationBar shouldPushItem:(UINavigationItem *)item; // called to push. return NO not to.
//    - (void)navigationBar:(UINavigationBar *)navigationBar didPushItem:(UINavigationItem *)item;    // called at end of animation of push or immediately if not animated
//    - (BOOL)navigationBar:(UINavigationBar *)navigationBar shouldPopItem:(UINavigationItem *)item;  // same as push methods
//    - (void)navigationBar:(UINavigationBar *)navigationBar didPopItem:(UINavigationItem *)item;
//
//    @end

                 // Title when topmost on the stack. default is nil
//    @property(nonatomic,retain) UIBarButtonItem *backBarButtonItem; // Bar button item to use for the back button in the child navigation item.
//    @property(nonatomic,retain) UIView          *titleView;         // Custom view to use in lieu of a title. May be sized horizontally. Only used when item is topmost on the stack.

//    @property(nonatomic,copy)   NSString *prompt;     // Explanatory text to display above the navigation bar buttons.

//    @property(nonatomic,assign) BOOL hidesBackButton; // If YES, this navigation item will hide the back button when it's on top of the stack.
//    - (void)setHidesBackButton:(BOOL)hidesBackButton animated:(BOOL)animated;


//    @property(nonatomic,copy) NSArray *leftBarButtonItems NS_AVAILABLE_IOS(5_0);
//    @property(nonatomic,copy) NSArray *rightBarButtonItems NS_AVAILABLE_IOS(5_0);
//    - (void)setLeftBarButtonItems:(NSArray *)items animated:(BOOL)animated NS_AVAILABLE_IOS(5_0);
//    - (void)setRightBarButtonItems:(NSArray *)items animated:(BOOL)animated NS_AVAILABLE_IOS(5_0);

//    @property(nonatomic) BOOL leftItemsSupplementBackButton NS_AVAILABLE_IOS(5_0);

//    @property(nonatomic,retain) UIBarButtonItem *leftBarButtonItem;
//    @property(nonatomic,retain) UIBarButtonItem *rightBarButtonItem;
//    - (void)setLeftBarButtonItem:(UIBarButtonItem *)item animated:(BOOL)animated;
//    - (void)setRightBarButtonItem:(UIBarButtonItem *)item animated:(BOOL)animated;

}
