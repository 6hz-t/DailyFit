package com.example.demo2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demo2.SportRecordDBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {

    private EditText etDate, etSportTime;
    private Spinner spinnerSportType;
    private Button btnSelectImage, btnSaveRecord;
    private ImageView ivSelectedImage;
    private String selectedImagePath;
    private SportRecordDBHelper dbHelper;
    private final Calendar myCalendar = Calendar.getInstance();

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        etDate = findViewById(R.id.et_date);
        etSportTime = findViewById(R.id.et_sport_time);
        spinnerSportType = findViewById(R.id.spinner_sport_type);
        btnSelectImage = findViewById(R.id.btn_select_image);
        btnSaveRecord = findViewById(R.id.btn_save_record);
        ivSelectedImage = findViewById(R.id.iv_selected_image);
        dbHelper = new SportRecordDBHelper(this);

        // 初始化运动类型列表
        List<String> sportTypes = new ArrayList<>();
        // 有氧类
        sportTypes.add("慢跑");
        sportTypes.add("快跑");
        sportTypes.add("游泳（自由泳）");
        sportTypes.add("游泳（蛙泳）");
        sportTypes.add("游泳（仰泳）");
        sportTypes.add("游泳（蝶泳）");
        sportTypes.add("骑自行车（含室内动感单车）");
        sportTypes.add("跳绳");
        sportTypes.add("健身操");
        sportTypes.add("有氧舞蹈");
        sportTypes.add("踢毽子");
        sportTypes.add("爬楼梯");
        sportTypes.add("竞走");
        sportTypes.add("滑冰");
        sportTypes.add("滑雪");
        sportTypes.add("划船机训练");
        sportTypes.add("椭圆机运动");
        sportTypes.add("登山");
        // 力量训练类
        sportTypes.add("杠铃深蹲");
        sportTypes.add("哑铃深蹲");
        sportTypes.add("箭步蹲");
        sportTypes.add("臀桥");
        sportTypes.add("硬拉");
        sportTypes.add("卧推（含哑铃卧推）");
        sportTypes.add("卧推（上斜卧推）");
        sportTypes.add("卧推（下斜卧推）");
        sportTypes.add("引体向上（含反握引体向上）");
        sportTypes.add("引体向上（颈后引体向上）");
        sportTypes.add("俯卧撑（窄距俯卧撑）");
        sportTypes.add("俯卧撑（宽距俯卧撑）");
        sportTypes.add("哑铃肩推");
        sportTypes.add("侧平举");
        sportTypes.add("前平举");
        sportTypes.add("俯身飞鸟");
        sportTypes.add("哑铃弯举（集中弯举）");
        sportTypes.add("杠铃弯举");
        sportTypes.add("三头肌下压");
        sportTypes.add("仰卧臂屈伸");
        sportTypes.add("卷腹（反向卷腹）");
        sportTypes.add("卷腹（侧卷腹）");
        sportTypes.add("平板支撑");
        sportTypes.add("俄罗斯转体");
        sportTypes.add("仰卧抬腿");

        // 设置适配器
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sportTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSportType.setAdapter(adapter);

        // 日期选择点击事件
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // 图片选择点击事件
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "选择图片"), PICK_IMAGE_REQUEST);
            }
        });

        // 保存记录点击事件
        btnSaveRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = etDate.getText().toString();
                String time = etSportTime.getText().toString();
                String sportType = spinnerSportType.getSelectedItem().toString();

                if (date.isEmpty() || time.isEmpty()) {
                    Toast.makeText(MainActivity2.this, "请填写日期和运动时间", Toast.LENGTH_SHORT).show();
                    return;
                }

                saveRecordToDB(date, Integer.parseInt(time), sportType, selectedImagePath);
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        new DatePickerDialog(MainActivity2.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CHINA);
        etDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            selectedImagePath = getRealPathFromURI(uri);
            ivSelectedImage.setImageURI(uri);
            ivSelectedImage.setVisibility(View.VISIBLE);
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String result = cursor.getString(column_index);
            cursor.close();
            return result;
        }
        return null;
    }

    private void saveRecordToDB(String date, int time, String sportType, String imagePath) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String insertQuery = "INSERT INTO " + SportRecordDBHelper.TABLE_NAME + " (" +
                SportRecordDBHelper.COLUMN_DATE + ", " +
                SportRecordDBHelper.COLUMN_TIME + ", " +
                SportRecordDBHelper.COLUMN_SPORT_TYPE + ", " +
                SportRecordDBHelper.COLUMN_IMAGE_PATH + ") VALUES ('" +
                date + "', " +
                time + ", '" +
                sportType + "', '" +
                imagePath + "');";
        try {
            db.execSQL(insertQuery);
            Log.d("MainActivity2", "数据插入成功");
        } catch (Exception e) {
            Log.e("MainActivity2", "数据插入失败：" + e.getMessage());
        }
        db.close();
        Toast.makeText(this, "保存记录成功", Toast.LENGTH_SHORT).show();
    }
}