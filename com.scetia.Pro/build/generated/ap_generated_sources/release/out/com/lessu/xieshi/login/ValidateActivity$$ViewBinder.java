// Generated code from Butter Knife. Do not modify!
package com.lessu.xieshi.login;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Finder;
import butterknife.internal.ViewBinder;
import java.lang.CharSequence;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class ValidateActivity$$ViewBinder<T extends ValidateActivity> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131230969, "method 'getValidateButtonDidPress'");
    unbinder.view2131230969 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.getValidateButtonDidPress();
      }
    });
    view = finder.findRequiredView(source, 2131231499, "method 'validateButtonDidPress'");
    unbinder.view2131231499 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.validateButtonDidPress();
      }
    });
    view = finder.findRequiredView(source, 2131231150, "method 'phoneNumEditTextChanged'");
    unbinder.view2131231150 = view;
    ((TextView) view).addTextChangedListener(new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence p0, int p1, int p2, int p3) {
        target.phoneNumEditTextChanged();
      }

      @Override
      public void beforeTextChanged(CharSequence p0, int p1, int p2, int p3) {
      }

      @Override
      public void afterTextChanged(Editable p0) {
      }
    });
    return unbinder;
  }

  protected InnerUnbinder<T> createUnbinder(T target) {
    return new InnerUnbinder(target);
  }

  protected static class InnerUnbinder<T extends ValidateActivity> implements Unbinder {
    private T target;

    View view2131230969;

    View view2131231499;

    View view2131231150;

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
      view2131230969.setOnClickListener(null);
      view2131231499.setOnClickListener(null);
      ((TextView) view2131231150).addTextChangedListener(null);
    }
  }
}
