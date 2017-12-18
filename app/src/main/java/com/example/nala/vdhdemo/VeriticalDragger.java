package com.example.nala.vdhdemo;

import android.content.Context;
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

public class VeriticalDragger extends LinearLayout {

    private ViewDragHelper mDragger;
    private View mDragView;

    public VeriticalDragger(Context context) {
        this(context,null);
    }

    public VeriticalDragger(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public VeriticalDragger(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDragger = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                final int topBound = getPaddingTop();
                final int bottomBound = getHeight() - mDragView.getHeight();

                int newTop = Math.min(Math.max(top,topBound),bottomBound);
                return newTop;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        if(action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL){
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
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDragView = getChildAt(0);
    }
}
