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
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

@SuppressLint("ViewConstructor")
public class ProgressStick extends View {
    private final BiDirectionalSeekBar seekBar;
    private final StickScroller stickScroller;
    private final int progressValue;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    private final RectF rectF;
    private final int dimenH;
    private final int dimenV;
    private final int dimenVZero;

    public ProgressStick(BiDirectionalSeekBar seekBar, StickScroller stickScroller, Context context, int progressValue) {
        super(context);
        this.seekBar = seekBar;
        this.stickScroller = stickScroller;
        this.progressValue = progressValue;
        this.dimenH = seekBar.STICK_WIDTH;
        this.dimenV = seekBar.STICK_MAX_HEIGHT - 30;
        this.dimenVZero = seekBar.STICK_MAX_HEIGHT - 20;

        int centerH = dimenH / 2;
        if (progressValue == 0) {
            rectF = new RectF(centerH - 2f, 0, centerH + 2f, dimenVZero);
        } else {
            rectF = new RectF(centerH - 1.5f, 0, centerH + 1.5f, dimenV);
        }

        initStick();
    }

    private void initStick() {
        initThis();
        initPaint();
        initPath();
    }

    private void initThis() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dimenH, progressValue == 0 ? dimenVZero : dimenV);
        params.gravity = Gravity.CENTER_VERTICAL;
        setLayoutParams(params);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.changeProgress(progressValue);
                stickScroller.onScrollStopped();
            }
        });
    }

    private void initPaint() {
        initStickColor();
        paint.setStyle(Paint.Style.FILL);
    }

    void initStickColor() {
        paint.setColor(progressValue == 0 ? seekBar.zeroStickColor() : seekBar.stickColor());
        invalidate();
    }

    private void initPath() {
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRoundRect(rectF, 10, 10, paint);
        super.onDraw(canvas);
    }
}
