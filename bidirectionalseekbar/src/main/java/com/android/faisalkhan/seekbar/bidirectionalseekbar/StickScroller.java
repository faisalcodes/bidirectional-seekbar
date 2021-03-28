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
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.android.faisalkhan.seekbar.bidirectionalseekbar.BiDirectionalSeekBar.STYLE_LINEAR;
import static com.android.faisalkhan.seekbar.bidirectionalseekbar.HorizonScrollView.OnScrollListener;
import static com.android.faisalkhan.seekbar.bidirectionalseekbar.HorizonScrollView.OnScrollStartListener;
import static com.android.faisalkhan.seekbar.bidirectionalseekbar.HorizonScrollView.OnScrollStopListener;

@SuppressLint("ViewConstructor")
public class StickScroller extends RelativeLayout implements OnScrollListener, OnScrollStartListener, OnScrollStopListener {
    private final Paint gradientPaintL = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint gradientPaintR = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Rect gradientRectL = new Rect();
    private final Rect gradientRectR = new Rect();
    private final Paint clipPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF clipRectL = new RectF();
    private final RectF clipRectR = new RectF();
    private final RectF arcL = new RectF();
    private final RectF arcR = new RectF();
    private final BiDirectionalSeekBar seekBar;
    private final LinearLayout stickContainer;
    private RelativeLayout stickContainerSuper;
    private HorizonScrollView scrollView;
    private Indicator indicator;
    private int mSeekBarCenter;
    private int fadeLength;
    private boolean fromUser;
    private int height;

    public StickScroller(final BiDirectionalSeekBar seekBar, Context context) {
        super(context);
        this.seekBar = seekBar;
        stickContainer = new LinearLayout(context);
        init();
    }

    private void init() {
        initHeight();
        initPaint();
        initThis();
    }

    private void initPaint() {
        gradientPaintL.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        gradientPaintR.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        if (seekBar.mStyle == STYLE_LINEAR) {
            clipPaint.setXfermode(null);
        } else {
            clipPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            clipPaint.setStyle(Paint.Style.FILL);
        }
    }

    private void initThis() {
        LayoutParams params = new LayoutParams(MATCH_PARENT, height);
        params.addRule(CENTER_HORIZONTAL, TRUE);
        params.addRule(ALIGN_PARENT_BOTTOM, TRUE);
        setLayoutParams(params);

        initScrollView();
        initIndicator();
    }

    private void updateThisParams() {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = height;
        setLayoutParams(params);
    }

    private void initHeight() {
        height = seekBar.mStyle == STYLE_LINEAR ? seekBar.STICK_HEIGHT_LINEAR : seekBar.STICK_MAX_HEIGHT;
    }

    private void initScrollView() {
        scrollView = new HorizonScrollView(seekBar);

        LayoutParams params = new LayoutParams(WRAP_CONTENT, MATCH_PARENT);
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
    }

    private void initIndicator() {
        indicator = new Indicator(seekBar, getContext());
        addView(indicator);
    }

    private void initStickContainer() {
        stickContainerSuper = new RelativeLayout(getContext());
        stickContainerSuper.setLayoutParams(new ViewGroup.LayoutParams(WRAP_CONTENT, height));
        stickContainer.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams params = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        params.addRule(CENTER_IN_PARENT, TRUE);
        stickContainer.setLayoutParams(params);
        stickContainer.setGravity(Gravity.CENTER_HORIZONTAL);
        stickContainer.setClipToPadding(false);

        initSticks();

        stickContainerSuper.addView(stickContainer);
        scrollView.addView(stickContainerSuper);
    }

    private void updateStickContainerParams() {
        ViewGroup.LayoutParams params = stickContainerSuper.getLayoutParams();
        params.height = height;
        stickContainerSuper.setLayoutParams(params);
    }

    public final void initSticks() {
        stickContainer.removeAllViews();
        int maxValue = seekBar.getMaxValue() - seekBar.getMinValue();
        for (int i = 0; i <= maxValue; i++) {
            ProgressStick progressStick = new ProgressStick(seekBar, this, getContext(), (i + seekBar.getMinValue()));
            stickContainer.addView(progressStick);
        }
    }

    public final void onUpdateStyle() {
        initHeight();
        updateThisParams();
        updateStickContainerParams();
        initPaint();
        indicator.onUpdateStyle();

        int count = stickContainer.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = stickContainer.getChildAt(i);
            if (child instanceof ProgressStick) ((ProgressStick) child).onUpdateStyle();
        }

        invalidate();
        requestLayout();
    }

    public final void initPadding(int seekBarCenter) {
        mSeekBarCenter = seekBarCenter;
        stickContainer.setPadding(seekBarCenter, 0, seekBarCenter, 0);
        fadeLength = seekBarCenter * 4 / 5;
    }

    final LinearLayout getStickContainer() {
        return stickContainer;
    }

    final HorizontalScrollView getScrollView() {
        return scrollView;
    }

    public final void refreshProgress(boolean animate) {
        if (animate) {
            refreshProgressWithAnimation();
            return;
        }

        scrollView.scrollTo(seekBar.calculateScroll(), 0);
    }

    private void refreshProgressWithAnimation() {
        int scroll = seekBar.calculateScroll();
        scrollView.smoothScrollTo(scroll, 0);
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


        if (seekBar.mStyle != STYLE_LINEAR) {
            int nB = seekBar.STICK_MAX_HEIGHT - seekBar.STICK_MIN_HEIGHT;
            int arcExtra = mSeekBarCenter - fadeLength;
            int arcOffset = indicator.mWidth >> 1;

            clipRectL.set(l1, t, r1, nB);
            arcL.set(fadeLength - (arcExtra), (t - nB), mSeekBarCenter - (arcOffset), nB);

            clipRectR.set(l2, t, r2, nB);
            arcR.set(mSeekBarCenter + (arcOffset), t - (nB), l2 + arcExtra, nB);
        } else {
            clipRectL.setEmpty();
            clipRectR.setEmpty();
            arcL.setEmpty();
            arcR.setEmpty();
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        initFade();
        int count = canvas.saveLayer(0.0f, 0.0f, (float) getWidth(), (float) getHeight(), null, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
        canvas.drawRect(gradientRectL, gradientPaintL);
        canvas.drawRect(gradientRectR, gradientPaintR);

        if (seekBar.mStyle != STYLE_LINEAR) {
            canvas.drawRect(clipRectL, clipPaint);
            canvas.drawArc(arcL, 0f, 90f, true, clipPaint);

            canvas.drawRect(clipRectR, clipPaint);
            canvas.drawArc(arcR, 90f, 180f, true, clipPaint);
        }
        canvas.restoreToCount(count);
    }

    @Override
    public void onScrollChanged(int l, int t, int oldL, int oldT) {
        if (!fromUser) return;
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
        seekBar.changeProgress(stickPosition, false, fromUser);
    }

    @Override
    public void onScrollStart() {
        fromUser = true;
        seekBar.labelView.animate().scaleX(1.2f).scaleY(1.2f).setDuration(150).start();
    }

    @Override
    public void onScrollStopped() {
        refreshProgressWithAnimation();
        seekBar.labelView.animate().scaleX(1f).scaleY(1f).setDuration(150).start();
        fromUser = false;
    }
}
