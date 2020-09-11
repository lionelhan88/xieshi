// Generated code from Butter Knife. Do not modify!
package com.lessu.xieshi.scan.fragment;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Finder;
import butterknife.internal.ViewBinder;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class PrintDataFragment$$ViewBinder<T extends PrintDataFragment> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131231477, "field 'tvBaocun' and method 'onViewClicked'");
    target.tvBaocun = finder.castView(view, 2131231477, "field 'tvBaocun'");
    unbinder.view2131231477 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = finder.findRequiredView(source, 2131231495, "field 'tvDuqv' and method 'onViewClicked'");
    target.tvDuqv = finder.castView(view, 2131231495, "field 'tvDuqv'");
    unbinder.view2131231495 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = finder.findRequiredView(source, 2131231604, "field 'tvQingchu' and method 'onViewClicked'");
    target.tvQingchu = finder.castView(view, 2131231604, "field 'tvQingchu'");
    unbinder.view2131231604 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = finder.findRequiredView(source, 2131231629, "field 'tvShujvjiaohu' and method 'onViewClicked'");
    target.tvShujvjiaohu = finder.castView(view, 2131231629, "field 'tvShujvjiaohu'");
    unbinder.view2131231629 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = finder.findRequiredView(source, 2131231635, "field 'tvTiaomaNum' and method 'onViewClicked'");
    target.tvTiaomaNum = finder.castView(view, 2131231635, "field 'tvTiaomaNum'");
    unbinder.view2131231635 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = finder.findRequiredView(source, 2131231658, "field 'tvXinpianNum' and method 'onViewClicked'");
    target.tvXinpianNum = finder.castView(view, 2131231658, "field 'tvXinpianNum'");
    unbinder.view2131231658 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = finder.findRequiredView(source, 2131231170, "field 'lvTiaoma' and method 'onViewClicked'");
    target.lvTiaoma = finder.castView(view, 2131231170, "field 'lvTiaoma'");
    unbinder.view2131231170 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = finder.findRequiredView(source, 2131231174, "field 'lvXinpian' and method 'onViewClicked'");
    target.lvXinpian = finder.castView(view, 2131231174, "field 'lvXinpian'");
    unbinder.view2131231174 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    return unbinder;
  }

  protected InnerUnbinder<T> createUnbinder(T target) {
    return new InnerUnbinder(target);
  }

  protected static class InnerUnbinder<T extends PrintDataFragment> implements Unbinder {
    private T target;

    View view2131231477;

    View view2131231495;

    View view2131231604;

    View view2131231629;

    View view2131231635;

    View view2131231658;

    View view2131231170;

    View view2131231174;

    protected InnerUnbinder(T target) {
      this.target = target;
    }

    @Override
    public final void unbind() {
      if (target == null) throw new IllegalStateException("Bindings already cleared.");
      unbind(target);
      target = null;
    }

    protected void unbind(T target) {
      view2131231477.setOnClickListener(null);
      target.tvBaocun = null;
      view2131231495.setOnClickListener(null);
      target.tvDuqv = null;
      view2131231604.setOnClickListener(null);
      target.tvQingchu = null;
      view2131231629.setOnClickListener(null);
      target.tvShujvjiaohu = null;
      view2131231635.setOnClickListener(null);
      target.tvTiaomaNum = null;
      view2131231658.setOnClickListener(null);
      target.tvXinpianNum = null;
      view2131231170.setOnClickListener(null);
      target.lvTiaoma = null;
      view2131231174.setOnClickListener(null);
      target.lvXinpian = null;
    }
  }
}
