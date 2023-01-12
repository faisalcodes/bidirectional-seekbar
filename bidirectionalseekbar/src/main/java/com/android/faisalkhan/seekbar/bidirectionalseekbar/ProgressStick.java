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
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import static com.android.faisalkhan.seekbar.bidirectionalseekbar.BiDirectionalSeekBar.STYLE_LINEAR;

@SuppressLint("ViewConstructor")
public class ProgressStick extends View {
    private final BiDirectionalSeekBar seekBar;
    private final StickScroller stickScroller;
    private final int progressValue;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    private final int dimenH;
    private RectF rectF;
    private int dimenV;
    private int dimenVZero;

    public ProgressStick(BiDirectionalSeekBar seekBar, StickScroller stickScroller, Context context, int progressValue) {
        super(context);
        this.seekBar = seekBar;
        this.stickScroller = stickScroller;
        this.progressValue = progressValue;
        this.dimenH = (int) Math.max(seekBar.STICK_WIDTH, seekBar.mStickGap);
        this.dimenV = seekBar.mStyle == STYLE_LINEAR ? seekBar.STICK_HEIGHT_LINEAR - 30 : seekBar.STICK_MAX_HEIGHT;
        this.dimenVZero = seekBar.mStyle == STYLE_LINEAR ? seekBar.STICK_HEIGHT_LINEAR - 20 : seekBar.STICK_MAX_HEIGHT;

        initStick();
    }

    private void initStick() {
        initThis();
        initPaint();
    }

    private void initThis() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dimenH, progressValue == 0 ? dimenVZero : dimenV);
        params.gravity = Gravity.CENTER_VERTICAL;
        setLayoutParams(params);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.changeProgress(progressValue, false, true);
                stickScroller.refreshProgress(true);
            }
        });
    }

    private void updateParams() {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = progressValue == 0 ? dimenVZero : dimenV;
        setLayoutParams(params);
    }

    private void initPaint() {
        paint.setStyle(Paint.Style.FILL);
        int centerH = dimenH / 2;
        if (progressValue == 0) rectF = new RectF(centerH - 2f, 0, centerH + 2f, dimenVZero);
        else rectF = new RectF(centerH - 1.5f, 0, centerH + 1.5f, dimenV);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(progressValue == 0 ? seekBar.zeroStickColor() : seekBar.stickColor());
        canvas.drawRoundRect(rectF, 10, 10, paint);
        super.onDraw(canvas);
    }

    public final void onUpdateStyle() {
        this.dimenV = seekBar.mStyle == STYLE_LINEAR ? seekBar.STICK_HEIGHT_LINEAR - 30 : seekBar.STICK_MAX_HEIGHT;
        this.dimenVZero = seekBar.mStyle == STYLE_LINEAR ? seekBar.STICK_HEIGHT_LINEAR - 20 : seekBar.STICK_MAX_HEIGHT;
        updateParams();
        initPaint();
        invalidate();
    }
}
