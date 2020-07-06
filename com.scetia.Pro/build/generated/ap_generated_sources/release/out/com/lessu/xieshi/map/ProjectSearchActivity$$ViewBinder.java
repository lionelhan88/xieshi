// Generated code from Butter Knife. Do not modify!
package com.lessu.xieshi.map;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Finder;
import butterknife.internal.ViewBinder;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class ProjectSearchActivity$$ViewBinder<T extends ProjectSearchActivity> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131231157, "method 'projectButtonDidClick'");
    unbinder.view2131231157 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.projectButtonDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131231255, "method 'timeButtonDidClick'");
    unbinder.view2131231255 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.timeButtonDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131231204, "method 'searchButtonDidClick'");
    unbinder.view2131231204 = view;
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

  protected static class InnerUnbinder<T extends ProjectSearchActivity> implements Unbinder {
    private T target;

    View view2131231157;

    View view2131231255;

    View view2131231204;

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
      view2131231157.setOnClickListener(null);
      view2131231255.setOnClickListener(null);
      view2131231204.setOnClickListener(null);
    }
  }
}
