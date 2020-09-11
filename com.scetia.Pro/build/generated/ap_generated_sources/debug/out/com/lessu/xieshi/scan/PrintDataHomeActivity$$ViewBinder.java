// Generated code from Butter Knife. Do not modify!
package com.lessu.xieshi.scan;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Finder;
import butterknife.internal.ViewBinder;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class PrintDataHomeActivity$$ViewBinder<T extends PrintDataHomeActivity> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131231280, "field 'printDataHomeFrameLayout'");
    target.printDataHomeFrameLayout = finder.castView(view, 2131231280, "field 'printDataHomeFrameLayout'");
    view = finder.findRequiredView(source, 2131231352, "field 'sbScan'");
    target.sbScan = finder.castView(view, 2131231352, "field 'sbScan'");
    view = finder.findRequiredView(source, 2131231279, "field 'printDataHomeDrawer'");
    target.printDataHomeDrawer = finder.castView(view, 2131231279, "field 'printDataHomeDrawer'");
    view = finder.findRequiredView(source, 2131231110, "field 'llBiaoshisaomiao'");
    target.llBiaoshisaomiao = finder.castView(view, 2131231110, "field 'llBiaoshisaomiao'");
    view = finder.findRequiredView(source, 2131231140, "field 'llShenqingshangbao'");
    target.llShenqingshangbao = finder.castView(view, 2131231140, "field 'llShenqingshangbao'");
    view = finder.findRequiredView(source, 2131231138, "field 'llShenhexiazai'");
    target.llShenhexiazai = finder.castView(view, 2131231138, "field 'llShenhexiazai'");
    view = finder.findRequiredView(source, 2131231130, "field 'llRukuchakan'");
    target.llRukuchakan = finder.castView(view, 2131231130, "field 'llRukuchakan'");
    view = finder.findRequiredView(source, 2131231137, "field 'llShebeixinxi'");
    target.llShebeixinxi = finder.castView(view, 2131231137, "field 'llShebeixinxi'");
    return unbinder;
  }

  protected InnerUnbinder<T> createUnbinder(T target) {
    return new InnerUnbinder(target);
  }

  protected static class InnerUnbinder<T extends PrintDataHomeActivity> implements Unbinder {
    private T target;

    protected InnerUnbinder(T target) {
      this.target = target;
    }

    @Override
    public final void unbind() {
      if (target == null) throw new IllegalStateException("Bindings already cleared.");
      unbind(target);
      target = null;
    }

    protected void unbind(T target) {
      target.printDataHomeFrameLayout = null;
      target.sbScan = null;
      target.printDataHomeDrawer = null;
      target.llBiaoshisaomiao = null;
      target.llShenqingshangbao = null;
      target.llShenhexiazai = null;
      target.llRukuchakan = null;
      target.llShebeixinxi = null;
    }
  }
}
