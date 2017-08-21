/*
 * Copyright (c) 2017 Kevin zhou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kevin.hannibai.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
        long startTime = System.currentTimeMillis();
        SamplePreferenceHandle samplePreferenceHandle = Hannibai.create(SamplePreferenceHandle.class);
        samplePreferenceHandle.setUserName(mEtUserName.getText().toString());


        String userName = samplePreferenceHandle.getUserName();
        Toast.makeText(MainActivity.this, "userName = " + userName + "time = " + (System.currentTimeMillis() - startTime), Toast.LENGTH_SHORT).show();

    }

    public void click2(View view) {
//        AskPreferenceHandle askPreferenceHandle = Hannibai.create(AskPreferenceHandle.class);
//        askPreferenceHandle.setVersion(mEtUserName.getText().toString());
//
//
//        String version = askPreferenceHandle.getVersion();
//        Toast.makeText(MainActivity.this, "version = " + version, Toast.LENGTH_SHORT).show();

        long startTime = System.currentTimeMillis();
        SamplePreferenceHandle samplePreferenceHandle = Hannibai.create(SamplePreferenceHandle.class, "my_id");
        samplePreferenceHandle.setUserName(mEtUserName.getText().toString());


        String userName = samplePreferenceHandle.getUserName();
        Toast.makeText(MainActivity.this, "userName = " + userName + "time = " + (System.currentTimeMillis() - startTime), Toast.LENGTH_SHORT).show();

    }
}
