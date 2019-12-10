package com.y.echartsdemo;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void eCharts(View view) {
        EChartsActivity.start(this);
    }

    public void eCharts2(View view) {
        ECharts2Activity.start(this);
    }


}
