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

public class MeetingListActivity$$ViewBinder<T extends MeetingListActivity> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131231215, "field 'meetingRecyclerView'");
    target.meetingRecyclerView = finder.castView(view, 2131231215, "field 'meetingRecyclerView'");
    view = finder.findRequiredView(source, 2131231203, "field 'meetingListDateSelectStart' and method 'onViewClicked'");
    target.meetingListDateSelectStart = finder.castView(view, 2131231203, "field 'meetingListDateSelectStart'");
    unbinder.view2131231203 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = finder.findRequiredView(source, 2131231202, "field 'meetingListDateSelectEnd' and method 'onViewClicked'");
    target.meetingListDateSelectEnd = finder.castView(view, 2131231202, "field 'meetingListDateSelectEnd'");
    unbinder.view2131231202 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = finder.findRequiredView(source, 2131231213, "field 'meetingListSearch' and method 'onViewClicked'");
    target.meetingListSearch = finder.castView(view, 2131231213, "field 'meetingListSearch'");
    unbinder.view2131231213 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = finder.findRequiredView(source, 2131231212, "field 'meetingListRefresh'");
    target.meetingListRefresh = finder.castView(view, 2131231212, "field 'meetingListRefresh'");
    return unbinder;
  }

  protected InnerUnbinder<T> createUnbinder(T target) {
    return new InnerUnbinder(target);
  }

  protected static class InnerUnbinder<T extends MeetingListActivity> implements Unbinder {
    private T target;

    View view2131231203;

    View view2131231202;

    View view2131231213;

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
      target.meetingRecyclerView = null;
      view2131231203.setOnClickListener(null);
      target.meetingListDateSelectStart = null;
      view2131231202.setOnClickListener(null);
      target.meetingListDateSelectEnd = null;
      view2131231213.setOnClickListener(null);
      target.meetingListSearch = null;
      target.meetingListRefresh = null;
    }
  }
}
