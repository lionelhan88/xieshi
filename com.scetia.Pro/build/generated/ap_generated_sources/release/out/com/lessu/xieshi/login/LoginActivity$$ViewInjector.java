// Generated code from Butter Knife. Do not modify!
package com.lessu.xieshi.login;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class LoginActivity$$ViewInjector {
  public static void inject(Finder finder, final com.lessu.xieshi.login.LoginActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131231091, "method 'loginButtonDidPress'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.loginButtonDidPress();
        }
      });
    view = finder.findRequiredView(source, 2131231487, "method 'userNameEditTextFocus'");
    view.setOnFocusChangeListener(
      new android.view.View.OnFocusChangeListener() {
        @Override public void onFocusChange(
          android.view.View p0,
          boolean p1
        ) {
          target.userNameEditTextFocus(p0, p1);
        }
      });
    view = finder.findRequiredView(source, 2131231142, "method 'passWordEditTextTextFocus'");
    view.setOnFocusChangeListener(
      new android.view.View.OnFocusChangeListener() {
        @Override public void onFocusChange(
          android.view.View p0,
          boolean p1
        ) {
          target.passWordEditTextTextFocus(p0, p1);
        }
      });
  }

  public static void reset(com.lessu.xieshi.login.LoginActivity target) {
  }
}
