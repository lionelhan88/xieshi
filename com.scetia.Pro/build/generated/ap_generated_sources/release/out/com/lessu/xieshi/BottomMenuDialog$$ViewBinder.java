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

public class BottomMenuDialog$$ViewBinder<T extends BottomMenuDialog> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131231682, "field 'userJoin' and method 'onViewClicked'");
    target.userJoin = finder.castView(view, 2131231682, "field 'userJoin'");
    unbinder.view2131231682 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = finder.findRequiredView(source, 2131231252, "field 'otherJoin' and method 'onViewClicked'");
    target.otherJoin = finder.castView(view, 2131231252, "field 'otherJoin'");
    unbinder.view2131231252 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = finder.findRequiredView(source, 2131230976, "field 'dialogMeetingConfirmCancel' and method 'onViewClicked'");
    target.dialogMeetingConfirmCancel = finder.castView(view, 2131230976, "field 'dialogMeetingConfirmCancel'");
    unbinder.view2131230976 = view;
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

  protected static class InnerUnbinder<T extends BottomMenuDialog> implements Unbinder {
    private T target;

    View view2131231682;

    View view2131231252;

    View view2131230976;

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
      view2131231682.setOnClickListener(null);
      target.userJoin = null;
      view2131231252.setOnClickListener(null);
      target.otherJoin = null;
      view2131230976.setOnClickListener(null);
      target.dialogMeetingConfirmCancel = null;
    }
  }
}
