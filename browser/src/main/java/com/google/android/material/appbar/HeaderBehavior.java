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

import r.android.content.Context;
import r.android.util.AttributeSet;
import r.android.view.MotionEvent;
//
import r.android.view.View;
//
//
import r.android.annotation.NonNull;
import r.android.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior;
import r.android.util.MathUtils;
import androidx.core.view.ViewCompat;

/**
 * The {@link Behavior} for a view that sits vertically above scrolling a view. See {@link
 * HeaderScrollingViewBehavior}.
 */
abstract class HeaderBehavior<V extends View> extends ViewOffsetBehavior<V> {

  private static final int INVALID_POINTER = -1;

  @Nullable private Runnable flingRunnable;
  //OverScroller scroller;

  private boolean isBeingDragged;
  private int activePointerId = INVALID_POINTER;
  private int lastMotionY;
  private int touchSlop = -1;
  //@Nullable private VelocityTracker velocityTracker;

  public HeaderBehavior() {}

  int setHeaderTopBottomOffset(CoordinatorLayout parent, V header, int newOffset) {
    return setHeaderTopBottomOffset(
        parent, header, newOffset, Integer.MIN_VALUE, Integer.MAX_VALUE);
  }

  int setHeaderTopBottomOffset(
      CoordinatorLayout parent, V header, int newOffset, int minOffset, int maxOffset) {
    final int curOffset = getTopAndBottomOffset();
    int consumed = 0;

    if (minOffset != 0 && curOffset >= minOffset && curOffset <= maxOffset) {
      // If we have some scrolling range, and we're currently within the min and max
      // offsets, calculate a new offset
      newOffset = MathUtils.clamp(newOffset, minOffset, maxOffset);

      if (curOffset != newOffset) {
        setTopAndBottomOffset(newOffset);
        // Update how much dy we have consumed
        consumed = curOffset - newOffset;
      }
    }

    return consumed;
  }

  int getTopBottomOffsetForScrollingSibling() {
    return getTopAndBottomOffset();
  }

  final int scroll(
      CoordinatorLayout coordinatorLayout, V header, int dy, int minOffset, int maxOffset) {
    return setHeaderTopBottomOffset(
        coordinatorLayout,
        header,
        getTopBottomOffsetForScrollingSibling() - dy,
        minOffset,
        maxOffset);
  }

  void onFlingFinished(CoordinatorLayout parent, V layout) {
    // no-op
  }

 /** Return true if the view can be dragged. */
  boolean canDragView(V view) {
    return false;
  }

 /** Returns the maximum px offset when {@code view} is being dragged. */
  int getMaxDragOffset(@NonNull V view) {
    return -view.getHeight();
  }

  int getScrollRangeForDragFling(@NonNull V view) {
    return view.getHeight();
  }

  }