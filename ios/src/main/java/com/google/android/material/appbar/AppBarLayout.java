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
/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.material.appbar;
import static java.lang.Math.abs;
import r.android.animation.ValueAnimator;
import r.android.graphics.Rect;
import r.android.graphics.drawable.Drawable;
import r.android.os.Build;
import r.android.os.Build.VERSION;
import r.android.os.Build.VERSION_CODES;
import r.android.view.View;
import r.android.view.ViewGroup;
import r.android.view.animation.Interpolator;
import r.android.widget.AbsListView;
import r.android.widget.LinearLayout;
import r.android.widget.ScrollView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
public class AppBarLayout extends LinearLayout implements CoordinatorLayout.AttachedBehavior {
  static final int PENDING_ACTION_NONE=0x0;
  static final int PENDING_ACTION_EXPANDED=0x1;
  static final int PENDING_ACTION_COLLAPSED=1 << 1;
  static final int PENDING_ACTION_ANIMATE_ENABLED=1 << 2;
  static final int PENDING_ACTION_FORCE=1 << 3;
public interface BaseOnOffsetChangedListener<T extends AppBarLayout> {
    void onOffsetChanged(    T appBarLayout,    int verticalOffset);
  }
public interface OnOffsetChangedListener extends BaseOnOffsetChangedListener<AppBarLayout> {
    void onOffsetChanged(    AppBarLayout appBarLayout,    int verticalOffset);
  }
public interface LiftOnScrollListener {
    void onUpdate(    float elevation,    int backgroundColor);
  }
  private static final int INVALID_SCROLL_RANGE=-1;
  private int currentOffset;
  private int totalScrollRange=INVALID_SCROLL_RANGE;
  private int downPreScrollRange=INVALID_SCROLL_RANGE;
  private int downScrollRange=INVALID_SCROLL_RANGE;
  private boolean haveChildWithInterpolator;
  private int pendingAction=PENDING_ACTION_NONE;
  private WindowInsetsCompat lastInsets;
  private List<BaseOnOffsetChangedListener> listeners;
  private boolean liftableOverride;
  private boolean liftable;
  private boolean lifted;
  private boolean liftOnScroll;
  private int liftOnScrollTargetViewId;
  private WeakReference<View> liftOnScrollTargetView;
  private final boolean hasLiftOnScrollColor;
  private final List<LiftOnScrollListener> liftOnScrollListeners=new ArrayList<>();
  private Drawable statusBarForeground;
  private final float appBarElevation;
  private Behavior behavior;
  public void addOnOffsetChangedListener(  BaseOnOffsetChangedListener listener){
    if (listeners == null) {
      listeners=new ArrayList<>();
    }
    if (listener != null && !listeners.contains(listener)) {
      listeners.add(listener);
    }
  }
  public void addOnOffsetChangedListener(  OnOffsetChangedListener listener){
    addOnOffsetChangedListener((BaseOnOffsetChangedListener)listener);
  }
  public void removeOnOffsetChangedListener(  BaseOnOffsetChangedListener listener){
    if (listeners != null && listener != null) {
      listeners.remove(listener);
    }
  }
  public void removeOnOffsetChangedListener(  OnOffsetChangedListener listener){
    removeOnOffsetChangedListener((BaseOnOffsetChangedListener)listener);
  }
  public void addLiftOnScrollListener(  LiftOnScrollListener liftOnScrollListener){
    liftOnScrollListeners.add(liftOnScrollListener);
  }
  public boolean removeLiftOnScrollListener(  LiftOnScrollListener liftOnScrollListener){
    return liftOnScrollListeners.remove(liftOnScrollListener);
  }
  public void clearLiftOnScrollListener(){
    liftOnScrollListeners.clear();
  }
  protected void onMeasure(  int widthMeasureSpec,  int heightMeasureSpec){
    super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    final int heightMode=MeasureSpec.getMode(heightMeasureSpec);
    if (heightMode != MeasureSpec.EXACTLY && ViewCompat.getFitsSystemWindows(this) && shouldOffsetFirstChild()) {
      int newHeight=getMeasuredHeight();
switch (heightMode) {
case MeasureSpec.AT_MOST:
        newHeight=r.android.util.MathUtils.clamp(getMeasuredHeight() + getTopInset(),0,MeasureSpec.getSize(heightMeasureSpec));
      break;
case MeasureSpec.UNSPECIFIED:
    newHeight+=getTopInset();
  break;
case MeasureSpec.EXACTLY:
default :
}
setMeasuredDimension(getMeasuredWidth(),newHeight);
}
invalidateScrollRanges();
}
protected void onLayout(boolean changed,int l,int t,int r,int b){
super.onLayout(changed,l,t,r,b);
if (ViewCompat.getFitsSystemWindows(this) && shouldOffsetFirstChild()) {
final int topInset=getTopInset();
for (int z=getChildCount() - 1; z >= 0; z--) {
ViewCompat.offsetTopAndBottom(getChildAt(z),topInset);
}
}
invalidateScrollRanges();
haveChildWithInterpolator=false;
for (int i=0, z=getChildCount(); i < z; i++) {
final View child=getChildAt(i);
final LayoutParams childLp=(LayoutParams)child.getLayoutParams();
final Interpolator interpolator=childLp.getScrollInterpolator();
if (interpolator != null) {
haveChildWithInterpolator=true;
break;
}
}
if (statusBarForeground != null) {
statusBarForeground.setBounds(0,0,getWidth(),getTopInset());
}
if (!liftableOverride) {
setLiftableState(liftOnScroll || hasCollapsibleChild());
}
}
private boolean hasCollapsibleChild(){
for (int i=0, z=getChildCount(); i < z; i++) {
if (((LayoutParams)getChildAt(i).getLayoutParams()).isCollapsible()) {
return true;
}
}
return false;
}
public void setOrientation(int orientation){
if (orientation != VERTICAL) {
throw new IllegalArgumentException("AppBarLayout is always vertical and does not support horizontal orientation");
}
super.setOrientation(orientation);
}
public CoordinatorLayout.Behavior<AppBarLayout> getBehavior(){
behavior=new AppBarLayout.Behavior();
return behavior;
}
public void setExpanded(boolean expanded){
setExpanded(expanded,ViewCompat.isLaidOut(this));
}
public void setExpanded(boolean expanded,boolean animate){
setExpanded(expanded,animate,true);
}
private void setExpanded(boolean expanded,boolean animate,boolean force){
pendingAction=(expanded ? PENDING_ACTION_EXPANDED : PENDING_ACTION_COLLAPSED) | (animate ? PENDING_ACTION_ANIMATE_ENABLED : 0) | (force ? PENDING_ACTION_FORCE : 0);
requestLayout();
}
protected boolean checkLayoutParams(ViewGroup.LayoutParams p){
return p instanceof LayoutParams;
}
protected LayoutParams generateDefaultLayoutParams(){
return new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
}
boolean hasChildWithInterpolator(){
return haveChildWithInterpolator;
}
public final int getTotalScrollRange(){
if (totalScrollRange != INVALID_SCROLL_RANGE) {
return totalScrollRange;
}
int range=0;
for (int i=0, z=getChildCount(); i < z; i++) {
final View child=getChildAt(i);
if (child.getVisibility() == GONE) {
continue;
}
final LayoutParams lp=(LayoutParams)child.getLayoutParams();
final int childHeight=child.getMeasuredHeight();
final int flags=lp.scrollFlags;
if ((flags & LayoutParams.SCROLL_FLAG_SCROLL) != 0) {
range+=childHeight + lp.topMargin + lp.bottomMargin;
if (i == 0 && ViewCompat.getFitsSystemWindows(child)) {
  range-=getTopInset();
}
if ((flags & LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED) != 0) {
  range-=ViewCompat.getMinimumHeight(child);
  break;
}
}
 else {
break;
}
}
return totalScrollRange=Math.max(0,range);
}
boolean hasScrollableChildren(){
return getTotalScrollRange() != 0;
}
int getUpNestedPreScrollRange(){
return getTotalScrollRange();
}
int getDownNestedPreScrollRange(){
if (downPreScrollRange != INVALID_SCROLL_RANGE) {
return downPreScrollRange;
}
int range=0;
for (int i=getChildCount() - 1; i >= 0; i--) {
final View child=getChildAt(i);
if (child.getVisibility() == GONE) {
continue;
}
final LayoutParams lp=(LayoutParams)child.getLayoutParams();
final int childHeight=child.getMeasuredHeight();
final int flags=lp.scrollFlags;
if ((flags & LayoutParams.FLAG_QUICK_RETURN) == LayoutParams.FLAG_QUICK_RETURN) {
int childRange=lp.topMargin + lp.bottomMargin;
if ((flags & LayoutParams.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED) != 0) {
  childRange+=ViewCompat.getMinimumHeight(child);
}
 else if ((flags & LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED) != 0) {
  childRange+=childHeight - ViewCompat.getMinimumHeight(child);
}
 else {
  childRange+=childHeight;
}
if (i == 0 && ViewCompat.getFitsSystemWindows(child)) {
  childRange=Math.min(childRange,childHeight - getTopInset());
}
range+=childRange;
}
 else if (range > 0) {
break;
}
}
return downPreScrollRange=Math.max(0,range);
}
int getDownNestedScrollRange(){
if (downScrollRange != INVALID_SCROLL_RANGE) {
return downScrollRange;
}
int range=0;
for (int i=0, z=getChildCount(); i < z; i++) {
final View child=getChildAt(i);
if (child.getVisibility() == GONE) {
continue;
}
final LayoutParams lp=(LayoutParams)child.getLayoutParams();
int childHeight=child.getMeasuredHeight();
childHeight+=lp.topMargin + lp.bottomMargin;
final int flags=lp.scrollFlags;
if ((flags & LayoutParams.SCROLL_FLAG_SCROLL) != 0) {
range+=childHeight;
if ((flags & LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED) != 0) {
  range-=ViewCompat.getMinimumHeight(child);
  break;
}
}
 else {
break;
}
}
return downScrollRange=Math.max(0,range);
}
void onOffsetChanged(int offset){
currentOffset=offset;
if (false/*!willNotDraw()*/) {
//ViewCompat.postInvalidateOnAnimation(this);
}
if (listeners != null) {
for (int i=0, z=listeners.size(); i < z; i++) {
final BaseOnOffsetChangedListener listener=listeners.get(i);
if (listener != null) {
  listener.onOffsetChanged(this,offset);
}
}
}
}
public final int getMinimumHeightForVisibleOverlappingContent(){
final int topInset=getTopInset();
final int minHeight=ViewCompat.getMinimumHeight(this);
if (minHeight != 0) {
return (minHeight * 2) + topInset;
}
final int childCount=getChildCount();
final int lastChildMinHeight=childCount >= 1 ? ViewCompat.getMinimumHeight(getChildAt(childCount - 1)) : 0;
if (lastChildMinHeight != 0) {
return (lastChildMinHeight * 2) + topInset;
}
return getHeight() / 3;
}
public boolean setLiftable(boolean liftable){
this.liftableOverride=true;
return setLiftableState(liftable);
}
public void setLiftableOverrideEnabled(boolean enabled){
this.liftableOverride=enabled;
}
private boolean setLiftableState(boolean liftable){
if (this.liftable != liftable) {
this.liftable=liftable;
refreshDrawableState();
return true;
}
return false;
}
public boolean isLifted(){
return lifted;
}
boolean setLiftedState(boolean lifted){
return setLiftedState(lifted,!liftableOverride);
}
boolean setLiftedState(boolean lifted,boolean force){
if (force && this.lifted != lifted) {
this.lifted=lifted;
refreshDrawableState();
if (isLiftOnScrollCompatibleBackground()) {
if (hasLiftOnScrollColor) {
  startLiftOnScrollColorAnimation(lifted ? 0 : 1,lifted ? 1 : 0);
}
 else if (liftOnScroll) {
  startLiftOnScrollColorAnimation(lifted ? 0 : appBarElevation,lifted ? appBarElevation : 0);
}
}
return true;
}
return false;
}
public void setLiftOnScroll(boolean liftOnScroll){
this.liftOnScroll=liftOnScroll;
}
public boolean isLiftOnScroll(){
return liftOnScroll;
}
boolean shouldLift(View defaultScrollingView){
View scrollingView=findLiftOnScrollTargetView(defaultScrollingView);
if (scrollingView == null) {
scrollingView=defaultScrollingView;
}
return scrollingView != null && (scrollingView.canScrollVertically(-1) || scrollingView.getScrollY() > 0);
}
private View findLiftOnScrollTargetView(View defaultScrollingView){
if (liftOnScrollTargetView == null && liftOnScrollTargetViewId != View.NO_ID) {
View targetView=null;
if (defaultScrollingView != null) {
targetView=defaultScrollingView.findViewById(liftOnScrollTargetViewId);
}
if (targetView == null && getParent() instanceof ViewGroup) {
targetView=((ViewGroup)getParent()).findViewById(liftOnScrollTargetViewId);
}
if (targetView != null) {
liftOnScrollTargetView=new WeakReference<>(targetView);
}
}
return liftOnScrollTargetView != null ? liftOnScrollTargetView.get() : null;
}
int getPendingAction(){
return pendingAction;
}
void resetPendingAction(){
pendingAction=PENDING_ACTION_NONE;
}
final int getTopInset(){
return lastInsets != null ? lastInsets.getSystemWindowInsetTop() : 0;
}
private boolean shouldOffsetFirstChild(){
if (getChildCount() > 0) {
final View firstChild=getChildAt(0);
return firstChild.getVisibility() != GONE && !ViewCompat.getFitsSystemWindows(firstChild);
}
return false;
}
public static class LayoutParams extends LinearLayout.LayoutParams {
public static final int SCROLL_FLAG_NO_SCROLL=0x0;
public static final int SCROLL_FLAG_SCROLL=0x1;
public static final int SCROLL_FLAG_EXIT_UNTIL_COLLAPSED=1 << 1;
public static final int SCROLL_FLAG_ENTER_ALWAYS=1 << 2;
public static final int SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED=1 << 3;
public static final int SCROLL_FLAG_SNAP=1 << 4;
public static final int SCROLL_FLAG_SNAP_MARGINS=1 << 5;
static final int FLAG_QUICK_RETURN=SCROLL_FLAG_SCROLL | SCROLL_FLAG_ENTER_ALWAYS;
static final int FLAG_SNAP=SCROLL_FLAG_SCROLL | SCROLL_FLAG_SNAP;
static final int COLLAPSIBLE_FLAGS=SCROLL_FLAG_EXIT_UNTIL_COLLAPSED | SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED;
int scrollFlags=SCROLL_FLAG_SCROLL;
public static final int SCROLL_EFFECT_NONE=0;
public static final int SCROLL_EFFECT_COMPRESS=1;
private ChildScrollEffect scrollEffect;
Interpolator scrollInterpolator;
public LayoutParams(int width,int height){
super(width,height);
}
public LayoutParams(int width,int height,float weight){
super(width,height,weight);
}
public LayoutParams(ViewGroup.LayoutParams p){
super(p);
}
public LayoutParams(LayoutParams source){
super(source);
scrollFlags=source.scrollFlags;
scrollEffect=source.scrollEffect;
scrollInterpolator=source.scrollInterpolator;
}
public void setScrollFlags(int flags){
scrollFlags=flags;
}
public int getScrollFlags(){
return scrollFlags;
}
public ChildScrollEffect getScrollEffect(){
return scrollEffect;
}
public void setScrollInterpolator(Interpolator interpolator){
scrollInterpolator=interpolator;
}
public Interpolator getScrollInterpolator(){
return scrollInterpolator;
}
boolean isCollapsible(){
return (scrollFlags & SCROLL_FLAG_SCROLL) == SCROLL_FLAG_SCROLL && (scrollFlags & COLLAPSIBLE_FLAGS) != 0;
}
}
public static class Behavior extends BaseBehavior<AppBarLayout> {
public Behavior(){
super();
}
}
protected static class BaseBehavior<T extends AppBarLayout> extends HeaderBehavior<T> {
private static final int MAX_OFFSET_ANIMATION_DURATION=600;
public abstract static class BaseDragCallback<T extends AppBarLayout> {
public abstract boolean canDrag(T appBarLayout);
}
private int offsetDelta;
private int lastStartedType;
private ValueAnimator offsetAnimator;
private SavedState savedState;
private WeakReference<View> lastNestedScrollingChildRef;
private BaseDragCallback onDragCallback;
private boolean coordinatorLayoutA11yScrollable;
public BaseBehavior(){
}
public boolean onStartNestedScroll(CoordinatorLayout parent,T child,View directTargetChild,View target,int nestedScrollAxes,int type){
final boolean started=(nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0 && (child.isLiftOnScroll() || canScrollChildren(parent,child,directTargetChild));
if (started && offsetAnimator != null) {
offsetAnimator.cancel();
}
lastNestedScrollingChildRef=null;
lastStartedType=type;
return started;
}
private boolean canScrollChildren(CoordinatorLayout parent,T child,View directTargetChild){
return child.hasScrollableChildren() && parent.getHeight() - directTargetChild.getHeight() <= child.getHeight();
}
public void onNestedPreScroll(CoordinatorLayout coordinatorLayout,T child,View target,int dx,int dy,int[] consumed,int type){
if (dy != 0) {
int min;
int max;
if (dy < 0) {
  min=-child.getTotalScrollRange();
  max=min + child.getDownNestedPreScrollRange();
}
 else {
  min=-child.getUpNestedPreScrollRange();
  max=0;
}
if (min != max) {
  consumed[1]=scroll(coordinatorLayout,child,dy,min,max);
}
}
if (child.isLiftOnScroll()) {
child.setLiftedState(child.shouldLift(target));
}
}
public void onNestedScroll(CoordinatorLayout coordinatorLayout,T child,View target,int dxConsumed,int dyConsumed,int dxUnconsumed,int dyUnconsumed,int type,int[] consumed){
if (dyUnconsumed < 0) {
consumed[1]=scroll(coordinatorLayout,child,dyUnconsumed,-child.getDownNestedScrollRange(),0);
}
if (dyUnconsumed == 0) {
updateAccessibilityActions(coordinatorLayout,child);
}
}
public void onStopNestedScroll(CoordinatorLayout coordinatorLayout,T abl,View target,int type){
if (lastStartedType == ViewCompat.TYPE_TOUCH || type == ViewCompat.TYPE_NON_TOUCH) {
snapToChildIfNeeded(coordinatorLayout,abl);
if (abl.isLiftOnScroll()) {
  abl.setLiftedState(abl.shouldLift(target));
}
}
lastNestedScrollingChildRef=new WeakReference<>(target);
}
public void setDragCallback(BaseDragCallback callback){
onDragCallback=callback;
}
private void animateOffsetTo(final CoordinatorLayout coordinatorLayout,final T child,final int offset,float velocity){
final int distance=abs(getTopBottomOffsetForScrollingSibling() - offset);
final int duration;
velocity=abs(velocity);
if (velocity > 0) {
duration=3 * Math.round(1000 * (distance / velocity));
}
 else {
final float distanceRatio=(float)distance / child.getHeight();
duration=(int)((distanceRatio + 1) * 150);
}
animateOffsetWithDuration(coordinatorLayout,child,offset,duration);
}
private void animateOffsetWithDuration(final CoordinatorLayout coordinatorLayout,final T child,final int offset,final int duration){
final int currentOffset=getTopBottomOffsetForScrollingSibling();
if (currentOffset == offset) {
if (offsetAnimator != null && offsetAnimator.isRunning()) {
  offsetAnimator.cancel();
}
return;
}
if (offsetAnimator == null) {
offsetAnimator=new ValueAnimator();
offsetAnimator.setInterpolator(new r.android.view.animation.DecelerateInterpolator());
offsetAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
  public void onAnimationUpdate(  ValueAnimator animator){
    setHeaderTopBottomOffset(coordinatorLayout,child,(int)animator.getAnimatedValue());coordinatorLayout.onChildViewsChanged(0);
  }
}
);
}
 else {
offsetAnimator.cancel();
}
offsetAnimator.setDuration(Math.min(duration,MAX_OFFSET_ANIMATION_DURATION));
offsetAnimator.setIntValues(currentOffset,offset);
offsetAnimator.start();
}
private int getChildIndexOnOffset(T abl,final int offset){
for (int i=0, count=abl.getChildCount(); i < count; i++) {
View child=abl.getChildAt(i);
int top=child.getTop();
int bottom=child.getBottom();
final LayoutParams lp=(LayoutParams)child.getLayoutParams();
if (checkFlag(lp.getScrollFlags(),LayoutParams.SCROLL_FLAG_SNAP_MARGINS)) {
  top-=lp.topMargin;
  bottom+=lp.bottomMargin;
}
if (top <= -offset && bottom >= -offset) {
  return i;
}
}
return -1;
}
private void snapToChildIfNeeded(CoordinatorLayout coordinatorLayout,T abl){
final int topInset=abl.getTopInset() + abl.getPaddingTop();
final int offset=getTopBottomOffsetForScrollingSibling() - topInset;
final int offsetChildIndex=getChildIndexOnOffset(abl,offset);
if (offsetChildIndex >= 0) {
final View offsetChild=abl.getChildAt(offsetChildIndex);
final LayoutParams lp=(LayoutParams)offsetChild.getLayoutParams();
final int flags=lp.getScrollFlags();
if ((flags & LayoutParams.FLAG_SNAP) == LayoutParams.FLAG_SNAP) {
  int snapTop=-offsetChild.getTop();
  int snapBottom=-offsetChild.getBottom();
  if (offsetChildIndex == 0 && ViewCompat.getFitsSystemWindows(abl) && ViewCompat.getFitsSystemWindows(offsetChild)) {
    snapTop-=abl.getTopInset();
  }
  if (checkFlag(flags,LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED)) {
    snapBottom+=ViewCompat.getMinimumHeight(offsetChild);
  }
 else   if (checkFlag(flags,LayoutParams.FLAG_QUICK_RETURN | LayoutParams.SCROLL_FLAG_ENTER_ALWAYS)) {
    final int seam=snapBottom + ViewCompat.getMinimumHeight(offsetChild);
    if (offset < seam) {
      snapTop=seam;
    }
 else {
      snapBottom=seam;
    }
  }
  if (checkFlag(flags,LayoutParams.SCROLL_FLAG_SNAP_MARGINS)) {
    snapTop+=lp.topMargin;
    snapBottom-=lp.bottomMargin;
  }
  final int newOffset=calculateSnapOffset(offset,snapBottom,snapTop) + topInset;
  animateOffsetTo(coordinatorLayout,abl,r.android.util.MathUtils.clamp(newOffset,-abl.getTotalScrollRange(),0),0);
}
}
}
private int calculateSnapOffset(int value,int bottom,int top){
return value < (bottom + top) / 2 ? bottom : top;
}
private static boolean checkFlag(final int flags,final int check){
return (flags & check) == check;
}
public boolean onMeasureChild(CoordinatorLayout parent,T child,int parentWidthMeasureSpec,int widthUsed,int parentHeightMeasureSpec,int heightUsed){
final CoordinatorLayout.LayoutParams lp=(CoordinatorLayout.LayoutParams)child.getLayoutParams();
if (lp.height == CoordinatorLayout.LayoutParams.WRAP_CONTENT) {
parent.onMeasureChild(child,parentWidthMeasureSpec,widthUsed,MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED),heightUsed);
return true;
}
return super.onMeasureChild(parent,child,parentWidthMeasureSpec,widthUsed,parentHeightMeasureSpec,heightUsed);
}
public boolean onLayoutChild(CoordinatorLayout parent,T abl,int layoutDirection){
boolean handled=super.onLayoutChild(parent,abl,layoutDirection);
final int pendingAction=abl.getPendingAction();
if (savedState != null && (pendingAction & PENDING_ACTION_FORCE) == 0) {
if (savedState.fullyScrolled) {
  setHeaderTopBottomOffset(parent,abl,-abl.getTotalScrollRange());
}
 else if (savedState.fullyExpanded) {
  setHeaderTopBottomOffset(parent,abl,0);
}
 else {
  View child=abl.getChildAt(savedState.firstVisibleChildIndex);
  int offset=-child.getBottom();
  if (savedState.firstVisibleChildAtMinimumHeight) {
    offset+=ViewCompat.getMinimumHeight(child) + abl.getTopInset();
  }
 else {
    offset+=Math.round(child.getHeight() * savedState.firstVisibleChildPercentageShown);
  }
  setHeaderTopBottomOffset(parent,abl,offset);
}
}
 else if (pendingAction != PENDING_ACTION_NONE) {
final boolean animate=(pendingAction & PENDING_ACTION_ANIMATE_ENABLED) != 0;
if ((pendingAction & PENDING_ACTION_COLLAPSED) != 0) {
  final int offset=-abl.getUpNestedPreScrollRange();
  if (animate) {
    animateOffsetTo(parent,abl,offset,0);
  }
 else {
    setHeaderTopBottomOffset(parent,abl,offset);
  }
}
 else if ((pendingAction & PENDING_ACTION_EXPANDED) != 0) {
  if (animate) {
    animateOffsetTo(parent,abl,0,0);
  }
 else {
    setHeaderTopBottomOffset(parent,abl,0);
  }
}
}
abl.resetPendingAction();
savedState=null;
setTopAndBottomOffset(r.android.util.MathUtils.clamp(getTopAndBottomOffset(),-abl.getTotalScrollRange(),0));
updateAppBarLayoutDrawableState(parent,abl,getTopAndBottomOffset(),0,true);
abl.onOffsetChanged(getTopAndBottomOffset());
updateAccessibilityActions(parent,abl);
return handled;
}
private void updateAccessibilityActions(CoordinatorLayout coordinatorLayout,T appBarLayout){
//ViewCompat.removeAccessibilityAction(coordinatorLayout,ACTION_SCROLL_FORWARD.getId());
//ViewCompat.removeAccessibilityAction(coordinatorLayout,ACTION_SCROLL_BACKWARD.getId());
if (appBarLayout.getTotalScrollRange() == 0) {
return;
}
View scrollingView=getChildWithScrollingBehavior(coordinatorLayout);
if (scrollingView == null) {
return;
}
if (!childrenHaveScrollFlags(appBarLayout)) {
return;
}
//
}
private View getChildWithScrollingBehavior(CoordinatorLayout coordinatorLayout){
final int childCount=coordinatorLayout.getChildCount();
for (int i=0; i < childCount; i++) {
final View child=coordinatorLayout.getChildAt(i);
CoordinatorLayout.LayoutParams lp=(CoordinatorLayout.LayoutParams)child.getLayoutParams();
if (lp.getBehavior() instanceof ScrollingViewBehavior) {
  return child;
}
}
return null;
}
private boolean childrenHaveScrollFlags(AppBarLayout appBarLayout){
final int childCount=appBarLayout.getChildCount();
for (int i=0; i < childCount; i++) {
final View child=appBarLayout.getChildAt(i);
final LayoutParams childLp=(LayoutParams)child.getLayoutParams();
final int flags=childLp.scrollFlags;
if (flags != AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL) {
  return true;
}
}
return false;
}
private boolean addAccessibilityScrollActions(final CoordinatorLayout coordinatorLayout,final T appBarLayout,final View scrollingView){
boolean a11yScrollable=false;
if (getTopBottomOffsetForScrollingSibling() != -appBarLayout.getTotalScrollRange()) {
//addActionToExpand(coordinatorLayout,appBarLayout,ACTION_SCROLL_FORWARD,false);
a11yScrollable=true;
}
if (getTopBottomOffsetForScrollingSibling() != 0) {
if (scrollingView.canScrollVertically(-1)) {
  final int dy=-appBarLayout.getDownNestedPreScrollRange();
  if (dy != 0) {
    a11yScrollable=true;
  }
}
 else {
  //addActionToExpand(coordinatorLayout,appBarLayout,ACTION_SCROLL_BACKWARD,true);
  a11yScrollable=true;
}
}
return a11yScrollable;
}
boolean canDragView(T view){
if (onDragCallback != null) {
return onDragCallback.canDrag(view);
}
if (lastNestedScrollingChildRef != null) {
final View scrollingView=lastNestedScrollingChildRef.get();
return scrollingView != null && scrollingView.isShown() && !scrollingView.canScrollVertically(-1);
}
 else {
return true;
}
}
void onFlingFinished(CoordinatorLayout parent,T layout){
snapToChildIfNeeded(parent,layout);
if (layout.isLiftOnScroll()) {
layout.setLiftedState(layout.shouldLift(findFirstScrollingChild(parent)));
}
}
int getMaxDragOffset(T view){
return -view.getDownNestedScrollRange() + view.getTopInset();
}
int getScrollRangeForDragFling(T view){
return view.getTotalScrollRange();
}
int setHeaderTopBottomOffset(CoordinatorLayout coordinatorLayout,T appBarLayout,int newOffset,int minOffset,int maxOffset){
final int curOffset=getTopBottomOffsetForScrollingSibling();
int consumed=0;
if (minOffset != 0 && curOffset >= minOffset && curOffset <= maxOffset) {
newOffset=r.android.util.MathUtils.clamp(newOffset,minOffset,maxOffset);
if (curOffset != newOffset) {
  final int interpolatedOffset=appBarLayout.hasChildWithInterpolator() ? interpolateOffset(appBarLayout,newOffset) : newOffset;
  final boolean offsetChanged=setTopAndBottomOffset(interpolatedOffset);
  consumed=curOffset - newOffset;
  offsetDelta=newOffset - interpolatedOffset;
  if (offsetChanged) {
    for (int i=0; i < appBarLayout.getChildCount(); i++) {
      LayoutParams params=(LayoutParams)appBarLayout.getChildAt(i).getLayoutParams();
      ChildScrollEffect scrollEffect=params.getScrollEffect();
      if (scrollEffect != null && (params.getScrollFlags() & LayoutParams.SCROLL_FLAG_SCROLL) != 0) {
        scrollEffect.onOffsetChanged(appBarLayout,appBarLayout.getChildAt(i),getTopAndBottomOffset());
      }
    }
  }
  if (!offsetChanged && appBarLayout.hasChildWithInterpolator()) {
    coordinatorLayout.dispatchDependentViewsChanged(appBarLayout);
  }
  appBarLayout.onOffsetChanged(getTopAndBottomOffset());
  updateAppBarLayoutDrawableState(coordinatorLayout,appBarLayout,newOffset,newOffset < curOffset ? -1 : 1,false);
}
}
 else {
offsetDelta=0;
}
updateAccessibilityActions(coordinatorLayout,appBarLayout);
return consumed;
}
boolean isOffsetAnimatorRunning(){
return offsetAnimator != null && offsetAnimator.isRunning();
}
private int interpolateOffset(T layout,final int offset){
final int absOffset=abs(offset);
for (int i=0, z=layout.getChildCount(); i < z; i++) {
final View child=layout.getChildAt(i);
final AppBarLayout.LayoutParams childLp=(LayoutParams)child.getLayoutParams();
final Interpolator interpolator=childLp.getScrollInterpolator();
if (absOffset >= child.getTop() && absOffset <= child.getBottom()) {
  if (interpolator != null) {
    int childScrollableHeight=0;
    final int flags=childLp.getScrollFlags();
    if ((flags & LayoutParams.SCROLL_FLAG_SCROLL) != 0) {
      childScrollableHeight+=child.getHeight() + childLp.topMargin + childLp.bottomMargin;
      if ((flags & LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED) != 0) {
        childScrollableHeight-=ViewCompat.getMinimumHeight(child);
      }
    }
    if (ViewCompat.getFitsSystemWindows(child)) {
      childScrollableHeight-=layout.getTopInset();
    }
    if (childScrollableHeight > 0) {
      final int offsetForView=absOffset - child.getTop();
      final int interpolatedDiff=Math.round(childScrollableHeight * interpolator.getInterpolation(offsetForView / (float)childScrollableHeight));
      return Integer.signum(offset) * (child.getTop() + interpolatedDiff);
    }
  }
  break;
}
}
return offset;
}
private void updateAppBarLayoutDrawableState(final CoordinatorLayout parent,final T layout,final int offset,final int direction,final boolean forceJump){
final View child=getAppBarChildOnOffset(layout,offset);
boolean lifted=false;
if (child != null) {
final AppBarLayout.LayoutParams childLp=(LayoutParams)child.getLayoutParams();
final int flags=childLp.getScrollFlags();
if ((flags & LayoutParams.SCROLL_FLAG_SCROLL) != 0) {
  final int minHeight=ViewCompat.getMinimumHeight(child);
  if (direction > 0 && (flags & (LayoutParams.SCROLL_FLAG_ENTER_ALWAYS | LayoutParams.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED)) != 0) {
    lifted=-offset >= child.getBottom() - minHeight - layout.getTopInset();
  }
 else   if ((flags & LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED) != 0) {
    lifted=-offset >= child.getBottom() - minHeight - layout.getTopInset();
  }
}
}
if (layout.isLiftOnScroll()) {
lifted=layout.shouldLift(findFirstScrollingChild(parent));
}
final boolean changed=layout.setLiftedState(lifted);
if (forceJump || (changed && shouldJumpElevationState(parent,layout))) {
if (layout.getBackground() != null) {
  layout.getBackground().jumpToCurrentState();
}
if (VERSION.SDK_INT >= VERSION_CODES.M && layout.getForeground() != null) {
  layout.getForeground().jumpToCurrentState();
}
if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP && layout.getStateListAnimator() != null) {
  layout.getStateListAnimator().jumpToCurrentState();
}
}
}
private boolean shouldJumpElevationState(CoordinatorLayout parent,T layout){
final List<View> dependencies=new ArrayList();
for (int i=0, size=dependencies.size(); i < size; i++) {
final View dependency=dependencies.get(i);
final CoordinatorLayout.LayoutParams lp=(CoordinatorLayout.LayoutParams)dependency.getLayoutParams();
final CoordinatorLayout.Behavior behavior=lp.getBehavior();
if (behavior instanceof ScrollingViewBehavior) {
  return ((ScrollingViewBehavior)behavior).getOverlayTop() != 0;
}
}
return false;
}
private static View getAppBarChildOnOffset(final AppBarLayout layout,final int offset){
final int absOffset=abs(offset);
for (int i=0, z=layout.getChildCount(); i < z; i++) {
final View child=layout.getChildAt(i);
if (absOffset >= child.getTop() && absOffset <= child.getBottom()) {
  return child;
}
}
return null;
}
private View findFirstScrollingChild(CoordinatorLayout parent){
for (int i=0, z=parent.getChildCount(); i < z; i++) {
final View child=parent.getChildAt(i);
if (child instanceof NestedScrollingChild || child instanceof AbsListView || child instanceof ScrollView) {
  return child;
}
}
return null;
}
int getTopBottomOffsetForScrollingSibling(){
return getTopAndBottomOffset() + offsetDelta;
}
void restoreScrollState(SavedState state,boolean force){
if (savedState == null || force) {
savedState=state;
}
}
}
public static class ScrollingViewBehavior extends HeaderScrollingViewBehavior {
public ScrollingViewBehavior(){
}
public boolean layoutDependsOn(CoordinatorLayout parent,View child,View dependency){
return dependency instanceof AppBarLayout;
}
public boolean onDependentViewChanged(CoordinatorLayout parent,View child,View dependency){
offsetChildAsNeeded(child,dependency);
updateLiftedStateIfNeeded(child,dependency);
return false;
}
public void onDependentViewRemoved(CoordinatorLayout parent,View child,View dependency){
if (dependency instanceof AppBarLayout) {
//ViewCompat.removeAccessibilityAction(parent,ACTION_SCROLL_FORWARD.getId());
//ViewCompat.removeAccessibilityAction(parent,ACTION_SCROLL_BACKWARD.getId());
//ViewCompat.setAccessibilityDelegate(parent,null);
}
}
public boolean onRequestChildRectangleOnScreen(CoordinatorLayout parent,View child,Rect rectangle,boolean immediate){
final AppBarLayout header=findFirstDependency(parent.getDependencies(child));
if (header != null) {
Rect offsetRect=new Rect(rectangle);
offsetRect.offset(child.getLeft(),child.getTop());
final Rect parentRect=tempRect1;
parentRect.set(0,0,parent.getWidth(),parent.getHeight());
if (!parentRect.contains(offsetRect)) {
  header.setExpanded(false,!immediate);
  return true;
}
}
return false;
}
private void offsetChildAsNeeded(View child,View dependency){
final CoordinatorLayout.Behavior behavior=((CoordinatorLayout.LayoutParams)dependency.getLayoutParams()).getBehavior();
if (behavior instanceof BaseBehavior) {
final BaseBehavior ablBehavior=(BaseBehavior)behavior;
ViewCompat.offsetTopAndBottom(child,(dependency.getBottom() - child.getTop()) + ablBehavior.offsetDelta + getVerticalLayoutGap() - getOverlapPixelsForOffset(dependency));
}
}
float getOverlapRatioForOffset(final View header){
if (header instanceof AppBarLayout) {
final AppBarLayout abl=(AppBarLayout)header;
final int totalScrollRange=abl.getTotalScrollRange();
final int preScrollDown=abl.getDownNestedPreScrollRange();
final int offset=getAppBarLayoutOffset(abl);
if (preScrollDown != 0 && (totalScrollRange + offset) <= preScrollDown) {
  return 0;
}
 else {
  final int availScrollRange=totalScrollRange - preScrollDown;
  if (availScrollRange != 0) {
    return 1f + (offset / (float)availScrollRange);
  }
}
}
return 0f;
}
private static int getAppBarLayoutOffset(AppBarLayout abl){
final CoordinatorLayout.Behavior behavior=((CoordinatorLayout.LayoutParams)abl.getLayoutParams()).getBehavior();
if (behavior instanceof BaseBehavior) {
return ((BaseBehavior)behavior).getTopBottomOffsetForScrollingSibling();
}
return 0;
}
AppBarLayout findFirstDependency(List<View> views){
for (int i=0, z=views.size(); i < z; i++) {
View view=views.get(i);
if (view instanceof AppBarLayout) {
  return (AppBarLayout)view;
}
}
return null;
}
int getScrollRange(View v){
if (v instanceof AppBarLayout) {
return ((AppBarLayout)v).getTotalScrollRange();
}
 else {
return super.getScrollRange(v);
}
}
private void updateLiftedStateIfNeeded(View child,View dependency){
if (dependency instanceof AppBarLayout) {
AppBarLayout appBarLayout=(AppBarLayout)dependency;
if (appBarLayout.isLiftOnScroll()) {
  appBarLayout.setLiftedState(appBarLayout.shouldLift(child));
}
}
}
}
public abstract static class ChildScrollEffect {
public abstract void onOffsetChanged(AppBarLayout appBarLayout,View child,float offset);
}
public AppBarLayout(){
hasLiftOnScrollColor=false;
appBarElevation=0;
setOrientation(VERTICAL);
}
private void invalidateScrollRanges(){
}
private boolean isLiftOnScrollCompatibleBackground(){
return false;
}
private void startLiftOnScrollColorAnimation(float fromValue,float toValue){
}
class SavedState {
boolean fullyScrolled;
boolean fullyExpanded;
int firstVisibleChildIndex;
float firstVisibleChildPercentageShown;
boolean firstVisibleChildAtMinimumHeight;
}
public Drawable getStateListAnimator(){
return null;
}
}
