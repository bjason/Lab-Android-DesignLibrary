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

//using accelerator to test the gesture
//y = +-10 means being standing

public class IndexActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_index);

        initToolbar();
        initInstances();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.view_layout_main);

        int sensorNum = 0;
        for (Sensor mSensor : deviceSensors) {
            String type;
            String description;
            sensorNum++;

            switch (mSensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    type = "TYPE_ACCELEROMETER";
                    description = getString(R.string.txt_acce);
                    mSensorManager.registerListener(mySensorListener,
                            mSensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
                case Sensor.TYPE_AMBIENT_TEMPERATURE:
                    type = "TYPE_AMBIENT_TEMPERATURE";
                    description = getString(R.string.txt_temp);
                    mSensorManager.registerListener(mySensorListener,
                            mSensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
                case Sensor.TYPE_GAME_ROTATION_VECTOR:
                    type = "TYPE_GAME_ROTATION_VECTOR";
                    description = getString(R.string.txt_rot);
                    mSensorManager.registerListener(mySensorListener,
                            mSensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
                case Sensor.TYPE_GRAVITY:
                    type = "TYPE_GRAVITY";
                    description = getString(R.string.txt_gra);
                    mSensorManager.registerListener(mySensorListener,
                            mSensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    type = "TYPE_GYROSCOPE";
                    description = getString(R.string.txt_gyo);
                    mSensorManager.registerListener(mySensorListener,
                            mSensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
                case Sensor.TYPE_LIGHT:
                    type = "TYPE_LIGHT";
                    description = getString(R.string.txt_light);
                    mSensorManager.registerListener(mySensorListener,
                            mSensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
                case Sensor.TYPE_LINEAR_ACCELERATION:
                    type = "TYPE_LINEAR_ACCELERATION";
                    description = getString(R.string.txt_lacce);
                    mSensorManager.registerListener(mySensorListener,
                            mSensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    type = "TYPE_MAGNETIC_FIELD";
                    description = getString(R.string.txt_mag);
                    mSensorManager.registerListener(mySensorListener,
                            mSensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                    type = "TYPE_MAGNETIC_FIELD_UNCALIBRATED";
                    description = getString(R.string.txt_unmag);
                    mSensorManager.registerListener(mySensorListener,
                            mSensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
                case Sensor.TYPE_PROXIMITY:
                    type = "TYPE_PROXIMITY";
                    description = getString(R.string.txt_pro);
                    mSensorManager.registerListener(mySensorListener,
                            mSensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
                case Sensor.TYPE_ROTATION_VECTOR:
                    type = "TYPE_ROTATION_VECTOR";
                    description = getString(R.string.txt_rot);
                    mSensorManager.registerListener(mySensorListener,
                            mSensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
                case Sensor.TYPE_SIGNIFICANT_MOTION:
                    type = "TYPE_SIGNIFICANT_MOTION";
                    description = getString(R.string.txt_motion);
                    mSensorManager.registerListener(mySensorListener,
                            mSensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
                case Sensor.TYPE_TEMPERATURE:
                    type = "TYPE_TEMPERATURE";
                    description = getString(R.string.txt_cuptemp);
                    mSensorManager.registerListener(mySensorListener,
                            mSensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
                case Sensor.TYPE_PRESSURE:
                    type = "TYPE_PRESSURE";
                    description = getString(R.string.txt_pre);
                    mSensorManager.registerListener(mySensorListener,
                            mSensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
                case Sensor.TYPE_STEP_COUNTER:
                    type = "TYPE_STEP_COUNTER";
                    description = getString(R.string.txt_step);
                    mSensorManager.registerListener(mySensorListener,
                            mSensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
                case Sensor.TYPE_RELATIVE_HUMIDITY:
                    type = "TYPE_RELATIVE_HUMIDITY";
                    description = getString(R.string.txt_humi);
                    mSensorManager.registerListener(mySensorListener,
                            mSensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
                case Sensor.TYPE_ORIENTATION:
                    type = "TYPE_ORIENTATION";
                    description = getString(R.string.txt_ori);
                    mSensorManager.registerListener(mySensorListener,
                            mSensor, SensorManager.SENSOR_DELAY_GAME);
                    break;

                default:
                    type = getString(R.string.txt_unknown);
                    description = mSensor.getName();
                    mSensorManager.registerListener(mySensorListener,
                            mSensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
            }

            TextView textView = new TextView(this);
            textView.setText("Name: " + mSensor.getName() + "\n");
            textView.append("\t\t\tType: " + type + "\n");
            textView.append("\t\t\tDescription: " + description);
            textView.setTextColor(Color.WHITE);
            linearLayout.addView(textView);

            textView = new TextView(this);
            textView.setId(mSensor.getType());
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
        drawerToggle = new ActionBarDrawerToggle(IndexActivity.this, drawerLayout, R.string.hello_world, R.string.hello_world);
        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);

        NavigationView navigation = (NavigationView) findViewById(R.id.navigation);
        final Intent intent = new Intent(this, SensorActivity.class);
        final Intent intentHome = new Intent(this, HomeActivity.class);
        final Intent intentIndex = new Intent(this, IndexActivity.class);

        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int sensorName = 0;
                switch (menuItem.getItemId()) {
                    case R.id.home_button:
                        startActivity(intentHome);
                        finish();
                        return true;
                    case R.id.index_button:
                        startActivity(intentIndex);
                        finish();
                        return true;
                    case R.id.lightSensor:
                        sensorName=Sensor.TYPE_LIGHT;
                        break;
                    case R.id.acceSensor:
                        sensorName = Sensor.TYPE_ACCELEROMETER;
                        break;
                    case R.id.graSensor:
                        sensorName = Sensor.TYPE_GRAVITY;
                        break;
                    case R.id.GyroSensor:
                        sensorName = Sensor.TYPE_GYROSCOPE;
                        break;
                    case R.id.proxiSensor:
                        sensorName = Sensor.TYPE_PROXIMITY;
                        break;
                    case R.id.magSensor:
                        sensorName = Sensor.TYPE_MAGNETIC_FIELD;
                        break;
                    case R.id.temperatureSensor:
                        sensorName = Sensor.TYPE_TEMPERATURE;
                        break;
                    case R.id.humiditySensor:
                        sensorName = Sensor.TYPE_RELATIVE_HUMIDITY;
                        break;
                    case R.id.pressureSensor:
                        sensorName = Sensor.TYPE_PRESSURE;
                        break;
                    case R.id.rotateSensor:
                        sensorName = Sensor.TYPE_ROTATION_VECTOR;
                        break;

                    default:
                        break;
                }
                intent.putExtra("SensorName", sensorName);
                startActivity(intent);
                finish();
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
        getMenuInflater().inflate(R.menu.optionmenu_main, menu);
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterListener();
    }
}