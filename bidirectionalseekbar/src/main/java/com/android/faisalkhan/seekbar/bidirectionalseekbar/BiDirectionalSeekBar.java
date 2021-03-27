/*
 * Copyright (c) 2021 Faisal Khan (faisalkhan100@gmail.com)
 * All Rights Reserved.
 */

package com.android.faisalkhan.seekbar.bidirectionalseekbar;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatTextView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


@SuppressWarnings("unused")
public class BiDirectionalSeekBar extends LinearLayout {
    public static final int TITLE_TINY = 0;
    public static final int TITLE_NORMAL = 1;
    private static final String TAG = "myLogs";
    final int STICK_WIDTH = 20;
    final int STICK_MAX_HEIGHT = 60;
    int mIndicatorColor;
    int mMinVal;
    int mMaxVal;
    LabelView labelView;
    int mProgress;
    private int mStickColor;
    private int mZeroStickColor;
    private int mTitleColor;
    int mLabelColor;
    private int parentWidth;
    private boolean percentageSign;
    private String mTitle;
    private int mTitleSize;
    private LinearLayout innerContainer;
    private OnProgressChangeListener mListener;
    private AppCompatTextView titleView;
    private StickScroller stickScroller;
    private LinearLayout stickContainer;
    private HorizontalScrollView scrollView;
    private boolean finalDimenSet = false;

    public BiDirectionalSeekBar(@NonNull Context context) {
        this(context, null);
    }

    public BiDirectionalSeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.recyclerViewStyle);
    }

    public BiDirectionalSeekBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Resources.Theme theme = getContext().getTheme();

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BiDirectionalSeekBar, defStyleAttr, 0);
        mTitle = a.getString(R.styleable.BiDirectionalSeekBar_title);
        mTitleSize = a.getInt(R.styleable.BiDirectionalSeekBar_titleSize, TITLE_NORMAL);
        mTitleColor = a.getColor(R.styleable.BiDirectionalSeekBar_titleColor, 0xFF000000);


        mMinVal = a.getInt(R.styleable.BiDirectionalSeekBar_minValue, 0);
        mMaxVal = a.getInt(R.styleable.BiDirectionalSeekBar_maxValue, 100);

        mProgress = a.getInt(R.styleable.BiDirectionalSeekBar_progress, 0);
        percentageSign = a.getBoolean(R.styleable.BiDirectionalSeekBar_percentageSign, false);


        TypedValue indicatorTypedValue = new TypedValue();
        boolean resolved = theme.resolveAttribute(R.attr.colorPrimary, indicatorTypedValue, true);
        mIndicatorColor = a.getColor(R.styleable.BiDirectionalSeekBar_indicatorColor,
                resolved ? indicatorTypedValue.data : 0xFF6200EE);

        mStickColor = a.getColor(R.styleable.BiDirectionalSeekBar_stickColor, 0xFF555555);
        mZeroStickColor = a.getColor(R.styleable.BiDirectionalSeekBar_zeroStickColor, 0xFF000000);
        mLabelColor = a.getColor(R.styleable.BiDirectionalSeekBar_labelColor, 0xFFFFFFFF);

        a.recycle();

        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (finalDimenSet) return;
        parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        initFinalDimen();
    }

    private void init(Context context) {
        initThis();
        initInnerContainer(context);
        initTitleView(context);
    }

    private void initThis() {
        setOrientation(VERTICAL);
        setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
    }

    private void initInnerContainer(Context context) {
        innerContainer = new LinearLayout(context);
        innerContainer.setOrientation(VERTICAL);
        innerContainer.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));

        innerContainer.setPadding(0, 15, 0, 15);

        initLabel(context);
        initStickScroller(context);

        addView(innerContainer);
    }

    private void initFinalDimen() {
        if (finalDimenSet) return;
        finalDimenSet = true;

        stickScroller.initPadding(parentWidth >> 1);
        scrollView.scrollTo(calculateScroll(), 0);
    }

    private void initStickScroller(Context context) {
        stickScroller = new StickScroller(this, context);
        stickContainer = stickScroller.getStickContainer();
        scrollView = stickScroller.getScrollView();
        innerContainer.addView(stickScroller);
    }

    private void initLabel(Context context) {
        labelView = new LabelView(this, context);
        changeProgress(mProgress);
        innerContainer.addView(labelView);
    }

    private void initTitleView(Context context) {
        if (mTitle == null) return;
        titleView = new AppCompatTextView(context);
        initTitleText();
        initTitleColor();
        initTitleSize();
        LayoutParams params = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        titleView.setLayoutParams(params);
        titleView.setGravity(Gravity.CENTER);
        titleView.setPadding(15, 0, 15, 0);

        addView(titleView);
    }

    private void initTitleText() {
        titleView.setText(mTitle);
    }

    private void initTitleColor() {
        if (titleView != null) titleView.setTextColor(mTitleColor);
    }

    private void initTitleSize() {
        if (titleView != null) titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTitleSize == TITLE_TINY ? 12 : 16);
    }

    void changeProgress(int progress) {
        progress = Math.max(progress, mMinVal);
        progress = Math.min(progress, mMaxVal);
        mProgress = progress;

        labelView.setText(percentageSign ? mProgress + "%" : String.valueOf(mProgress));
        if (mListener != null) mListener.onProgress(mProgress);
    }

    int calculateScroll() {
        int progress = mProgress - mMinVal;
        View stick = stickContainer.getChildAt(progress);
        if (stick instanceof ProgressStick) return (progress * STICK_WIDTH) + stick.getWidth() / 2;
        return 0;
    }

    int stickColor() {
        return mStickColor;
    }

    int zeroStickColor() {
        return mZeroStickColor;
    }

    // Property methods start
    public void setOnProgressChangeListener(@NonNull OnProgressChangeListener listener) {
        this.mListener = listener;
    }

    /**
     * Set color to the indicator and label background.
     *
     * @param color The color to be set.
     */
    public void setIndicatorColor(int color) {
        mIndicatorColor = color;
        stickScroller.getIndicator().setColor();
        labelView.setLabelBGColor();
    }

    /**
     * Set color to the progress sticks.
     *
     * @param color The color to be set.
     */
    public void setStickColor(int color) {
        mStickColor = color;
        stickScroller.initSticksColor();
    }

    /**
     * Set color to the zero (0) progress stick.
     * This is ignored if (minValue > 0) or (maxValue < 0).
     *
     * @param color The color to be set.
     */
    public void setZeroStickColor(int color) {
        mZeroStickColor = color;

        if (getMinValue() > 0) return;

        View zeroChild = stickContainer.getChildAt(Math.abs(getMinValue()));
        if (zeroChild instanceof ProgressStick) ((ProgressStick) zeroChild).initStickColor();
    }

    /**
     * Set color to the label text.
     * This is useful when background color and text color of the label are mismatching.
     *
     * @param color The color to be set.
     */
    public void setLabelColor(int color) {
        mLabelColor = color;
        labelView.setLabelColor(color);
    }

    /**
     * Set color to the title text.
     *
     * @param color The color to be set.
     */
    public void setTitleColor(int color) {
        mTitleColor = color;
        initTitleColor();
    }

    /**
     * @return The maximum value.
     */
    public int getMaxValue() {
        return mMaxVal;
    }

    /**
     * DEFAULT - 100
     *
     * @param maxValue The maximum value. Must be greater than minValue.
     */
    public void setMaxValue(int maxValue) {
        mMaxVal = maxValue;
        stickScroller.initSticks();
        changeProgress(mProgress);
        stickScroller.onScrollStopped();
    }

    /**
     * @return The minimum value.
     */
    public int getMinValue() {
        return mMinVal;
    }

    /**
     * DEFAULT - 0
     *
     * @param minValue The minimum value. Must be smaller than maxValue.
     */
    public void setMinValue(int minValue) {
        mMinVal = minValue;
        stickScroller.initSticks();
        changeProgress(mProgress);
        stickScroller.onScrollStopped();
    }

    /**
     * @return Returns the current progress.
     */
    public int getProgress() {
        return mProgress;
    }


    /**
     * Set progress to the seekBar.
     * if (progress < minValue) or (progress > maxValue),
     * then the progress becomes minValue or maxValue respectively.
     * <p>
     * DEFAULT - 0
     *
     * @param progress The progress value.
     */
    public void setProgress(int progress) {
        changeProgress(progress);
        stickScroller.onScrollStopped();
    }

    /**
     * Set whether the percentage sign (%) should be shown with progress label or not.
     * <p>
     * DEFAULT - False
     *
     * @param percentageSign true if (%) should be shown false otherwise.
     */
    public void setPercentageSign(boolean percentageSign) {
        this.percentageSign = percentageSign;
        changeProgress(mProgress);
    }

    @NonNull
    public String getTitle() {
        return mTitle;
    }

    /**
     * Set title to the seekBar.
     *
     * @param titleRes Title resource.
     */
    public void setTitle(@StringRes int titleRes) {
        setTitle(getContext().getString(titleRes));
    }

    /**
     * Set title to the seekBar.
     *
     * @param title The title text.
     */
    public void setTitle(@NonNull String title) {
        this.mTitle = title;
        if (titleView == null) initTitleView(getContext());
        else initTitleText();
    }

    /**
     * Set the size of which the title should be.
     * If {@link #TITLE_TINY} is set,
     * then the title size will be tiny. Otherwise it will be of normal size.
     * <p>
     * DEFAULT - {@link #TITLE_NORMAL}
     *
     * @param titleSize One of {@link #TITLE_TINY} or {@link #TITLE_NORMAL}
     */
    public void setTitleSize(int titleSize) {
        mTitleSize = titleSize;
        initTitleSize();
    }

    // Property methods ends

    public interface OnProgressChangeListener {
        void onProgress(int progress);
    }
}
