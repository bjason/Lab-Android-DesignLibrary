package com.inthecheesefactory.lab.designlibrary;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Arrays;

public class LightSensorActivity extends AppCompatActivity implements SensorEventListener {
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;

    private SensorManager mSensorManager;
    private Sensor mLight;
    private PathView pathView;
    private FloatQueue lux = new FloatQueue(100);
    private float sen;

    Thread thread;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    //Log.v("handle", "here");
                    pathView.setData(lux);
                    pathView.invalidate();
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
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (mLight != null) {
            pathView = new PathView(this);
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.view_layout);
            relativeLayout.addView(pathView);

            thread = new Thread(new LightSensorActivity.GameThread());
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
        drawerToggle = new ActionBarDrawerToggle(LightSensorActivity.this, drawerLayout, R.string.hello_world, R.string.hello_world);
        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.txt_light);

        final Intent homeIntent = new Intent(this, MainActivity.class);
        final Intent accIntent = new Intent(this, AcceSensorActivity.class);
        final Intent graIntent = new Intent(this, GravitySensorActivity.class);
        final Intent gyoIntent = new Intent(this, GyoSensorActivity.class);

        NavigationView navigation = (NavigationView) findViewById(R.id.navigation);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.home_button:
                        startActivity(homeIntent);
                        finish();
                        break;
                    case R.id.lightSensor:
                        break;
                    case R.id.acceSensor:
                        startActivity(accIntent);
                        finish();
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
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
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
                pathView.postInvalidate();

                //Send message
                LightSensorActivity.this.mHandler.sendMessage(message);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
