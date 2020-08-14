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

public class CustomDialog$$ViewBinder<T extends CustomDialog> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131230885, "field 'btConfirmSign' and method 'onViewClicked'");
    target.btConfirmSign = finder.castView(view, 2131230885, "field 'btConfirmSign'");
    unbinder.view2131230885 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = finder.findRequiredView(source, 2131230884, "field 'btCancelSign' and method 'onViewClicked'");
    target.btCancelSign = finder.castView(view, 2131230884, "field 'btCancelSign'");
    unbinder.view2131230884 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = finder.findRequiredView(source, 2131230904, "field 'btResetSign' and method 'onViewClicked'");
    target.btResetSign = finder.castView(view, 2131230904, "field 'btResetSign'");
    unbinder.view2131230904 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = finder.findRequiredView(source, 2131231364, "field 'signView'");
    target.signView = finder.castView(view, 2131231364, "field 'signView'");
    view = finder.findRequiredView(source, 2131231208, "field 'meetingSuccessDialogHyCode'");
    target.meetingSuccessDialogHyCode = finder.castView(view, 2131231208, "field 'meetingSuccessDialogHyCode'");
    view = finder.findRequiredView(source, 2131231209, "field 'meetingSuccessDialogHyDw'");
    target.meetingSuccessDialogHyDw = finder.castView(view, 2131231209, "field 'meetingSuccessDialogHyDw'");
    view = finder.findRequiredView(source, 2131231210, "field 'meetingSuccessDialogUser'");
    target.meetingSuccessDialogUser = finder.castView(view, 2131231210, "field 'meetingSuccessDialogUser'");
    return unbinder;
  }

  protected InnerUnbinder<T> createUnbinder(T target) {
    return new InnerUnbinder(target);
  }

  protected static class InnerUnbinder<T extends CustomDialog> implements Unbinder {
    private T target;

    View view2131230885;

    View view2131230884;

    View view2131230904;

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
      view2131230885.setOnClickListener(null);
      target.btConfirmSign = null;
      view2131230884.setOnClickListener(null);
      target.btCancelSign = null;
      view2131230904.setOnClickListener(null);
      target.btResetSign = null;
      target.signView = null;
      target.meetingSuccessDialogHyCode = null;
      target.meetingSuccessDialogHyDw = null;
      target.meetingSuccessDialogUser = null;
    }
  }
}
