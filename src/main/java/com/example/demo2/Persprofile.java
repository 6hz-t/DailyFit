package com.example.demo2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

public class Persprofile extends AppCompatActivity {

    private Spinner spinnerGender;
    private EditText etAge, etHeight, etWeight, etBodyFat;
    private Button btnSave;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persprofile);

        // 初始化视图组件
        spinnerGender = findViewById(R.id.spinner_gender);
        etAge = findViewById(R.id.et_age);
        etHeight = findViewById(R.id.et_height);
        etWeight = findViewById(R.id.et_weight);
        etBodyFat = findViewById(R.id.et_body_fat);
        btnSave = findViewById(R.id.btn_save);

        // 初始化 SharedPreferences
        sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);

        // 设置性别选择器
        List<String> genderList = Arrays.asList("男", "女");
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genderList);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);

        // 加载保存的数据
        loadData();

        // 设置保存按钮的点击事件监听器
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    private void loadData() {
        // 加载性别
        String gender = sharedPreferences.getString("gender", "男");
        int genderPosition = ((ArrayAdapter<String>) spinnerGender.getAdapter()).getPosition(gender);
        spinnerGender.setSelection(genderPosition);

        // 加载年龄
        int age = sharedPreferences.getInt("age", 0);
        if (age > 0) {
            etAge.setText(String.valueOf(age));
        }

        // 加载身高
        float height = sharedPreferences.getFloat("height", 0);
        if (height > 0) {
            etHeight.setText(String.valueOf(height));
        }

        // 加载体重
        float weight = sharedPreferences.getFloat("weight", 0);
        if (weight > 0) {
            etWeight.setText(String.valueOf(weight));
        }

        // 加载体脂率
        float bodyFat = sharedPreferences.getFloat("body_fat", 0);
        if (bodyFat > 0) {
            etBodyFat.setText(String.valueOf(bodyFat));
        }
    }

    private void saveData() {
        // 获取输入的数据
        String gender = spinnerGender.getSelectedItem().toString();
        int age = Integer.parseInt(etAge.getText().toString().isEmpty() ? "0" : etAge.getText().toString());
        float height = Float.parseFloat(etHeight.getText().toString().isEmpty() ? "0" : etHeight.getText().toString());
        float weight = Float.parseFloat(etWeight.getText().toString().isEmpty() ? "0" : etWeight.getText().toString());
        float bodyFat = Float.parseFloat(etBodyFat.getText().toString().isEmpty() ? "0" : etBodyFat.getText().toString());

        // 保存数据到 SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("gender", gender);
        editor.putInt("age", age);
        editor.putFloat("height", height);
        editor.putFloat("weight", weight);
        editor.putFloat("body_fat", bodyFat);
        editor.apply();
    }
}