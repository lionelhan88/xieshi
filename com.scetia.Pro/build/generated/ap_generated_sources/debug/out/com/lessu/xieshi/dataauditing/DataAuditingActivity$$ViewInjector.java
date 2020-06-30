// Generated code from Butter Knife. Do not modify!
package com.lessu.xieshi.dataauditing;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class DataAuditingActivity$$ViewInjector {
  public static void inject(Finder finder, final com.lessu.xieshi.dataauditing.DataAuditingActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131230859, "method 'auditingAllButtonDidClick'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.auditingAllButtonDidClick();
        }
      });
    view = finder.findRequiredView(source, 2131230860, "method 'auditingChoosenButtonDidClick'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.auditingChoosenButtonDidClick();
        }
      });
  }

  public static void reset(com.lessu.xieshi.dataauditing.DataAuditingActivity target) {
  }
}
