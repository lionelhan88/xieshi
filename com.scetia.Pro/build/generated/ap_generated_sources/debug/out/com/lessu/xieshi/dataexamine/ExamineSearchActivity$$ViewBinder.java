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
    view = finder.findRequiredView(source, 2131231489, "method 'typeButtonDidClick'");
    unbinder.view2131231489 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.typeButtonDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131231158, "method 'projectButtonDidClick'");
    unbinder.view2131231158 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.projectButtonDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131230858, "method 'auditedButtonDidClick'");
    unbinder.view2131230858 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.auditedButtonDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131231205, "method 'searchButtonDidClick'");
    unbinder.view2131231205 = view;
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

    View view2131231489;

    View view2131231158;

    View view2131230858;

    View view2131231205;

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
      view2131231489.setOnClickListener(null);
      view2131231158.setOnClickListener(null);
      view2131230858.setOnClickListener(null);
      view2131231205.setOnClickListener(null);
    }
  }
}
