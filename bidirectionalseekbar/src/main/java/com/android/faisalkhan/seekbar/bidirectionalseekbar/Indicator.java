/*
 * Copyright (c) 2021 Faisal Khan (faisalkhan100@gmail.com)
 * All Rights Reserved.
 */
package com.android.faisalkhan.seekbar.bidirectionalseekbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.widget.RelativeLayout;

import static android.widget.RelativeLayout.CENTER_IN_PARENT;
import static android.widget.RelativeLayout.TRUE;

@SuppressLint("ViewConstructor")
public class Indicator extends View {
    private final BiDirectionalSeekBar seekBar;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    private final RectF rectF;
    private final int dimenH;
    private final int dimenV;

    public Indicator(BiDirectionalSeekBar seekBar, Context context) {
        super(context);
        this.seekBar = seekBar;
        this.dimenH = seekBar.STICK_WIDTH;
        this.dimenV = seekBar.STICK_MAX_HEIGHT;

        int centerH = dimenH / 2;
        rectF = new RectF(centerH - 3, 0, centerH + 3, dimenV);
        init();
    }

    private void init() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dimenH, dimenV);
        params.addRule(CENTER_IN_PARENT, TRUE);
        setLayoutParams(params);
        initPaint();
    }

    private void initPaint() {
        paint.setStyle(Paint.Style.FILL);
        setColor();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(seekBar.mIndicatorColor);
        canvas.drawRoundRect(rectF, 10, 10, paint);
        super.onDraw(canvas);
    }

    public void setColor() {
        paint.setColor(seekBar.mIndicatorColor);
        invalidate();
    }
}
