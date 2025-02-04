package com.example.demo2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SportRecordAdapter adapter;
    private List<SportRecord> sportRecordList;
    private SportRecordDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 原有记录一下按钮逻辑
        Button bt = findViewById(R.id.but);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.setClass(MainActivity.this, MainActivity2.class);
                startActivity(in);
            }
        });

        // 原有个人主页浮动按钮逻辑
        FloatingActionButton fabProfile = findViewById(R.id.fab_profile);
        fabProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 这里假设个人主页的 Activity 类名为 Persprofile
                Intent intent = new Intent(MainActivity.this, Persprofile.class);
                startActivity(intent);
            }
        });

        // 初始化 RecyclerView 相关组件
        recyclerView = findViewById(R.id.recyclerView_sport_records);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sportRecordList = new ArrayList<>();
        adapter = new SportRecordAdapter(sportRecordList);
        recyclerView.setAdapter(adapter);

        dbHelper = new SportRecordDBHelper(this);
        loadSportRecords();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 清空原有数据
        sportRecordList.clear();
        loadSportRecords();
    }

    private void loadSportRecords() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();
            String[] projection = {
                    SportRecordDBHelper.COLUMN_DATE,
                    SportRecordDBHelper.COLUMN_TIME,
                    SportRecordDBHelper.COLUMN_SPORT_TYPE
            };
            cursor = db.query(
                    SportRecordDBHelper.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            while (cursor.moveToNext()) {
                int dateIndex = cursor.getColumnIndex(SportRecordDBHelper.COLUMN_DATE);
                int timeIndex = cursor.getColumnIndex(SportRecordDBHelper.COLUMN_TIME);
                int sportTypeIndex = cursor.getColumnIndex(SportRecordDBHelper.COLUMN_SPORT_TYPE);

                if (dateIndex != -1 && timeIndex != -1 && sportTypeIndex != -1) {
                    String date = cursor.getString(dateIndex);
                    int time = cursor.getInt(timeIndex);
                    String sportType = cursor.getString(sportTypeIndex);
                    sportRecordList.add(new SportRecord(date, time, sportType));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "加载运动记录失败", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        // 按日期降序排序（新的在上）
        Collections.sort(sportRecordList, new Comparator<SportRecord>() {
            @Override
            public int compare(SportRecord r1, SportRecord r2) {
                return r2.getDate().compareTo(r1.getDate());
            }
        });

        adapter.notifyDataSetChanged();

        if (sportRecordList.isEmpty()) {
            Toast.makeText(this, "暂无运动记录", Toast.LENGTH_SHORT).show();
        }
    }
}