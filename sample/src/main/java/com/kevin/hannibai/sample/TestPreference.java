package com.kevin.hannibai.sample;

import com.kevin.hannibai.annotation.Apply;
import com.kevin.hannibai.annotation.Commit;
import com.kevin.hannibai.annotation.DefFloat;
import com.kevin.hannibai.annotation.DefInt;
import com.kevin.hannibai.annotation.DefLong;
import com.kevin.hannibai.annotation.Expire;
import com.kevin.hannibai.annotation.SharePreference;

/**
 * Created by zhouwenkai on 2017/8/29.
 */

@SharePreference
public class TestPreference {
    public String name;
    @Commit
    @DefInt(18)
    public int age;
    @DefLong(10000L)
    @Expire(value = 1, unit = Expire.Unit.MINUTES)
    public long no;
    @Apply
    @DefFloat(25000.23F)
    @Expire(value = 20, unit = Expire.Unit.SECONDS, update = true)
    public float salary;

    public Employee employee;
}
