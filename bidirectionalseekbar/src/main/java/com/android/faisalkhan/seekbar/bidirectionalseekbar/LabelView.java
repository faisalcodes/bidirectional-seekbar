/*
 * Copyright (c) 2021 Faisal Khan (faisalkhan100@gmail.com)
 * All Rights Reserved.
 */
package com.android.faisalkhan.seekbar.bidirectionalseekbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

@SuppressLint("ViewConstructor")
public class LabelView extends RelativeLayout {
    private final BiDirectionalSeekBar seekBar;
    private final AppCompatTextView textView;
    private final Drawable drawable;

    public LabelView(BiDirectionalSeekBar seekBar, Context context) {
        super(context);
        this.seekBar = seekBar;
        textView = new AppCompatTextView(context);
        drawable = ContextCompat.getDrawable(getContext(), R.drawable.bg_seekbar_label);
        init();
    }

    private void init() {
        initThis();
        initTV();
    }

    private void initThis() {
        setLabelBGColor();

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        params.topMargin = 10;
        setLayoutParams(params);
        setGravity(Gravity.CENTER);
    }

    private void initTV() {
        textView.setTextColor(seekBar.mLabelColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

        LayoutParams params = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(15, 10, 15, 10);
        addView(textView);
    }

    public void setText(String string) {
        textView.setText(string);
    }

    public void setLabelBGColor() {
        if (drawable != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                DrawableCompat.setTint(drawable, seekBar.mIndicatorColor);
            } else {
                drawable.mutate().setColorFilter(seekBar.mIndicatorColor, PorterDuff.Mode.SRC_IN);
            }
        }
        setBackground(drawable);
    }

    public void setLabelColor(int color) {
        textView.setTextColor(color);
        invalidate();
    }
}
