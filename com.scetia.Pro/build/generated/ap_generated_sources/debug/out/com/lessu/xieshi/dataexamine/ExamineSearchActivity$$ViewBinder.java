// Generated code from Butter Knife. Do not modify!
package com.lessu.xieshi.dataexamine;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Finder;
import butterknife.internal.ViewBinder;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class ExamineSearchActivity$$ViewBinder<T extends ExamineSearchActivity> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131231641, "method 'typeButtonDidClick'");
    unbinder.view2131231641 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.typeButtonDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131231267, "method 'projectButtonDidClick'");
    unbinder.view2131231267 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.projectButtonDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131230868, "method 'auditedButtonDidClick'");
    unbinder.view2131230868 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.auditedButtonDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131231338, "method 'searchButtonDidClick'");
    unbinder.view2131231338 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.searchButtonDidClick();
      }
    });
    return unbinder;
  }

  protected InnerUnbinder<T> createUnbinder(T target) {
    return new InnerUnbinder(target);
  }

  protected static class InnerUnbinder<T extends ExamineSearchActivity> implements Unbinder {
    private T target;

    View view2131231641;

    View view2131231267;

    View view2131230868;

    View view2131231338;

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
      view2131231641.setOnClickListener(null);
      view2131231267.setOnClickListener(null);
      view2131230868.setOnClickListener(null);
      view2131231338.setOnClickListener(null);
    }
  }
}
