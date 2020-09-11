// Generated code from Butter Knife. Do not modify!
package com.lessu.xieshi.unqualified;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Finder;
import butterknife.internal.ViewBinder;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class UnqualifiedSearchActivity$$ViewBinder<T extends UnqualifiedSearchActivity> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131230787, "method 'Report_CreateDateEditTextPreDidClick'");
    unbinder.view2131230787 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.Report_CreateDateEditTextPreDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131230786, "method 'Report_CreateDateEditTextEndDidClick'");
    unbinder.view2131230786 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.Report_CreateDateEditTextEndDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131230771, "method 'ProjectAreaButtonDidClick'");
    unbinder.view2131230771 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.ProjectAreaButtonDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131230740, "method 'typeButtonDidClick'");
    unbinder.view2131230740 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.typeButtonDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131230764, "method 'ManageUnitIDButton'");
    unbinder.view2131230764 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.ManageUnitIDButton();
      }
    });
    view = finder.findRequiredView(source, 2131230818, "method 'UqExecStatusButton'");
    unbinder.view2131230818 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.UqExecStatusButton();
      }
    });
    view = finder.findRequiredView(source, 2131230888, "method 'bt_jianceleibieDidClick'");
    unbinder.view2131230888 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.bt_jianceleibieDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131230889, "method 'bt_jiancexiangmuDidClick'");
    unbinder.view2131230889 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.bt_jiancexiangmuDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131230733, "method 'CheckItemButtonDidClick'");
    unbinder.view2131230733 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.CheckItemButtonDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131231112, "method 'materialButtonDidClick'");
    unbinder.view2131231112 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.materialButtonDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131231117, "method 'projectButtonDidClick'");
    unbinder.view2131231117 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.projectButtonDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131230799, "method 'StartDateEditTextDidClick'");
    unbinder.view2131230799 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.StartDateEditTextDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131230739, "method 'EndDateEditTextDidClick'");
    unbinder.view2131230739 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.EndDateEditTextDidClick();
      }
    });
    return unbinder;
  }

  protected InnerUnbinder<T> createUnbinder(T target) {
    return new InnerUnbinder(target);
  }

  protected static class InnerUnbinder<T extends UnqualifiedSearchActivity> implements Unbinder {
    private T target;

    View view2131230787;

    View view2131230786;

    View view2131230771;

    View view2131230740;

    View view2131230764;

    View view2131230818;

    View view2131230888;

    View view2131230889;

    View view2131230733;

    View view2131231112;

    View view2131231117;

    View view2131230799;

    View view2131230739;

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
      view2131230787.setOnClickListener(null);
      view2131230786.setOnClickListener(null);
      view2131230771.setOnClickListener(null);
      view2131230740.setOnClickListener(null);
      view2131230764.setOnClickListener(null);
      view2131230818.setOnClickListener(null);
      view2131230888.setOnClickListener(null);
      view2131230889.setOnClickListener(null);
      view2131230733.setOnClickListener(null);
      view2131231112.setOnClickListener(null);
      view2131231117.setOnClickListener(null);
      view2131230799.setOnClickListener(null);
      view2131230739.setOnClickListener(null);
    }
  }
}
