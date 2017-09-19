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

import android.app.Application;

import com.baidu.hannibai.converter.fastjson.FastJsonConverterFactory;
import com.baidu.hannibai.converter.jackson.JacksonConverterFactory;
import com.kevin.hannibai.Hannibai;
import com.kevin.hannibai.converter.gson.GsonConverterFactory;

/**
 * Created by zhouwenkai on 2017/8/14.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Hannibai.init(this, false);
        Hannibai.setDebug(true);
//        Hannibai.setConverterFactory(GsonConverterFactory.create());
//        Hannibai.setConverterFactory(JacksonConverterFactory.create());
        Hannibai.setConverterFactory(FastJsonConverterFactory.create());
    }
}
