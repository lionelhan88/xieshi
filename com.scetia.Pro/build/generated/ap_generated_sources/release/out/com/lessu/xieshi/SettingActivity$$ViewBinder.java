// Generated code from Butter Knife. Do not modify!
package com.lessu.xieshi;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Finder;
import butterknife.internal.ViewBinder;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class SettingActivity$$ViewBinder<T extends SettingActivity> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131231028, "method 'jiechuButtonDidClick'");
    unbinder.view2131231028 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.jiechuButtonDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131231147, "method 'passwordButtonDidClick'");
    unbinder.view2131231147 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.passwordButtonDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131231221, "method 'serviceButtonDidClick'");
    unbinder.view2131231221 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.serviceButtonDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131230887, "method 'resetLogin'");
    unbinder.view2131230887 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.resetLogin();
      }
    });
    view = finder.findRequiredView(source, 2131231494, "method 'updateButtonDidClick'");
    unbinder.view2131231494 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.updateButtonDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131231200, "method 'scanLogin'");
    unbinder.view2131231200 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.scanLogin();
      }
    });
    return unbinder;
  }

  protected InnerUnbinder<T> createUnbinder(T target) {
    return new InnerUnbinder(target);
  }

  protected static class InnerUnbinder<T extends SettingActivity> implements Unbinder {
    private T target;

    View view2131231028;

    View view2131231147;

    View view2131231221;

    View view2131230887;

    View view2131231494;

    View view2131231200;

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
      view2131231028.setOnClickListener(null);
      view2131231147.setOnClickListener(null);
      view2131231221.setOnClickListener(null);
      view2131230887.setOnClickListener(null);
      view2131231494.setOnClickListener(null);
      view2131231200.setOnClickListener(null);
    }
  }
}
