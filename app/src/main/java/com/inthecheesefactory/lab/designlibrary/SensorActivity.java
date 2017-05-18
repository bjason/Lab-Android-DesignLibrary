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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.inthecheesefactory.lab.designlibrary.reference.FloatQueue;
import com.inthecheesefactory.lab.designlibrary.reference.PathView;

import java.util.List;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    
//     String TAG = "SensorTest";

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private PathView pathView;
    private FloatQueue lux = new FloatQueue(1000);
    private float[] sen;
    private int dimension;
    Intent intent;
    int sensorType;

    Thread thread;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    //Log.v("handle", "here");
                    pathView.setData(lux, mSensor);
                    pathView.invalidate();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_background);
        intent = getIntent();

        initToolbar();
        initInstances();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorType = intent.getExtras().getInt("SensorName");
        mSensor = mSensorManager.getDefaultSensor(sensorType);
//         Log.d(TAG, "name: " + mSensor.getStringType());

        if (mSensor != null) {
            pathView = new PathView(this);
            FrameLayout frameLayout = (FrameLayout) findViewById(R.id.view_layout);
            frameLayout.addView(pathView);

            TextView unit = (TextView) findViewById(R.id.sensor_unit);
            unit.setText(R.string.txt_unit);
            switch (sensorType) {
                case Sensor.TYPE_ACCELEROMETER:
                    getSupportActionBar().setTitle(R.string.txt_acce);
                    //getSupportActionBar().setTitle(mSensor.getName());
                    unit.append("m/s^2");
                    break;
                case Sensor.TYPE_PROXIMITY:
                    getSupportActionBar().setTitle(R.string.txt_pro);
                    unit.append("CM");
                    dimension = 2;
                    break;
                case Sensor.TYPE_LIGHT:
                    getSupportActionBar().setTitle(R.string.txt_light);
                    unit.append("lux");
//                     Log.d(TAG, "onCreate: confirm");
                    dimension = 1;
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    getSupportActionBar().setTitle(R.string.txt_mag);
                    unit.append("uT");
                    break;
                case Sensor.TYPE_ROTATION_VECTOR:
                    getSupportActionBar().setTitle(R.string.txt_rot);
                    unit.append("null");
                    break;
                case Sensor.TYPE_PRESSURE:
                    getSupportActionBar().setTitle(R.string.txt_pre);
                    unit.append("kPa");
                    break;
                case Sensor.TYPE_GRAVITY:
                    getSupportActionBar().setTitle(R.string.txt_gra);
                    unit.append("N/M^2");
                    break;
                case Sensor.TYPE_TEMPERATURE:
                    getSupportActionBar().setTitle(R.string.txt_temp);
                    unit.append("K");
                    break;
                case Sensor.TYPE_RELATIVE_HUMIDITY:
                    getSupportActionBar().setTitle(R.string.txt_humi);
                    unit.append("%");
                    break;

                default:
                    break;
            }

            sen = new float[dimension];
            thread = new Thread(new SensorActivity.GameThread());
            thread.start();
        } else {
            TextView textView = (TextView) findViewById(R.id.sensor_value);
            textView.setText(R.string.no_sensor_hint);
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    //drawer
    private void initInstances() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(SensorActivity.this, drawerLayout, R.string.hello_world, R.string.hello_world);
        drawerLayout.addDrawerListener(drawerToggle);
        dimension = 3;

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                        dimension = 1;
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
        getMenuInflater().inflate(R.menu.optionmenu_sensor, menu);
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

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            //Toast.makeText(MainActivity.this, R.string.about, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_pause) {
            unregisterListener();
            thread.interrupt();
        }

        if (id == R.id.action_history) {
            Intent intent = new Intent(this, HistoryActivity.class);
            intent.putExtra("dimension", dimension);

            List<float[]> tmp = lux.getQueue(dimension);
            for (int i = 0; i < dimension; i++)
                intent.putExtra("value" + i, tmp.get(i));

            intent.putExtra("time", lux.getTime());
            intent.putExtra("sensor", mSensor.getName());
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void unregisterListener() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        TextView textView = (TextView) findViewById(R.id.sensor_value);

        sen[0] = sensorEvent.values[0];
        textView.setText("Value: \n" + sen[0]);

        if (dimension != 1) {
            sen[1] = sensorEvent.values[1];
            textView.append("\n , " + sen[1]);
//             Log.d(TAG, "onSensorChanged: 2");
        }
        if (dimension == 3) {
            sen[2] = sensorEvent.values[2];
            textView.append("\n , " + sen[2]);
//             Log.d(TAG, "onSensorChanged: 3");
        }

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
//                 Log.d(TAG, "run: " + Arrays.toString(sen));
                lux.enQueue(sen);
                pathView.postInvalidate();

                //Send message
                SensorActivity.this.mHandler.sendMessage(message);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
