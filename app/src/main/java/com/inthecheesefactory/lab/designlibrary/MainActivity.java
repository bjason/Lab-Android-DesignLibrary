package com.inthecheesefactory.lab.designlibrary;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    Toolbar toolbar;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;

    CoordinatorLayout rootLayout;

    private SensorManager mSensorManager;
    List<Sensor> deviceSensors;

    private SensorEventListener mySensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            Sensor sensor = event.sensor;
            TextView textView = (TextView) findViewById(sensor.getType());

            textView.setText("\t\t\tValue: " + event.values[0]);
            if (event.values.length == 2) {
                textView.append("," + event.values[1]);
            }
            if (event.values.length == 3) {
                textView.append("," + event.values[2]);
            }
            textView.append("\n\n");
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initInstances();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.view_layout);

        int sensorNum = 0;
        for (Sensor sensor : deviceSensors) {
            String type;
            String description;
            sensorNum++;

            switch (sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    type = "TYPE_ACCELEROMETER";
                    description = "加速度传感器";
                    mSensorManager.registerListener(mySensorListener,
                            sensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
                case Sensor.TYPE_AMBIENT_TEMPERATURE:
                    type = "TYPE_AMBIENT_TEMPERATURE";
                    description = "外界温度传感器";
                    mSensorManager.registerListener(mySensorListener,
                            sensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
                case Sensor.TYPE_GAME_ROTATION_VECTOR:
                    type = "TYPE_GAME_ROTATION_VECTOR";
                    description = "转动向量传感器";
                    mSensorManager.registerListener(mySensorListener,
                            sensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
                case Sensor.TYPE_GRAVITY:
                    type = "TYPE_GRAVITY";
                    description = "重力传感器";
                    mSensorManager.registerListener(mySensorListener,
                            sensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    type = "TYPE_GYROSCOPE";
                    description = "陀螺仪";
                    mSensorManager.registerListener(mySensorListener,
                            sensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
                case Sensor.TYPE_LIGHT:
                    type = "TYPE_LIGHT";
                    description = "光照传感器";
                    mSensorManager.registerListener(mySensorListener,
                            sensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
                case Sensor.TYPE_LINEAR_ACCELERATION:
                    type = "TYPE_LINEAR_ACCELERATION";
                    description = "线性加速度传感器";
                    mSensorManager.registerListener(mySensorListener,
                            sensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    type = "TYPE_MAGNETIC_FIELD";
                    description = "磁场传感器";
                    mSensorManager.registerListener(mySensorListener,
                            sensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                    type = "TYPE_MAGNETIC_FIELD_UNCALIBRATED";
                    description = "未校准磁场传感器";
                    mSensorManager.registerListener(mySensorListener,
                            sensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
                case Sensor.TYPE_PROXIMITY:
                    type = "TYPE_PROXIMITY";
                    description = "近距离传感器";
                    mSensorManager.registerListener(mySensorListener,
                            sensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
                case Sensor.TYPE_ROTATION_VECTOR:
                    type = "TYPE_ROTATION_VECTOR";
                    description = "旋转向量传感器";
                    mSensorManager.registerListener(mySensorListener,
                            sensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
                case Sensor.TYPE_SIGNIFICANT_MOTION:
                    type = "TYPE_SIGNIFICANT_MOTION";
                    description = "有力动作感应器";
                    mSensorManager.registerListener(mySensorListener,
                            sensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
                case Sensor.TYPE_TEMPERATURE:
                    type = "TYPE_TEMPERATURE";
                    description = "cpu温度感应器";
                    mSensorManager.registerListener(mySensorListener,
                            sensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
                case Sensor.TYPE_PRESSURE:
                    type = "TYPE_PRESSURE";
                    description = "压力感应器";
                    mSensorManager.registerListener(mySensorListener,
                            sensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
                case Sensor.TYPE_STEP_COUNTER:
                    type = "TYPE_STEP_COUNTER";
                    description = "计步器";
                    mSensorManager.registerListener(mySensorListener,
                            sensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
                case Sensor.TYPE_RELATIVE_HUMIDITY:
                    type = "TYPE_RELATIVE_HUMIDITY";
                    description = "相对湿度感应器";
                    mSensorManager.registerListener(mySensorListener,
                            sensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
                case Sensor.TYPE_ORIENTATION:
                    type = "TYPE_ORIENTATION";
                    description = "方向感应器";
                    mSensorManager.registerListener(mySensorListener,
                            sensor, SensorManager.SENSOR_DELAY_GAME);
                    break;

                default:
                    type = "未知";
                    description = "未知";
                    mSensorManager.registerListener(mySensorListener,
                            sensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
            }

            TextView textView = new TextView(this);
            textView.setText("Name: " + sensor.getName() + "\n");
            textView.append("\t\t\tType: " + type + "\n");
            textView.append("\t\t\tDescription: " + description);
            textView.setTextColor(Color.WHITE);
            linearLayout.addView(textView);

            textView = new TextView(this);
            textView.setId(sensor.getType());
            textView.setText("\t\t\tValue: N/A" + "\n\n");
            textView.setTextColor(Color.WHITE);
            linearLayout.addView(textView);
        }

        TextView textView = (TextView) findViewById(R.id.text_main);
        textView.setText("There are " + sensorNum + " sensor(s) in your device.");
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initInstances() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.hello_world, R.string.hello_world);
        drawerLayout.setDrawerListener(drawerToggle);

        final Intent lightIntent = new Intent(this, LightSensorActivity.class);
        final Intent accIntent = new Intent(this, AcceSensorActivity.class);
        final Intent graIntent = new Intent(this, GravitySensorActivity.class);
        final Intent gyoIntent = new Intent(this, GyoSensorActivity.class);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);

        NavigationView navigation = (NavigationView) findViewById(R.id.navigation);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.home_button:
                        break;
                    case R.id.lightSensor:
                        startActivity(lightIntent);
                        finish();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        //noinspection SimplifiableIfStatement
        if (item.getItemId() == R.id.action_about) {
            //Toast.makeText(MainActivity.this, R.string.about, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.action_pause) {
            unregisterListener();
        }

        return super.onOptionsItemSelected(item);
    }

    public void unregisterListener() {
        mSensorManager.unregisterListener(mySensorListener);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterListener();
    }
}