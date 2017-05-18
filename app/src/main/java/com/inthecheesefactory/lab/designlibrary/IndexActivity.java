package com.inthecheesefactory.lab.designlibrary;

import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inthecheesefactory.lab.designlibrary.reference.FloatQueue;
import com.inthecheesefactory.lab.designlibrary.reference.SDCardHelper;
import com.inthecheesefactory.lab.designlibrary.reference.SaveSensorData;

import java.util.ArrayList;
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

    List<FloatQueue> sensData;

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
        sensData = new ArrayList<>();

        int sensorNum = 0;
        for (Sensor mSensor : deviceSensors) {
            String[] type;
            sensorNum++;

            switch (mSensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    type = initSensor(mSensor, "TYPE_ACCELEROMETER", getString(R.string.txt_acce));
                    break;
                case Sensor.TYPE_AMBIENT_TEMPERATURE:
                    type = initSensor(mSensor, "TYPE_AMBIENT_TEMPERATURE", getString(R.string.txt_temp));
                    break;
                case Sensor.TYPE_GAME_ROTATION_VECTOR:
                    type = initSensor(mSensor, "TYPE_GAME_ROTATION_VECTOR", getString(R.string.txt_rot));
                    break;
                case Sensor.TYPE_GRAVITY:
                    type = initSensor(mSensor, "TYPE_GRAVITY", getString(R.string.txt_gra));
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    type = initSensor(mSensor, "TYPE_GYROSCOPE", getString(R.string.txt_gyo));
                    break;
                case Sensor.TYPE_LIGHT:
                    type = initSensor(mSensor, "TYPE_LIGHT", getString(R.string.txt_light));
                    break;
                case Sensor.TYPE_LINEAR_ACCELERATION:
                    type = initSensor(mSensor, "TYPE_LINEAR_ACCELERATION", getString(R.string.txt_lacce));

                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    type = initSensor(mSensor, "TYPE_MAGNETIC_FIELD", getString(R.string.txt_mag));

                    break;
                case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                    type = initSensor(mSensor, "TYPE_MAGNETIC_FIELD_UNCALIBRATED", getString(R.string.txt_unmag));

                    break;
                case Sensor.TYPE_PROXIMITY:
                    type = initSensor(mSensor, "TYPE_PROXIMITY", getString(R.string.txt_pro));

                    break;
                case Sensor.TYPE_ROTATION_VECTOR:
                    type = initSensor(mSensor, "TYPE_ROTATION_VECTOR", getString(R.string.txt_rot));

                    break;
                case Sensor.TYPE_SIGNIFICANT_MOTION:
                    type = initSensor(mSensor, "TYPE_SIGNIFICANT_MOTION", getString(R.string.txt_motion));

                    break;
                case Sensor.TYPE_TEMPERATURE:
                    type = initSensor(mSensor, "TYPE_TEMPERATURE", getString(R.string.txt_cuptemp));

                    break;
                case Sensor.TYPE_PRESSURE:
                    type = initSensor(mSensor, "TYPE_PRESSURE", getString(R.string.txt_pre));

                    break;
                case Sensor.TYPE_STEP_COUNTER:
                    type = initSensor(mSensor, "TYPE_STEP_COUNTER", getString(R.string.txt_step));

                    break;
                case Sensor.TYPE_RELATIVE_HUMIDITY:
                    type = initSensor(mSensor, "TYPE_RELATIVE_HUMIDITY", getString(R.string.txt_humi));

                    break;
                case Sensor.TYPE_ORIENTATION:
                    type = initSensor(mSensor, "TYPE_ORIENTATION", getString(R.string.txt_ori));

                    break;

                default:
                    type = initSensor(mSensor, getString(R.string.txt_unknown), mSensor.getName());

                    break;
            }

            TextView textView = new TextView(this);
            textView.setText("Name: " + mSensor.getName() + "\n");
            textView.append("\t\t\tType: " + type[0] + "\n");
            textView.append("\t\t\tDescription: " + type[1]);
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

    private String[] initSensor(Sensor mSensor, String type, String description) {
        String[] ret = new String[2];
        ret[0] = type;
        ret[1] = description;
        mSensorManager.registerListener(mySensorListener,
                mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensData.add(new FloatQueue(100, 3, mSensor.getName()));
        return ret;
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initInstances() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(IndexActivity.this, drawerLayout, R.string.hello_world, R.string.hello_world);
        drawerLayout.addDrawerListener(drawerToggle);

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
                        sensorName = Sensor.TYPE_LIGHT;
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
        getMenuInflater().inflate(R.menu.optionmenu_list, menu);
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

        if (item.getItemId() == R.id.save_to_txt) {

            new AlertDialog.Builder(IndexActivity.this)
                    .setTitle("Choose an activity")
                    .setItems(R.array.activity_type, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            saveSensToFiles(
                                    getResources().getStringArray(R.array.activity_type)[i]);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();

        }
        return super.onOptionsItemSelected(item);
    }

    private void saveSensToFiles(String state) {
        boolean hasSuccess = false, hasFail = false;
        for (FloatQueue sd : sensData) {
            Bundle bundle = new Bundle();
            int dimension = sd.getDimen();
            List<float[]> tmp = sd.getQueue(dimension);

            bundle.putInt("dimension", dimension);
            bundle.putString("sensor", sd.getTag());
            bundle.putLongArray("time", sd.getTime());
            for (int i = 0; i < dimension; i++)
                bundle.putFloatArray("value" + i, tmp.get(i));

            SaveSensorData saveSensorData = new SaveSensorData(bundle);
            String fileName = saveSensorData.getFileName();

            if (SDCardHelper.saveSensorData(
                    saveSensorData.getOutputData().getBytes(), state, fileName)) {
                hasSuccess = true;
            } else {
                hasFail = true;
            }
        }

        if (hasSuccess && !hasFail)
            Toast.makeText(IndexActivity.this,
                    R.string.success_save,
                    Toast.LENGTH_SHORT).show();
        else if (hasFail && !hasSuccess)
            Toast.makeText(IndexActivity.this,
                    R.string.fail_to_save,
                    Toast.LENGTH_SHORT).show();
        else if (hasFail && hasSuccess)
            Toast.makeText(IndexActivity.this,
                    R.string.some_success,
                    Toast.LENGTH_SHORT).show();
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