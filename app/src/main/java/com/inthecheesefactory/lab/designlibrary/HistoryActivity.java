package com.inthecheesefactory.lab.designlibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.inthecheesefactory.lab.designlibrary.reference.SDCardHelper;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

        sTitle.setText(sensorName + " History");

        sHistory.setText("Time: \t\t\t\tValue\n\n");
        for (String line : getLine()) {
            sHistory.append(line + "\n");
        }
    }

    protected String[] getLine() {
        String[] tmp = new String[lux.get(0).length];
        for (int i = 0; i < lux.get(0).length; i++) {
            tmp[i] = (time[i] + ": \t" + lux.get(0)[i]);
            if (dimension > 1) {
                tmp[i] = tmp[i].concat(" , " + lux.get(1)[i]);
            }
            if (dimension == 3)
                tmp[i] = tmp[i].concat(" , " + lux.get(2)[i]);
        }
        return tmp;
    }

    protected String getOutputData(String[] in) {
        String out = "";
        for (String i : in) {
            out.concat(i);
        }
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

        SDCardHelper sdCardHelper = new SDCardHelper();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {

            //get output data
                /*outputStream = new FileOutputStream(new File(getApplicationContext().getFilesDir(), fileName));
                for (String out : getLine()) {
                    outputStream.write(out.getBytes());
                }*/
            if (sdCardHelper.saveFileToSDCardPrivateFilesDir(getOutputData(getLine()).getBytes(),
                    fileName, this)){
                Toast.makeText(HistoryActivity.this,
                        fileName + " " + getString(R.string.save_to_excel),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(HistoryActivity.this,
                        fileName + " " + getString(R.string.save_fail),
                        Toast.LENGTH_SHORT).show();
            }

        }

        if (id == R.id.action_delete) {
            File[] files = getDir(sdCardHelper.getSDCardPrivateFilesDir(this), MODE_PRIVATE)
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
                if (file.delete())
                    Toast.makeText(HistoryActivity.this,
                            file.getName() + " " + getString(R.string.txt_delete),
                            Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(HistoryActivity.this,
                            getString(R.string.delete_fail) + file.getName(),
                            Toast.LENGTH_LONG).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}

