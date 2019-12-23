package com.lessu.navigation;

import android.content.Context;
import android.widget.LinearLayout;

/**
 * Created by lessu on 14-7-31.
 */
public abstract class BarItem extends LinearLayout {
//    protected boolean enabled;      // default is YES
    protected String    title;        // default is nil

    public BarItem(Context context) {
        super(context);
    }
//    @property(nonatomic,retain)           UIImage     *image;        // default is nil
//    @property(nonatomic,retain)           UIImage     *landscapeImagePhone NS_AVAILABLE_IOS(5_0); // default is nil
//    @property(nonatomic)                  UIEdgeInsets imageInsets;  // default is UIEdgeInsetsZero
//    @property(nonatomic)                  UIEdgeInsets landscapeImagePhoneInsets NS_AVAILABLE_IOS(5_0);  // default is UIEdgeInsetsZero. These insets apply only when the landscapeImagePhone property is set.
//    @property(nonatomic)                  NSInteger    tag;          // default is 0

/* You may specify the font, text color, and shadow properties for the title in the text attributes dictionary, using the keys found in NSAttributedString.h.
 */
//    - (void)setTitleTextAttributes:(NSDictionary *)attributes forState:(UIControlState)state NS_AVAILABLE_IOS(5_0) UI_APPEARANCE_SELECTOR;
//    - (NSDictionary *)titleTextAttributesForState:(UIControlState)state NS_AVAILABLE_IOS(5_0) UI_APPEARANCE_SELECTOR;

}
