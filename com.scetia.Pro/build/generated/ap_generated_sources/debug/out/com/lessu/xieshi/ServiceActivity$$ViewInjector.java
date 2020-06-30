// Generated code from Butter Knife. Do not modify!
package com.lessu.xieshi;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class ServiceActivity$$ViewInjector {
  public static void inject(Finder finder, final com.lessu.xieshi.ServiceActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131231242, "method 'telecomButtonDidClick'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.telecomButtonDidClick();
        }
      });
    view = finder.findRequiredView(source, 2131231479, "method 'unicomButtonDidClick'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.unicomButtonDidClick();
        }
      });
  }

  public static void reset(com.lessu.xieshi.ServiceActivity target) {
  }
}
