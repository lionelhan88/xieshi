// Generated code from Butter Knife. Do not modify!
package com.lessu.xieshi.dataauditing;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class DataSearchActivity$$ViewInjector {
  public static void inject(Finder finder, final com.lessu.xieshi.dataauditing.DataSearchActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558795, "method 'typeButtonDidClick'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.typeButtonDidClick();
        }
      });
    view = finder.findRequiredView(source, 2131558797, "method 'projectButtonDidClick'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.projectButtonDidClick();
        }
      });
    view = finder.findRequiredView(source, 2131558799, "method 'auditedButtonDidClick'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.auditedButtonDidClick();
        }
      });
    view = finder.findRequiredView(source, 2131558800, "method 'searchButtonDidClick'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.searchButtonDidClick();
        }
      });
  }

  public static void reset(com.lessu.xieshi.dataauditing.DataSearchActivity target) {
  }
}
