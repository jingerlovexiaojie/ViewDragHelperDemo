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
 * Created by Nala on 2017/12/6.
 */

public class AutoBackDragger extends LinearLayout {

       private ViewDragHelper mDragger;
    private View mDragView;
    private Point mAutoBackOrignalPos = new Point();

    public AutoBackDragger(Context context) {
        this(context, null);
    }

    public AutoBackDragger(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoBackDragger(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDragger = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return top;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return left;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (releasedChild == mDragView) {
                    mDragger.settleCapturedViewAt(mAutoBackOrignalPos.x,mAutoBackOrignalPos.y);
                    invalidate();
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
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
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed,l,t,r,b);
        mAutoBackOrignalPos.x = mDragView.getLeft();
        mAutoBackOrignalPos.y = mDragView.getTop();

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDragView = getChildAt(0);

    }

    @Override
    public void computeScroll() {
        if(mDragger.continueSettling(true)){
            invalidate();
        }
    }
}
