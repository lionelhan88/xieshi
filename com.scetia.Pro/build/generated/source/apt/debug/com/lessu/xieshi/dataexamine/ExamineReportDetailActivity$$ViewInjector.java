// Generated code from Butter Knife. Do not modify!
package com.lessu.xieshi.dataexamine;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class ExamineReportDetailActivity$$ViewInjector {
  public static void inject(Finder finder, final com.lessu.xieshi.dataexamine.ExamineReportDetailActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558822, "method 'checkSampleButtonDidClick'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.checkSampleButtonDidClick();
        }
      });
  }

  public static void reset(com.lessu.xieshi.dataexamine.ExamineReportDetailActivity target) {
  }
}
