// Generated code from Butter Knife. Do not modify!
package com.lessu.xieshi.map;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class ProjectListActivity$$ViewInjector {
  public static void inject(Finder finder, final com.lessu.xieshi.map.ProjectListActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131231111, "method 'mapButtonDidClick'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.mapButtonDidClick();
        }
      });
    view = finder.findRequiredView(source, 2131231153, "method 'projectButtonDidClick'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.projectButtonDidClick();
        }
      });
  }

  public static void reset(com.lessu.xieshi.map.ProjectListActivity target) {
  }
}
