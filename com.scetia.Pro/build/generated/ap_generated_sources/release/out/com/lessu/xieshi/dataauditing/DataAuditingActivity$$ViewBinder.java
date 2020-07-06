// Generated code from Butter Knife. Do not modify!
package com.lessu.xieshi.dataauditing;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Finder;
import butterknife.internal.ViewBinder;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class DataAuditingActivity$$ViewBinder<T extends DataAuditingActivity> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131230859, "method 'auditingAllButtonDidClick'");
    unbinder.view2131230859 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.auditingAllButtonDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131230860, "method 'auditingChoosenButtonDidClick'");
    unbinder.view2131230860 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.auditingChoosenButtonDidClick();
      }
    });
    return unbinder;
  }

  protected InnerUnbinder<T> createUnbinder(T target) {
    return new InnerUnbinder(target);
  }

  protected static class InnerUnbinder<T extends DataAuditingActivity> implements Unbinder {
    private T target;

    View view2131230859;

    View view2131230860;

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
      view2131230859.setOnClickListener(null);
      view2131230860.setOnClickListener(null);
    }
  }
}
