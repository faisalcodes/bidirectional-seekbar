/*
 * Copyright (c) 2021 Faisal Khan (faisalkhan100@gmail.com)
 * All Rights Reserved.
 */
package com.android.faisalkhan.seekbar.bidirectionalseekbar;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

import androidx.annotation.NonNull;

@SuppressLint("ViewConstructor")
public class HorizonScrollView extends HorizontalScrollView {
    @NonNull
    private final BiDirectionalSeekBar seekBar;
    private boolean scrollStart = false;
    private final Runnable runnable;
    private OnScrollStartListener startListener;
    private OnScrollListener scrollListener;
    private OnScrollStopListener stopListener;
    private int prevPosition;

    public HorizonScrollView(@NonNull BiDirectionalSeekBar seekBar) {
        super(seekBar.getContext());
        this.seekBar = seekBar;
        requestDisallowInterceptTouchEvent(true);

        runnable = new Runnable() {
            @Override
            public void run() {
                if (prevPosition - getScrollX() == 0) {
                    if (stopListener != null) stopListener.onScrollStopped();
                } else {
                    prevPosition = getScrollX();
                    postDelayed(runnable, 100);
                }
            }
        };
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldL, int oldT) {
        if (scrollListener != null) scrollListener.onScrollChanged(l, t, oldL, oldT);
        if (scrollStart) {
            if (startListener != null) startListener.onScrollStart();
            scrollStart = false;
        }

        super.onScrollChanged(l, t, oldL, oldT);
    }

    public void setOnScrollStartListener(@NonNull OnScrollStartListener startListener) {
        this.startListener = startListener;
    }

    public void setOnScrollListener(@NonNull OnScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    public void setOnScrollStopListener(@NonNull OnScrollStopListener stopListener) {
        this.stopListener = stopListener;
    }

    public void startScrollerTask() {
        prevPosition = getScrollX();
        postDelayed(runnable, 100);
    }


    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            scrollStart = true;
            if (seekBar.mSeekBarChangeListener != null) seekBar.mSeekBarChangeListener.onStartTrackingTouch(seekBar);
        }
        if (e.getAction() == MotionEvent.ACTION_UP) {
            if (seekBar.mSeekBarChangeListener != null) seekBar.mSeekBarChangeListener.onStopTrackingTouch(seekBar);
            startScrollerTask();
        }
        return super.dispatchTouchEvent(e);
    }

    public interface OnScrollStartListener {
        void onScrollStart();
    }

    public interface OnScrollListener {
        void onScrollChanged(int l, int t, int oldL, int oldT);
    }

    public interface OnScrollStopListener {
        void onScrollStopped();
    }
}
