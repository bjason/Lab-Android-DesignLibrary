package com.inthecheesefactory.lab.designlibrary;

import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.inthecheesefactory.lab.designlibrary.reference.SimpleFragmentPagerAdapter;

//using accelerator to test the gesture
//y = +-10 means being standing
//z = +-10 means being sitting

//to-do
//* image slowly change
//image with dark filter
//make sensor items in navi menu collapse-able
//stop recording -> resume recording
//*menu with icon
//*z axes of pathview should be lower than textview

public class HomeActivity extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener,
        CurrentFragment.OnFragmentInteractionListener,
        SummaryFragment.OnFragmentInteractionListener {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    CoordinatorLayout rootLayout;
    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;
    private ImageView mProfileImage;
    private int mMaxScrollSize;

    private SimpleFragmentPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initToolbar();
        initInstances();
        initFragment();

        AppBarLayout appbarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
        appbarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = appbarLayout.getTotalScrollRange();
    }

    private void initFragment() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        pagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(pagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.txt_current)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.txt_summary)));
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initInstances() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(HomeActivity.this, drawerLayout, R.string.hello_world, R.string.hello_world);
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
            //unregisterListener();
        }

        if (item.getItemId() == R.id.setting) {

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        mProfileImage = (ImageView) findViewById(R.id.materialup_profile_image);
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(verticalOffset)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;

            mProfileImage.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(200)
                    .start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            mProfileImage.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}