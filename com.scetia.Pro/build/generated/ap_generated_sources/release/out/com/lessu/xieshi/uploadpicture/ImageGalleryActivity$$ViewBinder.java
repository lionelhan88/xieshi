// Generated code from Butter Knife. Do not modify!
package com.lessu.xieshi.uploadpicture;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Finder;
import butterknife.internal.ViewBinder;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class ImageGalleryActivity$$ViewBinder<T extends ImageGalleryActivity> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131231047, "method 'leftButtonDidPress'");
    unbinder.view2131231047 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.leftButtonDidPress();
      }
    });
    view = finder.findRequiredView(source, 2131231185, "method 'rightButtonDidPress'");
    unbinder.view2131231185 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.rightButtonDidPress();
      }
    });
    view = finder.findRequiredView(source, 2131230943, "method 'downloadButtonDidPress'");
    unbinder.view2131230943 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.downloadButtonDidPress();
      }
    });
    return unbinder;
  }

  protected InnerUnbinder<T> createUnbinder(T target) {
    return new InnerUnbinder(target);
  }

  protected static class InnerUnbinder<T extends ImageGalleryActivity> implements Unbinder {
    private T target;

    View view2131231047;

    View view2131231185;

    View view2131230943;

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
      view2131231047.setOnClickListener(null);
      view2131231185.setOnClickListener(null);
      view2131230943.setOnClickListener(null);
    }
  }
}
