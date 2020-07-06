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

public class ServiceActivity$$ViewBinder<T extends ServiceActivity> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131231246, "method 'telecomButtonDidClick'");
    unbinder.view2131231246 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.telecomButtonDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131231490, "method 'unicomButtonDidClick'");
    unbinder.view2131231490 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.unicomButtonDidClick();
      }
    });
    return unbinder;
  }

  protected InnerUnbinder<T> createUnbinder(T target) {
    return new InnerUnbinder(target);
  }

  protected static class InnerUnbinder<T extends ServiceActivity> implements Unbinder {
    private T target;

    View view2131231246;

    View view2131231490;

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
      view2131231246.setOnClickListener(null);
      view2131231490.setOnClickListener(null);
    }
  }
}
