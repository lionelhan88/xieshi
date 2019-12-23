// Generated code from Butter Knife. Do not modify!
package com.lessu.xieshi;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SettingActivity$$ViewInjector {
  public static void inject(Finder finder, final com.lessu.xieshi.SettingActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131559020, "method 'jiechuButtonDidClick'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.jiechuButtonDidClick();
        }
      });
    view = finder.findRequiredView(source, 2131559016, "method 'passwordButtonDidClick'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.passwordButtonDidClick();
        }
      });
    view = finder.findRequiredView(source, 2131559017, "method 'serviceButtonDidClick'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.serviceButtonDidClick();
        }
      });
    view = finder.findRequiredView(source, 2131559019, "method 'updateButtonDidClick'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.updateButtonDidClick();
        }
      });
  }

  public static void reset(com.lessu.xieshi.SettingActivity target) {
  }
}
