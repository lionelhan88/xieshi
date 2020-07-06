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
    view = finder.findRequiredView(source, 2131230780, "method 'Report_CreateDateEditTextPreDidClick'");
    unbinder.view2131230780 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.Report_CreateDateEditTextPreDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131230779, "method 'Report_CreateDateEditTextEndDidClick'");
    unbinder.view2131230779 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.Report_CreateDateEditTextEndDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131230764, "method 'ProjectAreaButtonDidClick'");
    unbinder.view2131230764 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.ProjectAreaButtonDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131230736, "method 'typeButtonDidClick'");
    unbinder.view2131230736 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.typeButtonDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131230758, "method 'ManageUnitIDButton'");
    unbinder.view2131230758 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.ManageUnitIDButton();
      }
    });
    view = finder.findRequiredView(source, 2131230809, "method 'UqExecStatusButton'");
    unbinder.view2131230809 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.UqExecStatusButton();
      }
    });
    view = finder.findRequiredView(source, 2131230873, "method 'bt_jianceleibieDidClick'");
    unbinder.view2131230873 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.bt_jianceleibieDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131230874, "method 'bt_jiancexiangmuDidClick'");
    unbinder.view2131230874 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.bt_jiancexiangmuDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131230729, "method 'CheckItemButtonDidClick'");
    unbinder.view2131230729 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.CheckItemButtonDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131231058, "method 'materialButtonDidClick'");
    unbinder.view2131231058 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.materialButtonDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131231063, "method 'projectButtonDidClick'");
    unbinder.view2131231063 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.projectButtonDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131230791, "method 'StartDateEditTextDidClick'");
    unbinder.view2131230791 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.StartDateEditTextDidClick();
      }
    });
    view = finder.findRequiredView(source, 2131230735, "method 'EndDateEditTextDidClick'");
    unbinder.view2131230735 = view;
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

    View view2131230780;

    View view2131230779;

    View view2131230764;

    View view2131230736;

    View view2131230758;

    View view2131230809;

    View view2131230873;

    View view2131230874;

    View view2131230729;

    View view2131231058;

    View view2131231063;

    View view2131230791;

    View view2131230735;

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
      view2131230780.setOnClickListener(null);
      view2131230779.setOnClickListener(null);
      view2131230764.setOnClickListener(null);
      view2131230736.setOnClickListener(null);
      view2131230758.setOnClickListener(null);
      view2131230809.setOnClickListener(null);
      view2131230873.setOnClickListener(null);
      view2131230874.setOnClickListener(null);
      view2131230729.setOnClickListener(null);
      view2131231058.setOnClickListener(null);
      view2131231063.setOnClickListener(null);
      view2131230791.setOnClickListener(null);
      view2131230735.setOnClickListener(null);
    }
  }
}
