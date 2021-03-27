/*
 * Copyright (c) 2021 Faisal Khan (faisalkhan100@gmail.com)
 * All Rights Reserved.
 */
package com.android.faisalkhan.seekbar.bidirectionalseekbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.faisalkhan.seekbar.bidirectionalseekbar.utils.HorizonScrollView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

@SuppressLint("ViewConstructor")
public class StickScroller extends RelativeLayout implements HorizonScrollView.OnScrollListener,
        HorizonScrollView.OnScrollStartListener, HorizonScrollView.OnScrollStopListener {
    private static final String TAG = "myLogs";
    private final BiDirectionalSeekBar seekBar;
    private final LinearLayout stickContainer;
    private HorizonScrollView scrollView;
    private Indicator indicator;

    private Paint gradientPaintL, gradientPaintR;
    private Rect gradientRectL, gradientRectR;
    private int fadeLength;

    public StickScroller(BiDirectionalSeekBar seekBar, Context context) {
        super(context);
        this.seekBar = seekBar;
        stickContainer = new LinearLayout(context);
        init();
    }

    private void init() {
        initPaint();
        initThis();
    }

    private void initPaint() {
        gradientPaintL = new Paint(Paint.ANTI_ALIAS_FLAG);
        gradientPaintR = new Paint(Paint.ANTI_ALIAS_FLAG);

        gradientPaintL.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        gradientPaintR.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        gradientRectL = new Rect();
        gradientRectR = new Rect();
    }

    private void initThis() {
        setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, seekBar.STICK_MAX_HEIGHT));
        initScrollView();
        initIndicator();
    }

    private void initScrollView() {
        scrollView = new HorizonScrollView(getContext());

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        params.addRule(CENTER_HORIZONTAL, TRUE);
        params.addRule(ALIGN_PARENT_BOTTOM, TRUE);
        scrollView.setLayoutParams(params);
        scrollView.setOverScrollMode(OVER_SCROLL_NEVER);
        scrollView.setHorizontalScrollBarEnabled(false);

        scrollView.setOnScrollListener(this);
        scrollView.setOnScrollStopListener(this);
        scrollView.setOnScrollStartListener(this);

        initStickContainer();

        addView(scrollView);

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.setScrollX(seekBar.calculateScroll());
            }
        });
    }

    private void initIndicator() {
        indicator = new Indicator(seekBar, getContext());
        addView(indicator);
    }

    private void initStickContainer() {
        RelativeLayout stickContainerSuper = new RelativeLayout(getContext());
        stickContainerSuper.setLayoutParams(new ViewGroup.LayoutParams(WRAP_CONTENT, seekBar.STICK_MAX_HEIGHT));

        stickContainer.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        params.addRule(CENTER_IN_PARENT, TRUE);
        stickContainer.setLayoutParams(params);
        stickContainer.setGravity(Gravity.CENTER_HORIZONTAL);
        stickContainer.setClipToPadding(false);

        initSticks();

        stickContainerSuper.addView(stickContainer);
        scrollView.addView(stickContainerSuper);
    }

    public void initSticks() {
        stickContainer.removeAllViews();
        int maxValue = seekBar.getMaxValue() - seekBar.getMinValue();
        for (int i = 0; i <= maxValue; i++) {
            ProgressStick progressStick = new ProgressStick(seekBar, this, getContext(), (i + seekBar.getMinValue()));
            stickContainer.addView(progressStick);
        }
    }

    public void initSticksColor() {
        int count = stickContainer.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = stickContainer.getChildAt(i);
            if (child instanceof ProgressStick) {
                ((ProgressStick) child).initStickColor();
            }
        }
    }

    public void initPadding(int seekBarCenter) {
        stickContainer.setPadding(seekBarCenter, 0, seekBarCenter, 0);
        fadeLength = seekBarCenter * 4 / 5;
    }

    LinearLayout getStickContainer() {
        return stickContainer;
    }

    Indicator getIndicator() {
        return indicator;
    }

    HorizontalScrollView getScrollView() {
        return scrollView;
    }

    private void initFade() {
        final int[] FADE_COLORS = new int[]{0x00000000, 0xFF000000};
        final int[] FADE_COLORS_REVERSE = new int[]{0xFF000000, 0x00000000};

        int actualWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int size = Math.min(fadeLength, actualWidth);

        int l1 = getPaddingLeft();
        int t = getPaddingTop();
        int r1 = l1 + size;
        int b = getHeight() - getPaddingBottom();

        gradientRectL.set(l1, t, r1, b);
        gradientPaintL.setShader(new LinearGradient(l1, t, r1, t, FADE_COLORS, null, Shader.TileMode.CLAMP));


        int l2 = getPaddingLeft() + actualWidth - size;
        int r2 = l2 + size;

        gradientRectR.set(l2, t, r2, b);
        gradientPaintR.setShader(new LinearGradient(l2, t, r2, t, FADE_COLORS_REVERSE, null, Shader.TileMode.CLAMP));
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        initFade();
        int count = canvas.saveLayer(0.0f, 0.0f, (float) getWidth(), (float) getHeight(), null, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
        canvas.drawRect(gradientRectL, gradientPaintL);
        canvas.drawRect(gradientRectR, gradientPaintR);
        canvas.restoreToCount(count);
    }

    @Override
    public void onScrollChanged(int l, int t, int oldL, int oldT) {
        int maxVal = seekBar.mMaxVal;
        int minVal = seekBar.mMinVal;
        int progress = seekBar.mProgress;

        int layoutWidth = stickContainer.getWidth() - (stickContainer.getPaddingLeft() + stickContainer.getPaddingRight());
        int stickWidth = layoutWidth / stickContainer.getChildCount();

        int stickPosition = l / stickWidth;

        if (stickPosition > maxVal + Math.abs(minVal)) stickPosition = maxVal + Math.abs(minVal);

        stickPosition += minVal;
        if (stickPosition < minVal) stickPosition = minVal;

        if (stickPosition == progress) return;
        seekBar.changeProgress(stickPosition);
    }

    @Override
    public void onScrollStart() {
        seekBar.labelView.animate().scaleX(1.3f).scaleY(1.3f).setDuration(150).start();
    }

    @Override
    public void onScrollStopped() {
        int scroll = seekBar.calculateScroll();
        scrollView.smoothScrollTo(scroll, 0);
        seekBar.labelView.animate().scaleX(1f).scaleY(1f).setDuration(150).start();
    }
}
