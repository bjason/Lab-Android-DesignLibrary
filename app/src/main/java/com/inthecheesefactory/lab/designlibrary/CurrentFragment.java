package com.inthecheesefactory.lab.designlibrary;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inthecheesefactory.lab.designlibrary.reference.FloatQueue;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentUpdateListener} interface
 * to handle interaction events.
 * Use the {@link CurrentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentFragment extends Fragment {

    private SensorManager mSensorManager;
    List<Sensor> deviceSensors;

    FloatQueue linearValues, acceValues;
    double currentState = 0;
    int ignore;
    boolean ifLinearAccelerationExist;
    Date now, lastChange;

    private OnFragmentUpdateListener mListener;

    public CurrentFragment() {
        // Required empty public constructor
    }

    private SensorEventListener mySensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            Sensor sensor = event.sensor;
            int sensorType = sensor.getType();
            float x, y, z;
            double SVM;
            Date now = Calendar.getInstance().getTime();
            double lastState = currentState;

            String currState = "";

            switch (sensorType) {
                case Sensor.TYPE_LINEAR_ACCELERATION: {
                    x = event.values[0];
                    y = event.values[1];
                    z = event.values[2];

                    linearValues.enQueue(x, y, z);

                    SVM = Math.sqrt(x * x + y * y + z * z);

                    if (SVM < 0.5) {
                        if (currentState != 1.1) {
                            //2.1 for sitting
                            //2.2 for standing
                            currentState = 2;   //2 for standing or sitting
                            currState = getString(R.string.txt_standing);
                        }
                    } else if (SVM < 7.5) {
                        currentState = 3.1; // for walking
                        currState = getString(R.string.txt_walking);
                    } else if (SVM < 15) {
                        currentState = 3.2; // for running
                        currState = getString(R.string.txt_running);
                    } else {
                        currentState = 3.5; // for tumbling
                    }
                    //4 for moving (cycling or sitting in car)

                    if (currentState != 3.5) {
                        if (lastState != currentState)
                            lastChange = changeTimeTo(now);
                    }
                }

                case Sensor.TYPE_ACCELEROMETER: {

                    x = event.values[0];
                    y = event.values[1];
                    z = event.values[2];

                    SVM = Math.sqrt(x * x + y * y + z * z);

                    //to solve linear accelerator not existing problem
                    if (!ifLinearAccelerationExist) {
                        /*
                        lowX = event.values[0] * FILTERING_VALAUE + lowX * (1.0f - FILTERING_VALAUE);
                        lowY = event.values[0] * FILTERING_VALAUE + lowY * (1.0f - FILTERING_VALAUE);
                        lowZ = event.values[0] * FILTERING_VALAUE + lowZ * (1.0f - FILTERING_VALAUE);
                        高通滤波器：
                        float highX   =   event.values[0]   -   lowX;
                        float highY   =   event.values[0]   -   lowY;
                        float highZ   =   event.values[0]   -   lowZ;*/
                    }

                    //to testify validation of tumble

                    acceValues.enQueue(event.values);

                    if (currentState == 3.5) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (Math.abs(z) > 5 || Math.abs(y) > 5) {
                            currentState = 5;
                            currState = getString(R.string.txt_tumble);
                            //...and other detect method
                        }
                        if ((lastState != currentState)) {
                            lastChange = changeTimeTo(now);
                        }
                    }

                    //determine the orientation: lie down / grovel
                    if (Math.abs(y) < 3) {
                        if (z > 8.8) {
                            currentState = 1.1;//1.1 for supine
                            currState = getString(R.string.txt_supine);
                        } else if (z < -8.8) {
                            currentState = 1.2;//1.2 for prone
                            currState = getString(R.string.txt_prone);
                        } else if (SVM > 9) {
                            currentState = 1.3;//1.3 for lateral
                            currState = getString(R.string.txt_lateral);
                        }
                        if ((lastState != currentState)) {
                            lastChange = changeTimeTo(now);
                        }
                    }

                }
            }
            if (currentState != 0) {
                long diff = now.getTime() - lastChange.getTime();
                long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diff);
                TextView tv = (TextView) getView().findViewById(R.id.gesture_keep);
                tv.setText(getString(R.string.txt_keep) + diffInSec + getString(R.string.seconds));

                if ((currentState != lastState) && !Objects.equals(currState, "")) {
                    stateChange(currState, diffInSec);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    private Date changeTimeTo(Date now) {
        Date lastTime = this.now;
        this.now = now;
        return lastTime;
    }

    private void stateChange(String txt_state, long timeLast) {
        TextView textView = (TextView) getView().findViewById(R.id.text_current_gesture);
        Bundle bundle = new Bundle();

        textView.setText(txt_state);
        //ImageView imageView = (ImageView) findViewById(R.id.cover_img);
        //changeImageView(imageView, ResourcesCompat.getDrawable(getResources(), drawable, null));

        //TODO send bundle to the activity that is supposed to handle the change
        bundle.putString("state", txt_state);
        bundle.putLong("timeLast", timeLast);
        mListener.onStateUpdate(bundle);
    }

    void changeImageView(ImageView imageView, Drawable changeTo) {
        TransitionDrawable td = new TransitionDrawable(new Drawable[]{
                imageView.getDrawable(),
                changeTo
        });
        imageView.setImageDrawable(td);
        td.startTransition(500);
    }

    public static CurrentFragment newInstance() {
        return new CurrentFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        linearValues = new FloatQueue(100, 3);
        acceValues = new FloatQueue(100, 3);
        ignore = 0;
        currentState = 0;
        now = Calendar.getInstance().getTime();
    }

    private void initSensors() {

        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        //register needed sensors
        for (Sensor mSensor : deviceSensors) {
            switch (mSensor.getType()) {
                case Sensor.TYPE_LINEAR_ACCELERATION:
                    mSensorManager.registerListener(mySensorListener,
                            mSensor, SensorManager.SENSOR_DELAY_UI);
                    ifLinearAccelerationExist = true;
                    break;
                case Sensor.TYPE_ACCELEROMETER:
                    mSensorManager.registerListener(mySensorListener,
                            mSensor, SensorManager.SENSOR_DELAY_UI);
                    break;
                case Sensor.TYPE_STEP_COUNTER:
                    mSensorManager.registerListener(mySensorListener,
                            mSensor, SensorManager.SENSOR_DELAY_UI);
                    break;
                case Sensor.TYPE_STEP_DETECTOR:
                    mSensorManager.registerListener(mySensorListener,
                            mSensor, SensorManager.SENSOR_DELAY_UI);
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        initSensors();
        return inflater.inflate(R.layout.fragment_current, container, false);
    }

    /*// TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onStateUpdate(uri);
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentUpdateListener) {
            mListener = (OnFragmentUpdateListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentUpdateListener {
        // TODO: Update argument type and name
        void onStateUpdate(Bundle state);
    }

    public void unregisterListener() {
        mSensorManager.unregisterListener(mySensorListener);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterListener();
    }
}
