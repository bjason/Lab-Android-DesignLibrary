package com.inthecheesefactory.lab.designlibrary;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.inthecheesefactory.lab.designlibrary.reference.SDCardHelper;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    Toolbar toolbar;
    List<float[]> lux;
    String[] time;
    String sensorName;
    int dimension;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //ActionBar actionBar = getSupportActionBar();
        toolbar = (Toolbar) findViewById(R.id.history_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        dimension = intent.getIntExtra("dimension", 0);

        lux = new ArrayList<>(dimension);
        for (int i = 0; i < dimension; i++) {
            lux.add(intent.getFloatArrayExtra("value" + i));
        }

        time = intent.getStringArrayExtra("time");
        TextView sTitle = (TextView) findViewById(R.id.textViewTitle);
        TextView sHistory = (TextView) findViewById(R.id.textViewHistory);
        sensorName = intent.getStringExtra("sensor");

        sTitle.setText(sensorName + getString(R.string.his));

        sHistory.setText("Time: \t\t\t\tValue\n\n");
        for (String line : getLine()) {
            sHistory.append(line);
        }
    }

    protected String[] getLine() {
        String[] tmp = new String[lux.get(0).length];

        for (int i = 0; i < lux.get(0).length; i++) {
            tmp[i] = ("\n" + time[i] + ": \t" + lux.get(0)[i]);
            if (dimension > 1) {
                tmp[i] = tmp[i].concat(" , " + lux.get(1)[i]);
            }
            if (dimension == 3)
                tmp[i] = tmp[i].concat(" , " + lux.get(2)[i]);
            tmp[i].concat("\n");
        }
        return tmp;
    }

    protected String getOutputData(String[] in) {
        String out = "";
        /*for (String i : in) {
            out.concat(i);
        }*/
        out = Arrays.toString(in);
        Log.d("con", "getOutputData: " + out);
        return out;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.optionmenu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm");
        String fileName = sensorName + " in " +
                dateFormat.format(Calendar.getInstance().getTime()) + ".txt";
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {

            if (SDCardHelper.saveFileToSDCardPrivateFilesDir(getOutputData(getLine()).getBytes(),
                    fileName, this)) {
                Toast.makeText(HistoryActivity.this,
                        fileName + " " + getString(R.string.save_to_txt),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(HistoryActivity.this,
                        fileName + " " + getString(R.string.save_fail),
                        Toast.LENGTH_SHORT).show();
            }

        }

        if (id == R.id.action_delete) {

            new AlertDialog.Builder(HistoryActivity.this).setTitle(getString(R.string.confirmation))
                    .setMessage("Are you sure to delete all the saved files?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteSaved();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteSaved() {
        String successDeleted = "", failDeleted = "";
        boolean hasErr = false, hasSuccess = false;

        File[] files = new File(SDCardHelper.getSDCardPrivateFilesDir(this))
                .listFiles(new FilenameFilter() {
                    //filter for txt files
                    @Override
                    public boolean accept(File dir, String name) {
                        if (name.lastIndexOf('.') > 0) {

                            // get last index for '.' char
                            int lastIndex = name.lastIndexOf('.');

                            // get extension
                            String str = name.substring(lastIndex);

                            // match path name extension
                            if (str.equals(".txt")) {
                                return true;
                            }
                        }
                        return false;
                    }
                });

        for (File file : files) {
            if (file.delete()) {
                hasSuccess = true;
                successDeleted.concat(file.getName() + "\n");
            } else {
                hasErr = true;
                failDeleted.concat(file.getName() + "\n");
            }
        }

        if (hasSuccess)
            Toast.makeText(HistoryActivity.this,
                    successDeleted + " " + getString(R.string.txt_delete),
                    Toast.LENGTH_SHORT).show();
        if (hasErr)
            Toast.makeText(HistoryActivity.this,
                    getString(R.string.delete_fail) + failDeleted,
                    Toast.LENGTH_LONG).show();
    }
}

