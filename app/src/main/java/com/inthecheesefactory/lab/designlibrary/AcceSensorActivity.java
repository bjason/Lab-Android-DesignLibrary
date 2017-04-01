package com.inthecheesefactory.lab.designlibrary;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Arrays;

public class AcceSensorActivity extends AppCompatActivity implements SensorEventListener {
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private AccePathView accePathView;
    private FloatQueue lux = new FloatQueue(100);
    private float sen;

    Thread thread;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    //Log.v("handle", "here");
                    accePathView.setData(lux);
                    accePathView.invalidate();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_background);

        initToolbar();
        initInstances();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (mSensor != null) {
            accePathView = new AccePathView(this);
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.view_layout);
            relativeLayout.addView(accePathView);

            thread = new Thread(new AcceSensorActivity.GameThread());
            thread.start();
        } else {
            TextView textView = (TextView) findViewById(R.id.light_hint);
            textView.setText("There is no light sensor in your device.");
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initInstances() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(AcceSensorActivity.this, drawerLayout, R.string.hello_world, R.string.hello_world);
        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.txt_acce);

        final Intent intent = new Intent(this, MainActivity.class);
        final Intent lightIntent = new Intent(this, LightSensorActivity.class);
        final Intent graIntent = new Intent(this, GravitySensorActivity.class);
        final Intent gyoIntent = new Intent(this, GyoSensorActivity.class);

        NavigationView navigation = (NavigationView) findViewById(R.id.navigation);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.home_button:
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.lightSensor:
                        startActivity(lightIntent);
                        finish();
                        break;
                    case R.id.acceSensor:
                        break;
                    case R.id.graSensor:
                        startActivity(graIntent);
                        finish();
                        break;
                    case R.id.GyroSensor:
                        startActivity(gyoIntent);
                        finish();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //noinspection SimplifiableIfStatement
        if (item.getItemId() == R.id.action_about) {
            //Toast.makeText(MainActivity.this, R.string.about, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.action_pause) {
            unregisterListener();
            thread.interrupt();
        }

        return super.onOptionsItemSelected(item);
    }

    private void unregisterListener() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.v("官方", Arrays.toString(sensorEvent.values));
        sen = sensorEvent.values[0];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    class GameThread implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                Message message = new Message();
                message.what = 1;
                lux.enqueue(sen);
                Log.v("lux", Arrays.toString(lux.getQueue()));
                accePathView.postInvalidate();

                //Send message
                AcceSensorActivity.this.mHandler.sendMessage(message);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    class AccePathView extends View {

        protected Paint mPaint;
        //折现的颜色
        protected int mLineColor = Color.parseColor("#76f112");

        //自身的大小
        protected int mWidth, mHeight;

        //心电图折现
        protected Path mPath;

        float[] lux = new float[100];
        float lastValue;

        public AccePathView(Context context) {
            this(context, null);
        }

        public AccePathView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public AccePathView(Context context, AttributeSet attrs, int defStyleAttr) {
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

        protected void onDraw(Canvas canvas) {
            Log.v("draw", "here");
            super.onDraw(canvas);
            mPath.reset();
            mPath.moveTo(0, mHeight / 2);

            for (int i = 1; i < lux.length; i++) {
                mPath.lineTo(11 * i, mHeight / 2 - 30 * lux[i]);
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
}
