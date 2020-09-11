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

public class OtherJoinMeetingActivity$$ViewBinder<T extends OtherJoinMeetingActivity> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131231255, "field 'otherMeetingJoinName'");
    target.otherMeetingJoinName = finder.castView(view, 2131231255, "field 'otherMeetingJoinName'");
    view = finder.findRequiredView(source, 2131231258, "field 'otherMeetingJoinUserHandSign'");
    target.otherMeetingJoinUserHandSign = finder.castView(view, 2131231258, "field 'otherMeetingJoinUserHandSign'");
    view = finder.findRequiredView(source, 2131230898, "field 'btOtherMeetingJoinOut' and method 'onViewClicked'");
    target.btOtherMeetingJoinOut = finder.castView(view, 2131230898, "field 'btOtherMeetingJoinOut'");
    unbinder.view2131230898 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = finder.findRequiredView(source, 2131230883, "field 'btBtOtherMeetingJoinConfirm' and method 'onViewClicked'");
    target.btBtOtherMeetingJoinConfirm = finder.castView(view, 2131230883, "field 'btBtOtherMeetingJoinConfirm'");
    unbinder.view2131230883 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = finder.findRequiredView(source, 2131230884, "field 'btBtOtherMeetingJoinReset' and method 'onViewClicked'");
    target.btBtOtherMeetingJoinReset = finder.castView(view, 2131230884, "field 'btBtOtherMeetingJoinReset'");
    unbinder.view2131230884 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = finder.findRequiredView(source, 2131231256, "field 'otherMeetingJoinNameError'");
    target.otherMeetingJoinNameError = finder.castView(view, 2131231256, "field 'otherMeetingJoinNameError'");
    view = finder.findRequiredView(source, 2131231254, "field 'otherJoinPhoneLabel'");
    target.otherJoinPhoneLabel = finder.castView(view, 2131231254, "field 'otherJoinPhoneLabel'");
    view = finder.findRequiredView(source, 2131231253, "field 'otherJoinPhone'");
    target.otherJoinPhone = finder.castView(view, 2131231253, "field 'otherJoinPhone'");
    return unbinder;
  }

  protected InnerUnbinder<T> createUnbinder(T target) {
    return new InnerUnbinder(target);
  }

  protected static class InnerUnbinder<T extends OtherJoinMeetingActivity> implements Unbinder {
    private T target;

    View view2131230898;

    View view2131230883;

    View view2131230884;

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
      target.otherMeetingJoinName = null;
      target.otherMeetingJoinUserHandSign = null;
      view2131230898.setOnClickListener(null);
      target.btOtherMeetingJoinOut = null;
      view2131230883.setOnClickListener(null);
      target.btBtOtherMeetingJoinConfirm = null;
      view2131230884.setOnClickListener(null);
      target.btBtOtherMeetingJoinReset = null;
      target.otherMeetingJoinNameError = null;
      target.otherJoinPhoneLabel = null;
      target.otherJoinPhone = null;
    }
  }
}
