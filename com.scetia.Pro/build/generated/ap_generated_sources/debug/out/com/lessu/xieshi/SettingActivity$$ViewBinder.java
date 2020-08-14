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
    view = finder.findRequiredView(source, 2131231078, "method 'jiechuButtonDidClick'");
    unbinder.view2131231078 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.jiechuButtonDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131231253, "method 'passwordButtonDidClick'");
    unbinder.view2131231253 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.passwordButtonDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131231356, "method 'serviceButtonDidClick'");
    unbinder.view2131231356 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.serviceButtonDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131230906, "method 'resetLogin'");
    unbinder.view2131230906 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.resetLogin();
      }
    });
    view = finder.findRequiredView(source, 2131231649, "method 'updateButtonDidClick'");
    unbinder.view2131231649 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.updateButtonDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131231330, "method 'scanLogin'");
    unbinder.view2131231330 = view;
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

    View view2131231078;

    View view2131231253;

    View view2131231356;

    View view2131230906;

    View view2131231649;

    View view2131231330;

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
      view2131231078.setOnClickListener(null);
      view2131231253.setOnClickListener(null);
      view2131231356.setOnClickListener(null);
      view2131230906.setOnClickListener(null);
      view2131231649.setOnClickListener(null);
      view2131231330.setOnClickListener(null);
    }
  }
}
