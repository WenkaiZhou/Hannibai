package com.kevin.hannibai.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.muzhi.sp.AskPreferenceHandle;
import com.google.gson.Gson;
import com.kevin.hannibai.BaseModel;
import com.kevin.hannibai.Hannibai;

public class MainActivity extends AppCompatActivity {

    private EditText mEtUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEtUserName = (EditText) this.findViewById(R.id.et_name);
    }

    public void click1(View view) {
        SamplePreferenceHandle samplePreferenceHandle = Hannibai.create(SamplePreferenceHandle.class);
        samplePreferenceHandle.setUserName(mEtUserName.getText().toString());


        String userName = samplePreferenceHandle.getUserName();
        Toast.makeText(MainActivity.this, "userName = " + userName, Toast.LENGTH_SHORT).show();

    }

    public void click2(View view) {
        AskPreferenceHandle askPreferenceHandle = Hannibai.create(AskPreferenceHandle.class);
        askPreferenceHandle.setVersion(mEtUserName.getText().toString());


        String version = askPreferenceHandle.getVersion();
        Toast.makeText(MainActivity.this, "version = " + version, Toast.LENGTH_SHORT).show();

    }
}
