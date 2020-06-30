// Generated code from Butter Knife. Do not modify!
package com.lessu.xieshi.uploadpicture;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class ImageGalleryActivity$$ViewInjector {
  public static void inject(Finder finder, final com.lessu.xieshi.uploadpicture.ImageGalleryActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131231043, "method 'leftButtonDidPress'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.leftButtonDidPress();
        }
      });
    view = finder.findRequiredView(source, 2131231181, "method 'rightButtonDidPress'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.rightButtonDidPress();
        }
      });
    view = finder.findRequiredView(source, 2131230943, "method 'downloadButtonDidPress'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.downloadButtonDidPress();
        }
      });
  }

  public static void reset(com.lessu.xieshi.uploadpicture.ImageGalleryActivity target) {
  }
}
