/*
 * Copyright (c) 2021 Faisal Khan
 * All Rights Reserved.
 */
package com.android.faisalkhan.seekbar.bidirectionalseekbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import static android.widget.RelativeLayout.CENTER_IN_PARENT;
import static android.widget.RelativeLayout.TRUE;
import static com.android.faisalkhan.seekbar.bidirectionalseekbar.BiDirectionalSeekBar.STYLE_LINEAR;

@SuppressLint("ViewConstructor")
public class Indicator extends View {
    private final BiDirectionalSeekBar seekBar;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    public int mWidth;
    private final int dimenH;
    private RectF rectF;
    private int dimenV;

    public Indicator(BiDirectionalSeekBar seekBar, Context context) {
        super(context);
        this.seekBar = seekBar;
        this.dimenH = seekBar.STICK_WIDTH;
        this.dimenV = seekBar.mStyle == STYLE_LINEAR ? seekBar.STICK_HEIGHT_LINEAR : seekBar.STICK_MAX_HEIGHT;
        init();
    }

    private void init() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dimenH, dimenV);
        params.addRule(CENTER_IN_PARENT, TRUE);
        setLayoutParams(params);
        initPaint();
    }

    private void updateParams() {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = dimenV;
        setLayoutParams(params);
    }

    private void initPaint() {
        initRect();
        paint.setStyle(Paint.Style.FILL);
    }

    private void initRect() {
        int centerH = dimenH / 2;
        int l = centerH - 3;
        int r = l + 6;
        mWidth = r;
        rectF = new RectF(l, 0, r, dimenV);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(seekBar.mIndicatorColor);
        canvas.drawRoundRect(rectF, 10, 10, paint);
        super.onDraw(canvas);
    }

    public final void onUpdateStyle() {
        this.dimenV = seekBar.mStyle == STYLE_LINEAR ? seekBar.STICK_HEIGHT_LINEAR : seekBar.STICK_MAX_HEIGHT;
        initRect();
        updateParams();
        invalidate();
    }
}
