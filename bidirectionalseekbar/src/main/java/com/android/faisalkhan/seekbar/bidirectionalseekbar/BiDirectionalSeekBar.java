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
import android.widget.RelativeLayout;

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
    public static final int STYLE_CURVE = 0;
    public static final int STYLE_LINEAR = 1;
    private static final String TAG = "myLogs";
    final int STICK_WIDTH = 20;
    final int STICK_HEIGHT_LINEAR = 60;
    final int STICK_MAX_HEIGHT = 80;
    final int STICK_MIN_HEIGHT = 30;
    float mStickGap;
    int mStyle;
    int mIndicatorColor;
    int mMinVal;
    int mMaxVal;
    int mProgress;
    int mLabelColor;
    LabelView labelView;
    OnSeekBarChangeListener mSeekBarChangeListener;
    private int mStickColor;
    private int mZeroStickColor;
    private int mTitleColor;
    private int parentWidth;
    private boolean percentageSign;
    private String mTitle;
    private int mTitleSize;
    private RelativeLayout innerContainer;
    private OnProgressChangeListener mProgressChangeListener;
    private AppCompatTextView titleView;
    private StickScroller mStickScroller;
    private LinearLayout stickContainer;
    private HorizontalScrollView scrollView;
    private boolean finalDimenSet = false;
    private boolean layoutReady = false;

    public BiDirectionalSeekBar(@NonNull Context context) {
        this(context, null);
    }

    public BiDirectionalSeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BiDirectionalSeekBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Resources.Theme theme = getContext().getTheme();

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BiDirectionalSeekBar, defStyleAttr, 0);
        mTitle = a.getString(R.styleable.BiDirectionalSeekBar_BDS_seekBarTitle);
        mTitleSize = a.getInt(R.styleable.BiDirectionalSeekBar_BDS_seekBarTitleSize, TITLE_TINY);

        TypedArray a2 = theme.obtainStyledAttributes(new int[]{android.R.attr.textColorPrimary,
                android.R.attr.textColorSecondaryInverse});
        int priTextColor = a2.getColor(0, 0xFF000000);
        int secTextColor = a2.getColor(0, 0xFF222222);
        mTitleColor = a.getColor(R.styleable.BiDirectionalSeekBar_BDS_seekBarTitleColor, priTextColor);
        a2.recycle();


        mMinVal = a.getInt(R.styleable.BiDirectionalSeekBar_BDS_minValue, 0);
        mMaxVal = a.getInt(R.styleable.BiDirectionalSeekBar_BDS_maxValue, 100);

        mProgress = a.getInt(R.styleable.BiDirectionalSeekBar_BDS_progress, 0);
        percentageSign = a.getBoolean(R.styleable.BiDirectionalSeekBar_BDS_percentageSign, false);


        TypedValue indicatorTypedValue = new TypedValue();
        boolean resolved = theme.resolveAttribute(R.attr.colorPrimary, indicatorTypedValue, true);
        mIndicatorColor = a.getColor(R.styleable.BiDirectionalSeekBar_BDS_indicatorColor,
                resolved ? indicatorTypedValue.data : 0xFF6200EE);

        mStickColor = a.getColor(R.styleable.BiDirectionalSeekBar_BDS_stickColor, secTextColor);
        mZeroStickColor = a.getColor(R.styleable.BiDirectionalSeekBar_BDS_zeroStickColor, priTextColor);
        mLabelColor = a.getColor(R.styleable.BiDirectionalSeekBar_BDS_labelColor, priTextColor);

        mStickGap = a.getDimension(R.styleable.BiDirectionalSeekBar_BDS_stickGap, STICK_WIDTH);
        mStyle = a.getInt(R.styleable.BiDirectionalSeekBar_BDS_seekBar_Style, STYLE_CURVE);

        a.recycle();

        init(context);
    }

    private void init(Context context) {
        initThis();
        initInnerContainer(context);
        initTitleView(context);
    }

    private void initThis() {
        setOrientation(VERTICAL);
        setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        setPadding(0, 0, 0, 5);
    }

    private void initInnerContainer(Context context) {
        innerContainer = new RelativeLayout(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                MATCH_PARENT, mStyle == STYLE_LINEAR
                ? STICK_HEIGHT_LINEAR * 2 : 130);
        innerContainer.setLayoutParams(params);
        initStickScroller();
        initLabel();
        addView(innerContainer);
    }

    private void updateInnerParams() {
        ViewGroup.LayoutParams params = innerContainer.getLayoutParams();
        params.height = mStyle == STYLE_LINEAR ? STICK_HEIGHT_LINEAR * 2 : 130;
        innerContainer.setLayoutParams(params);
    }

    private void initFinalDimen() {
        if (finalDimenSet) return;
        finalDimenSet = true;

        mStickScroller.initPadding(parentWidth >> 1);
    }

    private void initStickScroller() {
        mStickScroller = new StickScroller(this, getContext());
        stickContainer = mStickScroller.getStickContainer();
        scrollView = mStickScroller.getScrollView();
        innerContainer.addView(mStickScroller);
    }

    private void initLabel() {
        labelView = new LabelView(this, getContext());
        labelView.setText(percentageSign ? mProgress + "%" : String.valueOf(mProgress));
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
        params.topMargin = 10;
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
        if (titleView != null) titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTitleSize == TITLE_NORMAL ? 16 : 12);
    }

    void changeProgress(int progress, boolean internal, boolean fromUser) {
        progress = Math.max(progress, mMinVal);
        progress = Math.min(progress, mMaxVal);
        mProgress = progress;
        labelView.setText(percentageSign ? progress + "%" : String.valueOf(progress));
        if (mProgressChangeListener != null && !internal) mProgressChangeListener.onProgressChanged(this, progress, fromUser);
        if (mSeekBarChangeListener != null && !internal) mSeekBarChangeListener.onProgressChanged(this, progress, fromUser);
    }

    int calculateScroll() {
        int progress = mProgress - mMinVal;
        View stick = stickContainer.getChildAt(progress);
        if (stick instanceof ProgressStick) {
            int stickWidth = stick.getWidth();
            return (progress * stickWidth) + (stickWidth >> 1);
        }
        return 0;
    }

    int stickColor() {
        return mStickColor;
    }

    int zeroStickColor() {
        return mZeroStickColor;
    }

    // Property methods start
    /**
     * Set color to the indicator and label background.
     *
     * @param color The color to be set.
     */
    public void setIndicatorColor(int color) {
        mIndicatorColor = color;
        labelView.setLabelBGColor();
    }

    /**
     * Set color to the progress sticks.
     *
     * @param color The color to be set.
     */
    public void setStickColor(int color) {
        mStickColor = color;
    }

    /**
     * Set color to the zero (0) progress stick.
     * This is ignored if (minValue > 0) or (maxValue < 0).
     *
     * @param color The color to be set.
     */
    public void setZeroStickColor(int color) {
        mZeroStickColor = color;
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
    public void setSeekBarTitleColor(int color) {
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
        mStickScroller.initSticks();
        changeProgress(mProgress, true, false);
        mStickScroller.refreshProgress(false);
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
        mStickScroller.initSticks();
        changeProgress(mProgress, true, false);
        mStickScroller.refreshProgress(false);
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
        setProgress(progress, true);
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
    public void setProgress(int progress, boolean animate) {
        changeProgress(progress, false, false);
        mStickScroller.refreshProgress(animate && layoutReady);
    }

    /**
     * Set whether the percentage sign (%) should be shown with progress label.
     * <p>
     * DEFAULT - False
     *
     * @param percentageSign True if (%) should be shown false otherwise.
     */
    public void setPercentageSign(boolean percentageSign) {
        this.percentageSign = percentageSign;
        changeProgress(mProgress, true, false);
    }

    @NonNull
    public String getSeekBarTitle() {
        return mTitle;
    }

    /**
     * Set title to the seekBar.
     *
     * @param titleRes Title resource.
     */
    public void setSeekBarTitle(@StringRes int titleRes) {
        setSeekBarTitle(getContext().getString(titleRes));
    }

    /**
     * Set title to the seekBar.
     *
     * @param title The title text.
     */
    public void setSeekBarTitle(@NonNull String title) {
        this.mTitle = title;
        if (titleView == null) initTitleView(getContext());
        else initTitleText();
    }

    /**
     * Set the size of which the title should be.
     * If {@link #TITLE_TINY} is set,
     * then the title size will be tiny. Otherwise it will be of normal size.
     * <p>
     * DEFAULT - {@link #TITLE_TINY}
     *
     * @param titleSize One of {@link #TITLE_TINY} and {@link #TITLE_NORMAL}
     */
    public void setSeekBarTitleSize(int titleSize) {
        mTitleSize = titleSize;
        initTitleSize();
    }

    /**
     * Set gap between the sticks. This is useful to make seekBar look good when total progress sticks are lesser in count.
     *
     * @param gap Gap value in px.
     */
    public void setStickGap(int gap) {
        mStickGap = gap;
        mStickScroller.initSticks();
    }

    /**
     * @param style One of {@link #STYLE_CURVE} and {@link #STYLE_LINEAR}
     */
    public void setSeekBarStyle(int style) {
        mStyle = style;
        updateInnerParams();
        mStickScroller.onUpdateStyle();
    }

    public void setOnProgressChangeListener(@NonNull OnProgressChangeListener listener) {
        this.mProgressChangeListener = listener;
    }

    public void setOnSeekBarChangeListener(@NonNull OnSeekBarChangeListener listener) {
        this.mSeekBarChangeListener = listener;
    }

    // Property methods ends

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (finalDimenSet) return;
        parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        initFinalDimen();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (!layoutReady) {
            scrollView.scrollTo(calculateScroll(), 0);
        }
        layoutReady = true;
    }

    public interface OnProgressChangeListener {
        void onProgressChanged(BiDirectionalSeekBar seekBar, int progress, boolean fromUser);
    }

    public interface OnSeekBarChangeListener {
        /**
         * Notification that the user has started a touch gesture. Clients may want to use this
         * to disable advancing the seekBar.
         *
         * @param seekBar The SeekBar in which the touch gesture began
         */
        void onStartTrackingTouch(BiDirectionalSeekBar seekBar);

        /**
         * Listener for progress change. Clients can use the fromUser parameter
         * to distinguish user-initiated changes from those that occurred programmatically.
         *
         * @param seekBar  The SeekBar whose progress has changed
         * @param progress The current progress value. This will be in the range min..max where min
         *                 and max were set by {@link #setMinValue(int)} and
         *                 {@link #setMaxValue(int)}, respectively. (The default values for
         *                 min is 0 and max is 100.)
         * @param fromUser True if the progress change was initiated by the user.
         */
        void onProgressChanged(BiDirectionalSeekBar seekBar, int progress, boolean fromUser);

        /**
         * Notification that the user has finished a touch gesture. Clients may want to use this
         * to re-enable advancing the seekBar.
         *
         * @param seekBar The SeekBar in which the touch gesture began
         */
        void onStopTrackingTouch(BiDirectionalSeekBar seekBar);
    }
}
