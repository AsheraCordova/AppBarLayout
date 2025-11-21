//start - license
/*
 * Copyright (c) 2025 Ashera Cordova
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */
//end - license
package com.ashera.appbarlayout;
// start - imports
import java.util.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.*;
import android.widget.*;
import android.view.View.*;

import com.ashera.widget.BaseHasWidgets;

import android.annotation.SuppressLint;

import com.ashera.core.IFragment;
import com.ashera.widget.bus.*;
import com.ashera.converter.*;
import com.ashera.widget.bus.Event.*;
import com.ashera.widget.*;
import com.ashera.widget.IWidgetLifeCycleListener.*;
import com.ashera.layout.*;

import android.graphics.Canvas;
import android.widget.*;
import androidx.core.view.*;
import android.view.*;

import static com.ashera.widget.IWidget.*;
//end - imports
import android.graphics.drawable.Drawable;
public class AppBarLayoutImpl extends BaseHasWidgets {
	//start - body
	public final static String LOCAL_NAME = "com.google.android.material.appbar.AppBarLayout"; 
	public final static String GROUP_NAME = "com.google.android.material.appbar.AppBarLayout";
	private com.google.android.material.appbar.AppBarLayout appBarLayout;
	

	
		@SuppressLint("NewApi")
		final static class Layout_scrollFlags  extends AbstractBitFlagConverter{
		private Map<String, Integer> mapping = new HashMap<>();
				{
				mapping.put("noScroll", 0x0);
				mapping.put("scroll", 0x1);
				mapping.put("exitUntilCollapsed", 0x2);
				mapping.put("enterAlways", 0x4);
				mapping.put("enterAlwaysCollapsed", 0x8);
				mapping.put("snap", 0x10);
				mapping.put("snapMargins", 0x20);
				}
		@Override
		public Map<String, Integer> getMapping() {
				return mapping;
				}

		@Override
		public Integer getDefault() {
				return 0;
				}
				}
	@Override
	public void loadAttributes(String localName) {
		ViewGroupImpl.register(localName);

		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("statusBarForeground").withType("drawable"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("expanded").withType("boolean"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("liftOnScroll").withType("boolean"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("liftOnScrollTargetViewId").withType("id"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("elevation").withType("dimensionfloat"));
	
				ConverterFactory.register("com.google.android.material.appbar.AppBarLayout.layout_scrollFlags", new Layout_scrollFlags());
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("layout_scrollFlags").withType("com.google.android.material.appbar.AppBarLayout.layout_scrollFlags").forChild());
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("layout_scrollInterpolator").withType("string").forChild());
	}
	
	public AppBarLayoutImpl() {
		super(GROUP_NAME, LOCAL_NAME);
	}
	public  AppBarLayoutImpl(String localname) {
		super(GROUP_NAME, localname);
	}
	public  AppBarLayoutImpl(String groupName, String localname) {
		super(groupName, localname);
	}

	@Override
	public IWidget newInstance() {
		return new AppBarLayoutImpl(groupName, localName);
	}
	
	@SuppressLint("NewApi")
	@Override
	public void create(IFragment fragment, Map<String, Object> params) {
		super.create(fragment, params);
Context context = (Context) fragment.getRootActivity();
	Object systemStyle = params.get("systemStyle");
	Object systemAndroidAttrStyle = params.get("systemAndroidAttrStyle");
	
	if (systemStyle == null && systemAndroidAttrStyle == null) {
		appBarLayout = new AppBarLayoutExt(context);
	} else {
		int defStyleAttr = 0;
		int defStyleRes = 0;
		
		if (systemStyle != null) {
			defStyleRes = context.getResources().getIdentifier((String) systemStyle, "style", context.getPackageName());	
		}
		
		if (systemAndroidAttrStyle != null) {
			defStyleAttr = context.getResources().getIdentifier((String) systemAndroidAttrStyle, "attr", "android");	
		}
		
		if (defStyleRes == 0) {
			appBarLayout = new AppBarLayoutExt(context, null, defStyleAttr);	
		} else {
		}
		
	}

		
		nativeCreate(params);
		
		
		ViewGroupImpl.registerCommandConveter(this);

	}

	@Override
	public Object asWidget() {
		return appBarLayout;
	}

	@Override
	public boolean remove(IWidget w) {
		boolean remove = super.remove(w);
		appBarLayout.removeView((View) w.asWidget());
		return remove;
	}
	
	@Override
    public boolean remove(int index) {
		IWidget widget = widgets.get(index);
        boolean remove = super.remove(index);

        if (index + 1 <= appBarLayout.getChildCount()) {
            appBarLayout.removeViewAt(index);
        }    
        return remove;
    }

	
	@Override
	public void add(IWidget w, int index) {
		if (index != -2) {
			View view = (View) w.asWidget();
			createLayoutParams(view);
			    if (index == -1) {
			        appBarLayout.addView(view);
			    } else {
			        appBarLayout.addView(view, index);
			    }
		}
		
		ViewGroupImpl.nativeAddView(asNativeWidget(), w.asNativeWidget());
		super.add(w, index);
	}
	
	private void createLayoutParams(View view) {
		com.google.android.material.appbar.AppBarLayout.LayoutParams layoutParams = (com.google.android.material.appbar.AppBarLayout.LayoutParams) view.getLayoutParams();
		
		layoutParams = (com.google.android.material.appbar.AppBarLayout.LayoutParams) view.getLayoutParams();
		if (layoutParams == null) {
			layoutParams = new com.google.android.material.appbar.AppBarLayout.LayoutParams(-2, -2);
			view.setLayoutParams(layoutParams);
		}  else {
			layoutParams.height = -2;
			layoutParams.width = -2;
		}
	}
	
	private com.google.android.material.appbar.AppBarLayout.LayoutParams getLayoutParams(View view) {
		return (com.google.android.material.appbar.AppBarLayout.LayoutParams) view.getLayoutParams();		
	}
	
	@SuppressLint("NewApi")
	@Override
	public void setChildAttribute(IWidget w, WidgetAttribute key, String strValue, Object objValue) {
		View view = (View) w.asWidget();
		com.google.android.material.appbar.AppBarLayout.LayoutParams layoutParams = getLayoutParams(view);
		ViewGroupImpl.setChildAttribute(w, key, objValue, layoutParams);		
		
		switch (key.getAttributeName()) {
		case "layout_width":
			layoutParams.width = (int) objValue;
			break;	
		case "layout_height":
			layoutParams.height = (int) objValue;
			break;
			case "layout_scrollFlags": {
				
							layoutParams.setScrollFlags((int) objValue);
				
			}
			break;
			case "layout_scrollInterpolator": {
				
							setScrollInterpolator(layoutParams, objValue);
				
			}
			break;
		default:
			break;
		}
		
		
		view.setLayoutParams(layoutParams);		
	}
	
	@SuppressLint("NewApi")
	@Override
	public Object getChildAttribute(IWidget w, WidgetAttribute key) {
		Object attributeValue = ViewGroupImpl.getChildAttribute(w, key);		
		if (attributeValue != null) {
			return attributeValue;
		}
		View view = (View) w.asWidget();
		com.google.android.material.appbar.AppBarLayout.LayoutParams layoutParams = getLayoutParams(view);

		switch (key.getAttributeName()) {
		case "layout_width":
			return layoutParams.width;
		case "layout_height":
			return layoutParams.height;


		}
		
		return null;

	}
	
		
	public class AppBarLayoutExt extends com.google.android.material.appbar.AppBarLayout implements ILifeCycleDecorator, com.ashera.widget.IMaxDimension{
		private MeasureEvent measureFinished = new MeasureEvent();
		private OnLayoutEvent onLayoutEvent = new OnLayoutEvent();
		
		public IWidget getWidget() {
			return AppBarLayoutImpl.this;
		}
		private int mMaxWidth = -1;
		private int mMaxHeight = -1;
		@Override
		public void setMaxWidth(int width) {
			mMaxWidth = width;
		}
		@Override
		public void setMaxHeight(int height) {
			mMaxHeight = height;
		}
		@Override
		public int getMaxWidth() {
			return mMaxWidth;
		}
		@Override
		public int getMaxHeight() {
			return mMaxHeight;
		}

		public AppBarLayoutExt(Context context, android.util.AttributeSet attrs, int defStyleAttr) {
	        super(context, attrs, defStyleAttr);
	    }

		public AppBarLayoutExt(Context context) {
			super(context);
			
		}
		
		@Override
		public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

			if(mMaxWidth > 0) {
	        	widthMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxWidth, MeasureSpec.AT_MOST);
	        }
	        if(mMaxHeight > 0) {
	            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST);

	        }

	        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			IWidgetLifeCycleListener listener = (IWidgetLifeCycleListener) getListener();
			if (listener != null) {
			    measureFinished.setWidth(getMeasuredWidth());
			    measureFinished.setHeight(getMeasuredHeight());
				listener.eventOccurred(EventId.measureFinished, measureFinished);
			}
		}
		
		@Override
		protected void onLayout(boolean changed, int l, int t, int r, int b) {
			super.onLayout(changed, l, t, r, b);
			
			ViewImpl.nativeMakeFrame(asNativeWidget(), l, t, r, b);
			
			replayBufferedEvents();
			
			IWidgetLifeCycleListener listener = (IWidgetLifeCycleListener) getListener();
			if (listener != null) {
				onLayoutEvent.setB(b);
				onLayoutEvent.setL(l);
				onLayoutEvent.setR(r);
				onLayoutEvent.setT(t);
				onLayoutEvent.setChanged(changed);
				listener.eventOccurred(EventId.onLayout, onLayoutEvent);
			}
			
			if (isInvalidateOnFrameChange() && isInitialised()) {
				AppBarLayoutImpl.this.invalidate();
			}
		}	
		
		@Override
		public void onDraw(Canvas canvas) {
			Runnable runnable = () -> super.onDraw(canvas);
			executeMethodListeners("onDraw", runnable, canvas);
		}

		@Override
		public void draw(Canvas canvas) {
			Runnable runnable = () -> super.draw(canvas);
			executeMethodListeners("draw", runnable, canvas);
		}

		@SuppressLint("WrongCall")
		@Override
		public void execute(String method, Object... args) {
			switch (method) {
				case "onDraw":
					setOnMethodCalled(true);
					super.onDraw((Canvas) args[0]);
					break;

				case "draw":
					setOnMethodCalled(true);
					super.draw((Canvas) args[0]);
					break;

				default:
					break;
			}
		}

		public void updateMeasuredDimension(int width, int height) {
			setMeasuredDimension(width, height);
		}


		@Override
		public ILifeCycleDecorator newInstance(IWidget widget) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAttribute(WidgetAttribute widgetAttribute,
				String strValue, Object objValue) {
			throw new UnsupportedOperationException();
		}		
		

		@Override
		public List<String> getMethods() {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public void initialized() {
			throw new UnsupportedOperationException();
		}
		
        @Override
        public Object getAttribute(WidgetAttribute widgetAttribute) {
            throw new UnsupportedOperationException();
        }
        @Override
        public void drawableStateChanged() {
        	super.drawableStateChanged();
        	if (!isWidgetDisposed()) {
        		ViewImpl.drawableStateChanged(AppBarLayoutImpl.this);
        	}
        }
        
    	public void setState0(float value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 0, value);
    	}
    	public void setState0(int value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 0, value);
    	}
    	public void setState0(double value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 0, value);
    	}
    	
    	public void setState0(Float value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 0, value);
    	}
    	public void setState0(Integer value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 0, value);
    	}
    	public void setState0(Double value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 0, value);
    	}
    	public void setState0(Object value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 0, value);
    	}
    	public void setState1(float value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 1, value);
    	}
    	public void setState1(int value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 1, value);
    	}
    	public void setState1(double value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 1, value);
    	}
    	
    	public void setState1(Float value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 1, value);
    	}
    	public void setState1(Integer value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 1, value);
    	}
    	public void setState1(Double value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 1, value);
    	}
    	public void setState1(Object value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 1, value);
    	}
    	public void setState2(float value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 2, value);
    	}
    	public void setState2(int value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 2, value);
    	}
    	public void setState2(double value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 2, value);
    	}
    	
    	public void setState2(Float value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 2, value);
    	}
    	public void setState2(Integer value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 2, value);
    	}
    	public void setState2(Double value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 2, value);
    	}
    	public void setState2(Object value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 2, value);
    	}
    	public void setState3(float value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 3, value);
    	}
    	public void setState3(int value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 3, value);
    	}
    	public void setState3(double value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 3, value);
    	}
    	
    	public void setState3(Float value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 3, value);
    	}
    	public void setState3(Integer value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 3, value);
    	}
    	public void setState3(Double value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 3, value);
    	}
    	public void setState3(Object value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 3, value);
    	}
    	public void setState4(float value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 4, value);
    	}
    	public void setState4(int value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 4, value);
    	}
    	public void setState4(double value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 4, value);
    	}
    	
    	public void setState4(Float value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 4, value);
    	}
    	public void setState4(Integer value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 4, value);
    	}
    	public void setState4(Double value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 4, value);
    	}
    	public void setState4(Object value) {
    		ViewImpl.setState(AppBarLayoutImpl.this, 4, value);
    	}
        	public void state0() {
        		ViewImpl.state(AppBarLayoutImpl.this, 0);
        	}
        	public void state1() {
        		ViewImpl.state(AppBarLayoutImpl.this, 1);
        	}
        	public void state2() {
        		ViewImpl.state(AppBarLayoutImpl.this, 2);
        	}
        	public void state3() {
        		ViewImpl.state(AppBarLayoutImpl.this, 3);
        	}
        	public void state4() {
        		ViewImpl.state(AppBarLayoutImpl.this, 4);
        	}
                        
        public void stateYes() {
        	ViewImpl.stateYes(AppBarLayoutImpl.this);
        	
        }
        
        public void stateNo() {
        	ViewImpl.stateNo(AppBarLayoutImpl.this);
        }
     
	
	}
	@Override
	public Class getViewClass() {
		return AppBarLayoutExt.class;
	}
	
	@SuppressLint("NewApi")
	@Override
	public void setAttribute(WidgetAttribute key, String strValue, Object objValue, ILifeCycleDecorator decorator) {
				ViewGroupImpl.setAttribute(this,  key, strValue, objValue, decorator);
		Object nativeWidget = asNativeWidget();
		switch (key.getAttributeName()) {
			case "statusBarForeground": {


	appBarLayout.setStatusBarForeground((Drawable)objValue);



			}
			break;
			case "expanded": {


	appBarLayout.setExpanded((boolean)objValue);



			}
			break;
			case "liftOnScroll": {


	appBarLayout.setLiftOnScroll((boolean)objValue);



			}
			break;
			case "liftOnScrollTargetViewId": {


	appBarLayout.setLiftOnScrollTargetViewId((int)objValue);



			}
			break;
			case "elevation": {


	appBarLayout.setTargetElevation((float)objValue);



			}
			break;
		default:
			break;
		}
		
	}
	
	@Override
	@SuppressLint("NewApi")
	public Object getAttribute(WidgetAttribute key, ILifeCycleDecorator decorator) {
		Object attributeValue = ViewGroupImpl.getAttribute(this, key, decorator);
		if (attributeValue != null) {
			return attributeValue;
		}
		Object nativeWidget = asNativeWidget();
		switch (key.getAttributeName()) {
		}
		return null;
	}


	@Override
    public Object asNativeWidget() {
        return appBarLayout;
    }
    private void nativeCreate(Map<String, Object> params) {
    }
    
    @Override
    public void requestLayout() {
    	if (isInitialised()) {
    		ViewImpl.requestLayout(this, asNativeWidget());
    	}
    }
    
    @Override
    public void invalidate() {
    	if (isInitialised()) {
    		ViewImpl.invalidate(this, asNativeWidget());
    	}
    }
    
	

	@Override
	public void setId(String id){
		if (id != null && !id.equals("")){
			super.setId(id);
			appBarLayout.setId((int) quickConvert(id, "id"));
		}
	}
	
    

		//end - body


private void setScrollInterpolator(com.google.android.material.appbar.AppBarLayout.LayoutParams layoutParams, Object objValue) {
	String value = (String) objValue;
	String inlineResource = fragment.getInlineResource(value);
	if (fragment.getRootDirectory() == null && inlineResource == null) {
		String key1 = value.replaceFirst("@anim/", "");
		Context context = (Context) getFragment().getRootActivity();
		int identifier = context.getResources().getIdentifier(key1, "anim", context.getPackageName());
		android.view.animation.Interpolator interpolator = android.view.animation.AnimationUtils.loadInterpolator(context, identifier);
		layoutParams.setScrollInterpolator(interpolator);
	} else {
		layoutParams.setScrollInterpolator(ViewImpl.getInterpolator(this, objValue));
	}
	
}
}
