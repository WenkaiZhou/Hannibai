package com.kevin.hannibai.sample;

import android.app.Application;

import com.kevin.hannibai.Hannibai;

/**
 * Created by zhouwenkai on 2017/8/14.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Hannibai.init(this);
        Hannibai.setDebug(true);
        Hannibai.setConverterFactory(GsonConverterFactory.create());
    }
}
