package com.ashera.appbarlayout;

import com.ashera.widget.IBehavior;
import com.ashera.widget.WidgetFactory;
import com.google.android.material.appbar.AppBarLayout;

public class AppBarLayoutPlugin  {
    public static void initPlugin() {
    	//start - widgets
        WidgetFactory.register(new com.ashera.appbarlayout.AppBarLayoutImpl());
        //end - widgets
        WidgetFactory.registerBehavior("@string/appbar_scrolling_view_behavior",
                new IBehavior() {
                    @Override
                    public Object newInstance() {
                        return new AppBarLayout.ScrollingViewBehavior();
                    }
                });
    }
}
