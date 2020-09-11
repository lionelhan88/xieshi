// Generated code from Butter Knife. Do not modify!
package com.lessu.xieshi.meet.fragment;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Finder;
import butterknife.internal.ViewBinder;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class CompanySignFragment$$ViewBinder<T extends CompanySignFragment> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131231233, "field 'misFragmentCompanySignRv'");
    target.misFragmentCompanySignRv = finder.castView(view, 2131231233, "field 'misFragmentCompanySignRv'");
    view = finder.findRequiredView(source, 2131231234, "field 'misMeetingFragmentRefresh'");
    target.misMeetingFragmentRefresh = finder.castView(view, 2131231234, "field 'misMeetingFragmentRefresh'");
    return unbinder;
  }

  protected InnerUnbinder<T> createUnbinder(T target) {
    return new InnerUnbinder(target);
  }

  protected static class InnerUnbinder<T extends CompanySignFragment> implements Unbinder {
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
      target.misFragmentCompanySignRv = null;
      target.misMeetingFragmentRefresh = null;
    }
  }
}
