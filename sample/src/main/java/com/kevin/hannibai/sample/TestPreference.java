package com.kevin.hannibai.sample;

import com.kevin.hannibai.annotation.DefBoolean;
import com.kevin.hannibai.annotation.DefFloat;
import com.kevin.hannibai.annotation.DefInt;
import com.kevin.hannibai.annotation.DefLong;
import com.kevin.hannibai.annotation.DefString;
import com.kevin.hannibai.annotation.SharePreference;
import com.kevin.hannibai.sample.bean.User;

/**
 * Created by zhouwenkai on 2017/8/12.
 */

@SharePreference
public class TestPreference {
    @DefString("zwenkai")
    public String userName;
    @DefInt(18)
    public int age;
    @DefBoolean(true)
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
