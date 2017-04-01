package com.inthecheesefactory.lab.designlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Arrays;

/**
 * Created by WU on 2017/3/27.
 */

public class PathView extends View {

    protected Paint mPaint;
    //折现的颜色
    protected int mLineColor = Color.parseColor("#76f112");

    //自身的大小
    protected int mWidth, mHeight;

    //心电图折现
    protected Path mPath;

    float[] lux = new float[100];
    float lastValue;

    public PathView(Context context) {
        this(context, null);
    }

    public PathView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPath = new Path();
        setWillNotDraw(false);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.v("draw", "here");
        super.onDraw(canvas);
        mPath.reset();
        mPath.moveTo(0, mHeight - 5 * lux[0] - 50);

        for (int i = 1; i < lux.length; i++) {
            mPath.lineTo(11 * i, mHeight - 5 * lux[i] - 50);
            Log.i("输出", "data: " + lux[i]);
        }
        //设置画笔style
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mLineColor);
        mPaint.setStrokeWidth(5);
        canvas.drawPath(mPath, mPaint);

        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(50);
        canvas.drawText("Value: " + lastValue, 40, mHeight / 2 - 100, mPaint);
    }

    public void setData(FloatQueue lux) {
        this.lux = lux.getQueue();
        Log.v("luxinpv", Arrays.toString(lux.getQueue()));
        lastValue = lux.dequeue();
        Log.v("lastvalue", String.valueOf(lastValue));
    }
}
