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

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kevin.hannibai.Hannibai;
import com.kevin.hannibai.annotation.Apply;
import com.kevin.hannibai.annotation.Commit;

public class MainActivity extends Activity {

    private TextView mTvNameContent;
    private TextView mTvAgeContent;
    private TextView mTvNoContent;
    private TextView mTvSalaryContent;
    private TextView mTvEmployeeContent;
    private EditText mEtName;
    private EditText mEtAge;
    private EditText mEtNo;
    private EditText mEtSalary;

    TestPreferenceHandle testPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvNameContent = (TextView) this.findViewById(R.id.tv_name_content);
        mTvAgeContent = (TextView) this.findViewById(R.id.tv_age_content);
        mTvNoContent = (TextView) this.findViewById(R.id.tv_no_content);
        mTvSalaryContent = (TextView) this.findViewById(R.id.tv_salary_content);
        mTvEmployeeContent = (TextView) this.findViewById(R.id.tv_employee_content);

        mEtName = (EditText) this.findViewById(R.id.et_name);
        mEtAge = (EditText) this.findViewById(R.id.et_age);
        mEtNo = (EditText) this.findViewById(R.id.et_no);
        mEtSalary = (EditText) this.findViewById(R.id.et_salary);

        testPreference = Hannibai.create(TestPreferenceHandle.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshView();
    }


    public void onSetNameClick(View view) {
        String value = mEtName.getText().toString();
        testPreference.setName(value);
        refreshNameView();
    }

    public void onGetNameClick(View view) {
        String userName = testPreference.getName();
        showToast("get name: " + userName);
        refreshNameView();
    }

    public void onRemoveNameClick(View view) {
        testPreference.removeName();
        refreshNameView();
        showToast("remove name");
    }

    public void onSetAgeClick(View view) {
        String value = mEtAge.getText().toString();
        if (!TextUtils.isEmpty(value)) {
            testPreference.setAge(Integer.parseInt(value));
        }
        refreshAgeView();
    }

    public void onGetAgeClick(View view) {
        int age = testPreference.getAge();
        showToast("get age: " + age);
        refreshAgeView();
    }

    public void onRemoveAgeClick(View view) {
        testPreference.removeAge();
        refreshAgeView();
        showToast("remove age");
    }

    public void onSetNoClick(View view) {
        String value = mEtNo.getText().toString();
        if (!TextUtils.isEmpty(value)) {
            testPreference.setNo(Long.parseLong(value));
        }
        refreshNoView();
    }

    public void onGetNoClick(View view) {
        long no = testPreference.getNo();
        showToast("get NO.: " + no);
        refreshNoView();
    }

    public void onRemoveNoClick(View view) {
        testPreference.removeNo();
        refreshNoView();
        showToast("remove NO.");
    }

    public void onSetSalaryClick(View view) {
        String value = mEtSalary.getText().toString();
        if (!TextUtils.isEmpty(value)) {
            testPreference.setSalary(Float.parseFloat(value));
        }
        refreshSalaryView();
    }

    public void onGetSalaryClick(View view) {
        float salary = testPreference.getSalary();
        showToast("get salary: " + salary);
        refreshSalaryView();
    }

    public void onRemoveSalaryClick(View view) {
        testPreference.removeSalary();
        refreshSalaryView();
        showToast("remove salary");
    }

    public void onSetEmployeeClick(View view) {
        Employee employee = new Employee();
        employee.name = testPreference.getName();
        employee.age = testPreference.getAge();
        employee.NO = testPreference.getNo();
        employee.salary = testPreference.getSalary();
        testPreference.setEmployee(employee);
        refreshEmployeeView();
    }

    public void onGetEmployeeClick(View view) {
        Employee employee = testPreference.getEmployee();
        refreshSalaryView();
        showToast("get employee: " + employee);
    }

    public void onRemoveEmployeeClick(View view) {
        testPreference.removeEmployee();
        refreshSalaryView();
        showToast("remove employee");
    }

    private void showToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    private void refreshView() {
        refreshNameView();
        refreshAgeView();
        refreshNoView();
        refreshSalaryView();
        refreshEmployeeView();
    }

    private void refreshNameView() {
        setContent(mTvNameContent, "public String name;",
                "name", String.class.getSimpleName(), "\"\"", testPreference.getName(), Apply.class.getSimpleName(), "永远不过期");
    }

    private void refreshAgeView() {
        setContent(mTvAgeContent, "@Commit\n@DefInt(18)\npublic int age;",
                "age", int.class.getSimpleName(), 18, testPreference.getAge(), Commit.class.getSimpleName(), "永远不过期");
    }

    private void refreshNoView() {
        setContent(mTvNoContent, "@DefLong(10000L)\n@Expire(value = 1, unit = Expire.Unit.MINUTES)\npublic long no;",
                "NO.", long.class.getSimpleName(), 10000L, testPreference.getNo(), Apply.class.getSimpleName(), "1分钟");
    }

    private void refreshSalaryView() {
        setContent(mTvSalaryContent, "@Apply\n@DefFloat(25000.23F)\n@Expire(value = 20, unit = Expire.Unit.SECONDS, update = true)\npublic float salary;",
                "salary", float.class.getSimpleName(), 25000.23F, testPreference.getSalary(), Apply.class.getSimpleName(), "20秒, 更新后会重新置计时");
    }

    private void refreshEmployeeView() {
        Employee employee = testPreference.getEmployee();
        setContent(mTvEmployeeContent, "public Employee employee;",
                "salary", Employee.class.getSimpleName(), "null", employee == null ? "" : employee.toString(), Apply.class.getSimpleName(), "永远不过期");
    }

    private void setContent(TextView textView, String code, String key, String type, Object defaultValue, Object value, String submitType, String expire) {
        textView.setText(String.format("```\n%s\n```\n%s: 类型是 %s, 默认值 {%s}, 值 {%s}, 提交类型 %s, 过期时间 %s.", code, key, type, defaultValue, value, submitType, expire));
    }
}