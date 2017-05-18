package com.inthecheesefactory.lab.designlibrary.reference;

import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by WU on 2017/4/21.
 * dimension, lux, time, sensorName is needed
 */

public class SaveSensorData {

    private List<float[]> lux;
    private long[] time;
    private String sensorName;
    private int dimension;

    public SaveSensorData(Bundle bundle){
        dimension = bundle.getInt("dimension", 0);

        //use lux to mimic a floatqueue
        lux = new ArrayList<>(dimension);
        for (int i = 0; i < dimension; i++) {
            lux.add(bundle.getFloatArray("value" + i));
        }

        time = bundle.getLongArray("time");
        sensorName = bundle.getString("sensor");
    }

    public String[] getLine() {
        String[] tmp = new String[lux.get(0).length];

        for (int i = 0; i < lux.get(0).length; i++) {
            tmp[i] = ("\n" + time[i] + ": \t" + lux.get(0)[i]);
            if (dimension > 1) {
                tmp[i] = tmp[i].concat(" , " + lux.get(1)[i]);
            }
            if (dimension == 3)
                tmp[i] = tmp[i].concat(" , " + lux.get(2)[i]);
        }
        return tmp;
    }

    public String getOutputData() {
        String[] in = getLine();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < in.length; i++) {
            stringBuilder.append(in[i]);
        }
        return stringBuilder.toString();
    }

    public String getFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm");
        String fileName = sensorName + " in " +
                dateFormat.format(Calendar.getInstance().getTime()) + ".txt";
        return fileName;
    }
}
