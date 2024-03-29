//
//  Generated by the J2ObjC translator.  DO NOT EDIT!
//  source: D:\Java\git\core-ios-widgets\IOSAppBarLayoutPlugin\src\main\java\com\google\android\material\appbar\HeaderBehavior.java
//

#include "J2ObjC_header.h"

#pragma push_macro("INCLUDE_ALL_HeaderBehavior")
#ifdef RESTRICT_HeaderBehavior
#define INCLUDE_ALL_HeaderBehavior 0
#else
#define INCLUDE_ALL_HeaderBehavior 1
#endif
#undef RESTRICT_HeaderBehavior

#if __has_feature(nullability)
#pragma clang diagnostic push
#pragma GCC diagnostic ignored "-Wnullability"
#pragma GCC diagnostic ignored "-Wnullability-completeness"
#endif

#if !defined (ASHeaderBehavior_) && (INCLUDE_ALL_HeaderBehavior || defined(INCLUDE_ASHeaderBehavior))
#define ASHeaderBehavior_

#define RESTRICT_ViewOffsetBehavior 1
#define INCLUDE_ASViewOffsetBehavior 1
#include "ViewOffsetBehavior.h"

@class ADView;
@class ADXCoordinatorLayout;

/*!
 @brief The <code>Behavior</code> for a view that sits vertically above scrolling a view.See <code>HeaderScrollingViewBehavior</code>
 .
 */
@interface ASHeaderBehavior : ASViewOffsetBehavior

#pragma mark Public

- (instancetype)initPackagePrivate;

#pragma mark Package-Private

/*!
 @brief Return true if the view can be dragged.
 */
- (jboolean)canDragViewWithADView:(ADView *)view;

/*!
 @brief Returns the maximum px offset when <code>view</code> is being dragged.
 */
- (jint)getMaxDragOffsetWithADView:(ADView *)view;

- (jint)getScrollRangeForDragFlingWithADView:(ADView *)view;

- (jint)getTopBottomOffsetForScrollingSibling;

- (void)onFlingFinishedWithADXCoordinatorLayout:(ADXCoordinatorLayout *)parent
                                     withADView:(ADView *)layout;

- (jint)scrollWithADXCoordinatorLayout:(ADXCoordinatorLayout *)coordinatorLayout
                            withADView:(ADView *)header
                               withInt:(jint)dy
                               withInt:(jint)minOffset
                               withInt:(jint)maxOffset;

- (jint)setHeaderTopBottomOffsetWithADXCoordinatorLayout:(ADXCoordinatorLayout *)parent
                                              withADView:(ADView *)header
                                                 withInt:(jint)newOffset;

- (jint)setHeaderTopBottomOffsetWithADXCoordinatorLayout:(ADXCoordinatorLayout *)parent
                                              withADView:(ADView *)header
                                                 withInt:(jint)newOffset
                                                 withInt:(jint)minOffset
                                                 withInt:(jint)maxOffset;

@end

J2OBJC_EMPTY_STATIC_INIT(ASHeaderBehavior)

FOUNDATION_EXPORT void ASHeaderBehavior_initPackagePrivate(ASHeaderBehavior *self);

J2OBJC_TYPE_LITERAL_HEADER(ASHeaderBehavior)

@compatibility_alias ComGoogleAndroidMaterialAppbarHeaderBehavior ASHeaderBehavior;

#endif


#if __has_feature(nullability)
#pragma clang diagnostic pop
#endif
#pragma pop_macro("INCLUDE_ALL_HeaderBehavior")
