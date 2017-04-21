package com.inthecheesefactory.lab.designlibrary.reference;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.hardware.Sensor;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by WU on 2017/3/27.
 */

public class PathView extends View {

    protected Paint mPaint;
    //折现的颜色
    protected int[] mLineColor = {Color.parseColor("#76f112"),
            Color.parseColor("#990000"), Color.parseColor("#CCCC00")};//x for green y for red z for yellow

    //自身的大小
    protected int mWidth, mHeight;

    //心电图折现
    protected Path mPath;

    List<float[]> lux;
    float[] lastValue;
    Sensor mSensor;
    int dimension;
    final static int holdUnit = 100;

    public PathView(Context context) {
        this(context, null);
    }

    public PathView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
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
        super.onDraw(canvas);

        float resolution;

        if (lux == null){
            return;
        }
        Iterator iterator = lux.iterator();
        float max = lux.get(0)[0];
        float min = max;
        while (iterator.hasNext()) {
            for (float i : (float[]) iterator.next()) {
                if (max < i) {
                    max = i;
                }
                if (min > i) {
                    min = i;
                }
            }
        }

        if (max != min) {
            resolution = (mHeight * 0.8f) / (max - min);
        } else resolution = 0;

        for (int j = 0; j < dimension; j++) {
            mPath = new Path();
            mPath.moveTo(0, mHeight * 0.9f - resolution * (lux.get(j)[0] - min));
            for (int i = 1; i < lux.get(j).length; i++) {
                mPath.lineTo(mWidth / holdUnit * i, mHeight * 0.9f - resolution * (lux.get(j)[i] - min));
            }

            //设置画笔style
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(mLineColor[j]);
            mPaint.setStrokeWidth(5);
            canvas.drawPath(mPath, mPaint);
        }
    }

    public void setData(FloatQueue lux, Sensor sensor) {
        dimension = lux.getDimen();
        List<float[]> tmp = lux.getQueue(dimension);
        this.lux = new ArrayList<>(dimension);

        for (float[] i : tmp) {
            this.lux.add(extract(i));
        }

        lastValue = lux.deQueue(dimension);
        mSensor = sensor;
    }

    public float[] extract(float[] in) {
        if (in.length < holdUnit)
            return in;
        else {
            float[] tmp = new float[holdUnit];

            for (int i = in.length - holdUnit, j = 0; i < in.length; i++, j++) {
                tmp[j] = in[i];
            }
            return tmp;
        }
    }
}
