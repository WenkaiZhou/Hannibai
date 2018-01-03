package com.kevin.hannibai.sample;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by zhouwenkai on 2017/9/15.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class Employee {
    public String name;
    public int age;
    public long NO;
    public float salary;

    @Override
    public String toString() {
        return String.format("[name]: %s, [age]: %d, [NO.]: %s, [salary]: %s", name, age, NO, salary);
    }
}
