// Generated code from Butter Knife. Do not modify!
package com.lessu.xieshi.todaystatistics;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Finder;
import butterknife.internal.ViewBinder;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class TodayStatisticsDetailActivity$$ViewBinder<T extends TodayStatisticsDetailActivity> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131231304, "field 'projectTestingLowUnqualified'");
    target.projectTestingLowUnqualified = finder.castView(view, 2131231304, "field 'projectTestingLowUnqualified'");
    view = finder.findRequiredView(source, 2131231300, "field 'projectTestingHighUnqualified'");
    target.projectTestingHighUnqualified = finder.castView(view, 2131231300, "field 'projectTestingHighUnqualified'");
    view = finder.findRequiredView(source, 2131231302, "field 'projectTestingJizhuangUnqualified'");
    target.projectTestingJizhuangUnqualified = finder.castView(view, 2131231302, "field 'projectTestingJizhuangUnqualified'");
    view = finder.findRequiredView(source, 2131231301, "field 'projectTestingHuitanUnqualified'");
    target.projectTestingHuitanUnqualified = finder.castView(view, 2131231301, "field 'projectTestingHuitanUnqualified'");
    view = finder.findRequiredView(source, 2131231305, "field 'projectTestingZongheUnqualified'");
    target.projectTestingZongheUnqualified = finder.castView(view, 2131231305, "field 'projectTestingZongheUnqualified'");
    view = finder.findRequiredView(source, 2131231306, "field 'projectTestingZuanxinUnqualified'");
    target.projectTestingZuanxinUnqualified = finder.castView(view, 2131231306, "field 'projectTestingZuanxinUnqualified'");
    view = finder.findRequiredView(source, 2131231303, "field 'projectTestingLayout'");
    target.projectTestingLayout = finder.castView(view, 2131231303, "field 'projectTestingLayout'");
    return unbinder;
  }

  protected InnerUnbinder<T> createUnbinder(T target) {
    return new InnerUnbinder(target);
  }

  protected static class InnerUnbinder<T extends TodayStatisticsDetailActivity> implements Unbinder {
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
      target.projectTestingLowUnqualified = null;
      target.projectTestingHighUnqualified = null;
      target.projectTestingJizhuangUnqualified = null;
      target.projectTestingHuitanUnqualified = null;
      target.projectTestingZongheUnqualified = null;
      target.projectTestingZuanxinUnqualified = null;
      target.projectTestingLayout = null;
    }
  }
}
