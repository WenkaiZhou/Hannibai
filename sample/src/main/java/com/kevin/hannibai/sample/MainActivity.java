package com.kevin.hannibai.sample;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kevin.hannibai.HannibaiTable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        new SamplePreference();
//        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
//        String name = preferences.getString("name", "defaultname");
//       preferences.get


//        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        String name = "xixi";
//        String age = "22";
//        editor.putString("name", name);
//        editor.putString("age", age);
////        editor.remove()
//        editor.commit();



    }

    public void click(View view) {
        SamplePreferenceHannibai samplePreference = HannibaiTable.getSamplePreference();
        samplePreference.getIsGood();
//        SamplePreferenceHannibai samplePreference = Hannibai.getInstance().create(SamplePreferenceHannibai.class);
//        samplePreference.getIsGood();
    }
}
