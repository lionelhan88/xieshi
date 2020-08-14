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

public class MeetingDetailActivity$$ViewBinder<T extends MeetingDetailActivity> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131231192, "field 'meetingDetailName'");
    target.meetingDetailName = finder.castView(view, 2131231192, "field 'meetingDetailName'");
    view = finder.findRequiredView(source, 2131231190, "field 'meetingDetailCreateUser'");
    target.meetingDetailCreateUser = finder.castView(view, 2131231190, "field 'meetingDetailCreateUser'");
    view = finder.findRequiredView(source, 2131231193, "field 'meetingDetailPhone'");
    target.meetingDetailPhone = finder.castView(view, 2131231193, "field 'meetingDetailPhone'");
    view = finder.findRequiredView(source, 2131231194, "field 'meetingDetailStartDate'");
    target.meetingDetailStartDate = finder.castView(view, 2131231194, "field 'meetingDetailStartDate'");
    view = finder.findRequiredView(source, 2131231191, "field 'meetingDetailEndDate'");
    target.meetingDetailEndDate = finder.castView(view, 2131231191, "field 'meetingDetailEndDate'");
    view = finder.findRequiredView(source, 2131231188, "field 'meetingDetailAddress'");
    target.meetingDetailAddress = finder.castView(view, 2131231188, "field 'meetingDetailAddress'");
    view = finder.findRequiredView(source, 2131230889, "field 'btMeetingIsConfirm' and method 'onViewClicked'");
    target.btMeetingIsConfirm = finder.castView(view, 2131230889, "field 'btMeetingIsConfirm'");
    unbinder.view2131230889 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = finder.findRequiredView(source, 2131231189, "field 'meetingDetailContent'");
    target.meetingDetailContent = finder.castView(view, 2131231189, "field 'meetingDetailContent'");
    view = finder.findRequiredView(source, 2131231211, "field 'meetingUserIsSigned'");
    target.meetingUserIsSigned = finder.castView(view, 2131231211, "field 'meetingUserIsSigned'");
    view = finder.findRequiredView(source, 2131231116, "field 'llMeetingConfirm'");
    target.llMeetingConfirm = finder.castView(view, 2131231116, "field 'llMeetingConfirm'");
    view = finder.findRequiredView(source, 2131231117, "field 'llMeetingSigned'");
    target.llMeetingSigned = finder.castView(view, 2131231117, "field 'llMeetingSigned'");
    return unbinder;
  }

  protected InnerUnbinder<T> createUnbinder(T target) {
    return new InnerUnbinder(target);
  }

  protected static class InnerUnbinder<T extends MeetingDetailActivity> implements Unbinder {
    private T target;

    View view2131230889;

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
      target.meetingDetailName = null;
      target.meetingDetailCreateUser = null;
      target.meetingDetailPhone = null;
      target.meetingDetailStartDate = null;
      target.meetingDetailEndDate = null;
      target.meetingDetailAddress = null;
      view2131230889.setOnClickListener(null);
      target.btMeetingIsConfirm = null;
      target.meetingDetailContent = null;
      target.meetingUserIsSigned = null;
      target.llMeetingConfirm = null;
      target.llMeetingSigned = null;
    }
  }
}
