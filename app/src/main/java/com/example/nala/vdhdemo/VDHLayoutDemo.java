package com.example.nala.vdhdemo;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Nala on 2017/12/5.
 */

public class VDHLayoutDemo extends LinearLayout {
    private ViewDragHelper mDragger;
    private View mDragView;
    private View mAutoBackView;
    private View mEdgeTrackerView;
    private Point mAutoBackOrignPos = new Point();

    public VDHLayoutDemo(Context context) {
        this(context,null);
    }

    public VDHLayoutDemo(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public VDHLayoutDemo(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDragger = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == mDragView || child == mAutoBackView;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
               return left;

            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
              return top;
            }

            @Override
            public void onEdgeTouched(int edgeFlags, int pointerId) {
                super.onEdgeTouched(edgeFlags, pointerId);
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if(releasedChild == mAutoBackView){
                    mDragger.settleCapturedViewAt(mAutoBackOrignPos.x ,mAutoBackOrignPos.y);
                    invalidate();
                }
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                mDragger.captureChildView(mEdgeTrackerView,pointerId);
            }
        });
        mDragger.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        if(action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP){
            mDragger.cancel();
            return false;
        }
        return mDragger.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragger.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if(mDragger.continueSettling(true)){
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean c, int l, int t, int  r, int  b) {
        super.onLayout(c, l, t, r, b);
        mAutoBackOrignPos.x = mAutoBackView.getLeft();
        mAutoBackOrignPos.y = mAutoBackView.getTop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
       mDragView = getChildAt(0);
       mAutoBackView = getChildAt(1);
       mEdgeTrackerView = getChildAt(2);

    }
}
