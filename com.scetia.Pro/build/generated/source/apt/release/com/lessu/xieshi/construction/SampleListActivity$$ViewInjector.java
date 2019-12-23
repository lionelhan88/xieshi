// Generated code from Butter Knife. Do not modify!
package com.lessu.xieshi.construction;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SampleListActivity$$ViewInjector {
  public static void inject(Finder finder, final com.lessu.xieshi.construction.SampleListActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131559000, "method 'qrcodeScanButton'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.qrcodeScanButton();
        }
      });
  }

  public static void reset(com.lessu.xieshi.construction.SampleListActivity target) {
  }
}
