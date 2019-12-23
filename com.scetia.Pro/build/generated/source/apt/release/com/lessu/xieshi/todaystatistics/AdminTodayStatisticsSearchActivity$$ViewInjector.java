// Generated code from Butter Knife. Do not modify!
package com.lessu.xieshi.todaystatistics;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class AdminTodayStatisticsSearchActivity$$ViewInjector {
  public static void inject(Finder finder, final com.lessu.xieshi.todaystatistics.AdminTodayStatisticsSearchActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558754, "method 'ProjectAreaButtonDidClick'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.ProjectAreaButtonDidClick();
        }
      });
  }

  public static void reset(com.lessu.xieshi.todaystatistics.AdminTodayStatisticsSearchActivity target) {
  }
}
