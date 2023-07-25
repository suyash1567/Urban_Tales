package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ProgressBar;

public class Statistics extends AppCompatActivity {

    ProgressBar pb1, pb2, pb3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics);

        pb1=findViewById(R.id.progress_bar);
        pb2=findViewById(R.id.progress_bar2);
        pb3=findViewById(R.id.progress_bar3);
        pb1.setProgress(50);
        pb2.setProgress(85);
        pb3.setProgress(78);
    }
}