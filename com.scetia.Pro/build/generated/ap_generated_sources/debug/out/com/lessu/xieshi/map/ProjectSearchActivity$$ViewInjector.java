// Generated code from Butter Knife. Do not modify!
package com.lessu.xieshi.map;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class ProjectSearchActivity$$ViewInjector {
  public static void inject(Finder finder, final com.lessu.xieshi.map.ProjectSearchActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131231153, "method 'projectButtonDidClick'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.projectButtonDidClick();
        }
      });
    view = finder.findRequiredView(source, 2131231251, "method 'timeButtonDidClick'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.timeButtonDidClick();
        }
      });
    view = finder.findRequiredView(source, 2131231200, "method 'searchButtonDidClick'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.searchButtonDidClick();
        }
      });
  }

  public static void reset(com.lessu.xieshi.map.ProjectSearchActivity target) {
  }
}
