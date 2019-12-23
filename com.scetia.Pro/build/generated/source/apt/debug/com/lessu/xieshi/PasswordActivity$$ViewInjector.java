// Generated code from Butter Knife. Do not modify!
package com.lessu.xieshi;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class PasswordActivity$$ViewInjector {
  public static void inject(Finder finder, final com.lessu.xieshi.PasswordActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558947, "method 'commitButtonDidClick'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.commitButtonDidClick();
        }
      });
  }

  public static void reset(com.lessu.xieshi.PasswordActivity target) {
  }
}
