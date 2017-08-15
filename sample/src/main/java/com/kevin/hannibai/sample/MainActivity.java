package com.kevin.hannibai.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.kevin.hannibai.Hannibai;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click(View view) {
        SamplePreferenceHandle samplePreferenceHandle = Hannibai.create(SamplePreferenceHandle.class);
        samplePreferenceHandle.getIsGood();

    }
}
