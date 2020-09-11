// Generated code from Butter Knife. Do not modify!
package com.lessu.xieshi.meet.fragment;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Finder;
import butterknife.internal.ViewBinder;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class MisMeetingDetailFragment$$ViewBinder<T extends MisMeetingDetailFragment> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131231198, "field 'meetingDetailName'");
    target.meetingDetailName = finder.castView(view, 2131231198, "field 'meetingDetailName'");
    view = finder.findRequiredView(source, 2131231196, "field 'meetingDetailCreateUser'");
    target.meetingDetailCreateUser = finder.castView(view, 2131231196, "field 'meetingDetailCreateUser'");
    view = finder.findRequiredView(source, 2131231199, "field 'meetingDetailPhone'");
    target.meetingDetailPhone = finder.castView(view, 2131231199, "field 'meetingDetailPhone'");
    view = finder.findRequiredView(source, 2131231201, "field 'meetingDetailStartDate'");
    target.meetingDetailStartDate = finder.castView(view, 2131231201, "field 'meetingDetailStartDate'");
    view = finder.findRequiredView(source, 2131231197, "field 'meetingDetailEndDate'");
    target.meetingDetailEndDate = finder.castView(view, 2131231197, "field 'meetingDetailEndDate'");
    view = finder.findRequiredView(source, 2131231194, "field 'meetingDetailAddress'");
    target.meetingDetailAddress = finder.castView(view, 2131231194, "field 'meetingDetailAddress'");
    view = finder.findRequiredView(source, 2131230890, "field 'btMeetingIsConfirm'");
    target.btMeetingIsConfirm = finder.castView(view, 2131230890, "field 'btMeetingIsConfirm'");
    view = finder.findRequiredView(source, 2131231195, "field 'meetingDetailContent'");
    target.meetingDetailContent = finder.castView(view, 2131231195, "field 'meetingDetailContent'");
    view = finder.findRequiredView(source, 2131231219, "field 'meetingUserIsSigned'");
    target.meetingUserIsSigned = finder.castView(view, 2131231219, "field 'meetingUserIsSigned'");
    view = finder.findRequiredView(source, 2131231121, "field 'llMeetingConfirm'");
    target.llMeetingConfirm = finder.castView(view, 2131231121, "field 'llMeetingConfirm'");
    view = finder.findRequiredView(source, 2131231122, "field 'llMeetingSigned'");
    target.llMeetingSigned = finder.castView(view, 2131231122, "field 'llMeetingSigned'");
    view = finder.findRequiredView(source, 2131231014, "field 'smartRefreshLayout'");
    target.smartRefreshLayout = finder.castView(view, 2131231014, "field 'smartRefreshLayout'");
    view = finder.findRequiredView(source, 2131231200, "field 'meetingDetailPhoto'");
    target.meetingDetailPhoto = finder.castView(view, 2131231200, "field 'meetingDetailPhoto'");
    return unbinder;
  }

  protected InnerUnbinder<T> createUnbinder(T target) {
    return new InnerUnbinder(target);
  }

  protected static class InnerUnbinder<T extends MisMeetingDetailFragment> implements Unbinder {
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
      target.meetingDetailName = null;
      target.meetingDetailCreateUser = null;
      target.meetingDetailPhone = null;
      target.meetingDetailStartDate = null;
      target.meetingDetailEndDate = null;
      target.meetingDetailAddress = null;
      target.btMeetingIsConfirm = null;
      target.meetingDetailContent = null;
      target.meetingUserIsSigned = null;
      target.llMeetingConfirm = null;
      target.llMeetingSigned = null;
      target.smartRefreshLayout = null;
      target.meetingDetailPhoto = null;
    }
  }
}
