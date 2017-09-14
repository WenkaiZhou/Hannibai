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

import com.kevin.hannibai.annotation.Commit;
import com.kevin.hannibai.annotation.DefBoolean;
import com.kevin.hannibai.annotation.DefFloat;
import com.kevin.hannibai.annotation.DefInt;
import com.kevin.hannibai.annotation.DefLong;
import com.kevin.hannibai.annotation.DefString;
import com.kevin.hannibai.annotation.Expire;
import com.kevin.hannibai.annotation.SharePreference;
import com.kevin.hannibai.sample.bean.User;

/**
 * Created by zhouwenkai on 2017/8/12.
 */

@SharePreference
public class SamplePreference {
    @DefString("zwenkai")
    @Commit
    public String userName;
    @DefInt(18)
    public int age;
    @DefBoolean(true)
    @Expire(value = 1000000000000000000L, unit = Expire.Unit.DAYS)
    public boolean isGood;
    @DefLong(123456789)
    public long time;
    @DefFloat(123.45f)
    public float price;

    public String version;
    public int versionCode;
    public long startTime;
    public boolean isFirst;
    public float sprice;

    @DefInt(18)
    public Integer mAge;
    @DefBoolean(true)
    public Boolean ixsGood;
    @DefLong(123456789)
    public Long taime;
    @DefFloat(123.45f)
    public Float prsice;

    public User user;
}
