// Generated code from Butter Knife. Do not modify!
package com.lessu.xieshi.mis.activitys;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Finder;
import butterknife.internal.ViewBinder;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class MisMeetingActivity$$ViewBinder<T extends MisMeetingActivity> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131231227, "field 'misMeetingName'");
    target.misMeetingName = finder.castView(view, 2131231227, "field 'misMeetingName'");
    view = finder.findRequiredView(source, 2131230957, "field 'danweiSignNumberText'");
    target.danweiSignNumberText = finder.castView(view, 2131230957, "field 'danweiSignNumberText'");
    view = finder.findRequiredView(source, 2131231255, "field 'personSignNumberText'");
    target.personSignNumberText = finder.castView(view, 2131231255, "field 'personSignNumberText'");
    view = finder.findRequiredView(source, 2131230955, "field 'daiqianSignNumberText'");
    target.daiqianSignNumberText = finder.castView(view, 2131230955, "field 'daiqianSignNumberText'");
    view = finder.findRequiredView(source, 2131231419, "field 'totalSignNumberText'");
    target.totalSignNumberText = finder.castView(view, 2131231419, "field 'totalSignNumberText'");
    view = finder.findRequiredView(source, 2131231228, "field 'misMeetingTabLayout'");
    target.misMeetingTabLayout = finder.castView(view, 2131231228, "field 'misMeetingTabLayout'");
    view = finder.findRequiredView(source, 2131231229, "field 'misMeetingViewPager'");
    target.misMeetingViewPager = finder.castView(view, 2131231229, "field 'misMeetingViewPager'");
    return unbinder;
  }

  protected InnerUnbinder<T> createUnbinder(T target) {
    return new InnerUnbinder(target);
  }

  protected static class InnerUnbinder<T extends MisMeetingActivity> implements Unbinder {
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
      target.misMeetingName = null;
      target.danweiSignNumberText = null;
      target.personSignNumberText = null;
      target.daiqianSignNumberText = null;
      target.totalSignNumberText = null;
      target.misMeetingTabLayout = null;
      target.misMeetingViewPager = null;
    }
  }
}
