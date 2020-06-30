// Generated code from Butter Knife. Do not modify!
package com.lessu.xieshi;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SettingActivity$$ViewInjector {
  public static void inject(Finder finder, final com.lessu.xieshi.SettingActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131231025, "method 'jiechuButtonDidClick'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.jiechuButtonDidClick();
        }
      });
    view = finder.findRequiredView(source, 2131231143, "method 'passwordButtonDidClick'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.passwordButtonDidClick();
        }
      });
    view = finder.findRequiredView(source, 2131231217, "method 'serviceButtonDidClick'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.serviceButtonDidClick();
        }
      });
    view = finder.findRequiredView(source, 2131230887, "method 'resetLogin'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.resetLogin();
        }
      });
    view = finder.findRequiredView(source, 2131231483, "method 'updateButtonDidClick'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.updateButtonDidClick();
        }
      });
    view = finder.findRequiredView(source, 2131231196, "method 'scanLogin'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.scanLogin();
        }
      });
  }

  public static void reset(com.lessu.xieshi.SettingActivity target) {
  }
}
