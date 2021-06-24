package com.scetia.app_sand.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FabScrollBehavior extends FloatingActionButton.Behavior {
    private boolean isAnimate = false;
    private int viewY;
    // 因为需要在布局xml中引用，所以必须实现该构造方法
    public FabScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        viewY= layoutParams.bottomMargin+child.getHeight();
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        //dy>0向上滑动，dy<0向下滑动
        if(dy>0&&!isAnimate&&child.getVisibility()==View.VISIBLE){
            animateOut2(child);
        }else if(dy<0&&!isAnimate&&child.getVisibility()==View.INVISIBLE){
            animateIn2(child);
        }
    }

    // FAB移出屏幕动画（隐藏动画）
    private void animateOut(final FloatingActionButton fab) {
        fab.animate().translationY(viewY).setInterpolator(new LinearInterpolator()).start();
    }

    // FAB移入屏幕动画（显示动画）
    private void animateIn(final FloatingActionButton fab) {
        fab.animate().translationY(0).setInterpolator(new LinearInterpolator()).start();
    }

    // FAB移出屏幕动画（隐藏动画）
    private void animateOut2(final FloatingActionButton fab) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(fab,"translationY",0,viewY);
        animator.setDuration(500);
        animator.setInterpolator(new LinearInterpolator());
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimate = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimate = false;
                fab.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }
    // FAB移入屏幕动画（显示动画）
    private void animateIn2(final FloatingActionButton fab) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(fab,"translationY",viewY,0);
        animator.setDuration(500);
        animator.setInterpolator(new LinearInterpolator());
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimate = true;
                fab.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimate = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();

    }

}
