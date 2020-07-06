// Generated code from Butter Knife. Do not modify!
package com.lessu.xieshi.todaystatistics;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Finder;
import butterknife.internal.ViewBinder;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class AdminTodayinfoActivity$$ViewBinder<T extends AdminTodayinfoActivity> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131231265, "field 'tvProjectName'");
    target.tvProjectName = finder.castView(view, 2131231265, "field 'tvProjectName'");
    view = finder.findRequiredView(source, 2131231266, "field 'tvProjectNature'");
    target.tvProjectNature = finder.castView(view, 2131231266, "field 'tvProjectNature'");
    view = finder.findRequiredView(source, 2131231261, "field 'tvProjectAddress'");
    target.tvProjectAddress = finder.castView(view, 2131231261, "field 'tvProjectAddress'");
    view = finder.findRequiredView(source, 2131231263, "field 'tvProjectConstruct'");
    target.tvProjectConstruct = finder.castView(view, 2131231263, "field 'tvProjectConstruct'");
    view = finder.findRequiredView(source, 2131231262, "field 'tvProjectBuild'");
    target.tvProjectBuild = finder.castView(view, 2131231262, "field 'tvProjectBuild'");
    view = finder.findRequiredView(source, 2131231267, "field 'tvProjectSupervior'");
    target.tvProjectSupervior = finder.castView(view, 2131231267, "field 'tvProjectSupervior'");
    view = finder.findRequiredView(source, 2131231264, "field 'tvProjectDetection'");
    target.tvProjectDetection = finder.castView(view, 2131231264, "field 'tvProjectDetection'");
    return unbinder;
  }

  protected InnerUnbinder<T> createUnbinder(T target) {
    return new InnerUnbinder(target);
  }

  protected static class InnerUnbinder<T extends AdminTodayinfoActivity> implements Unbinder {
    private T target;

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
      target.tvProjectName = null;
      target.tvProjectNature = null;
      target.tvProjectAddress = null;
      target.tvProjectConstruct = null;
      target.tvProjectBuild = null;
      target.tvProjectSupervior = null;
      target.tvProjectDetection = null;
    }
  }
}
