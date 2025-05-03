package com.example.lab4; // Thay bằng package của bạn

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Bai1_2 extends AppCompatActivity {

    LinearLayout mainLayout;
    Button btnStartMySetting;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai1_2);

        mainLayout = findViewById(R.id.mainLayout);
        btnStartMySetting = findViewById(R.id.btnStartMySetting);

        // Lấy SharedPreferences mặc định của ứng dụng
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        btnStartMySetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Bai1_2.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật màu nền mỗi khi Activity quay lại foreground
        updateBackgroundColor();
    }

    private void updateBackgroundColor() {
        // Đọc giá trị từ CheckBoxPreference
        boolean useRedBackground = sharedPreferences.getBoolean("background_color_pref", false); // false là giá trị mặc định

        if (useRedBackground) {
            mainLayout.setBackgroundColor(Color.RED);
        } else {
            mainLayout.setBackgroundColor(Color.BLUE);
        }
    }
}