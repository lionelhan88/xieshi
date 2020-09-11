// Generated code from Butter Knife. Do not modify!
package com.lessu.xieshi.meet;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Finder;
import butterknife.internal.ViewBinder;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class ReplaceActivity$$ViewBinder<T extends ReplaceActivity> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131231323, "field 'replaceSignHyCode' and method 'onViewClicked'");
    target.replaceSignHyCode = finder.castView(view, 2131231323, "field 'replaceSignHyCode'");
    unbinder.view2131231323 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = finder.findRequiredView(source, 2131231331, "field 'replaceSignUserName'");
    target.replaceSignUserName = finder.castView(view, 2131231331, "field 'replaceSignUserName'");
    view = finder.findRequiredView(source, 2131231322, "field 'replaceSignHandSignView'");
    target.replaceSignHandSignView = finder.castView(view, 2131231322, "field 'replaceSignHandSignView'");
    view = finder.findRequiredView(source, 2131230904, "field 'btReplaceConfirm' and method 'onViewClicked'");
    target.btReplaceConfirm = finder.castView(view, 2131230904, "field 'btReplaceConfirm'");
    unbinder.view2131230904 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = finder.findRequiredView(source, 2131230906, "field 'btReplaceReset' and method 'onViewClicked'");
    target.btReplaceReset = finder.castView(view, 2131230906, "field 'btReplaceReset'");
    unbinder.view2131230906 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = finder.findRequiredView(source, 2131231320, "field 'replaceSignCompanyName'");
    target.replaceSignCompanyName = finder.castView(view, 2131231320, "field 'replaceSignCompanyName'");
    view = finder.findRequiredView(source, 2131231332, "field 'replaceSignUserPhone'");
    target.replaceSignUserPhone = finder.castView(view, 2131231332, "field 'replaceSignUserPhone'");
    view = finder.findRequiredView(source, 2131231321, "field 'replaceSignHandImage'");
    target.replaceSignHandImage = finder.castView(view, 2131231321, "field 'replaceSignHandImage'");
    view = finder.findRequiredView(source, 2131231330, "field 'replaceSignTime'");
    target.replaceSignTime = finder.castView(view, 2131231330, "field 'replaceSignTime'");
    view = finder.findRequiredView(source, 2131230905, "field 'btReplaceOut' and method 'onViewClicked'");
    target.btReplaceOut = finder.castView(view, 2131230905, "field 'btReplaceOut'");
    unbinder.view2131230905 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = finder.findRequiredView(source, 2131231127, "field 'llReplaceSignTime'");
    target.llReplaceSignTime = finder.castView(view, 2131231127, "field 'llReplaceSignTime'");
    return unbinder;
  }

  protected InnerUnbinder<T> createUnbinder(T target) {
    return new InnerUnbinder(target);
  }

  protected static class InnerUnbinder<T extends ReplaceActivity> implements Unbinder {
    private T target;

    View view2131231323;

    View view2131230904;

    View view2131230906;

    View view2131230905;

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
      view2131231323.setOnClickListener(null);
      target.replaceSignHyCode = null;
      target.replaceSignUserName = null;
      target.replaceSignHandSignView = null;
      view2131230904.setOnClickListener(null);
      target.btReplaceConfirm = null;
      view2131230906.setOnClickListener(null);
      target.btReplaceReset = null;
      target.replaceSignCompanyName = null;
      target.replaceSignUserPhone = null;
      target.replaceSignHandImage = null;
      target.replaceSignTime = null;
      view2131230905.setOnClickListener(null);
      target.btReplaceOut = null;
      target.llReplaceSignTime = null;
    }
  }
}
