// Generated code from Butter Knife. Do not modify!
package com.lessu.xieshi.login;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class ValidateActivity$$ViewInjector {
  public static void inject(Finder finder, final com.lessu.xieshi.login.ValidateActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131559108, "method 'getValidateButtonDidPress'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.getValidateButtonDidPress();
        }
      });
    view = finder.findRequiredView(source, 2131559109, "method 'validateButtonDidPress'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.validateButtonDidPress();
        }
      });
    view = finder.findRequiredView(source, 2131559106, "method 'phoneNumEditTextChanged'");
    ((android.widget.TextView) view).addTextChangedListener(
      new android.text.TextWatcher() {
        @Override public void onTextChanged(
          java.lang.CharSequence p0,
          int p1,
          int p2,
          int p3
        ) {
          target.phoneNumEditTextChanged();
        }
        @Override public void beforeTextChanged(
          java.lang.CharSequence p0,
          int p1,
          int p2,
          int p3
        ) {
          
        }
        @Override public void afterTextChanged(
          android.text.Editable p0
        ) {
          
        }
      });
  }

  public static void reset(com.lessu.xieshi.login.ValidateActivity target) {
  }
}
