package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class Bai1_2 extends AppCompatActivity {

    private static final String TAG = "Bai1_2Activity";
    // LinearLayout mainLayout;
    View colorDisplayArea; // *** Tham chiếu đến View hiển thị màu ***
    Button btnStartMySetting;

    // Key của preference đã định nghĩa trong preferences.xml
    public static final String KEY_PREF_BG_COLOR_RED = "background_color_red_pref";

    // Mã màu
    private final int COLOR_RED_BG = Color.RED;
    private final int COLOR_BLUE_BG = Color.BLUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai1_2);
        Log.d(TAG, "onCreate: Activity started");

        // Ánh xạ các View
        // mainLayout = findViewById(R.id.rootLayout_bai1_2); // Không cần nữa
        colorDisplayArea = findViewById(R.id.colorDisplayArea); // *** Ánh xạ View màu ***
        btnStartMySetting = findViewById(R.id.btnStartMySetting);

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
        Log.d(TAG, "onResume: Activity resumed, updating color display area");
        // Cập nhật màu cho vùng hiển thị mỗi khi quay lại Activity
        updateBackgroundColor();
    }

    /**
     * Đọc giá trị từ SharedPreferences và cập nhật màu nền cho colorDisplayArea.
     */
    private void updateBackgroundColor() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isCheckedForRed = prefs.getBoolean(KEY_PREF_BG_COLOR_RED, false); // Mặc định là false (xanh)

        // *** THAY ĐỔI Ở ĐÂY: Đặt màu cho colorDisplayArea ***
        if (isCheckedForRed) {
            colorDisplayArea.setBackgroundColor(COLOR_RED_BG); // Đặt màu đỏ
            Log.d(TAG, "updateBackgroundColor: Color area set to RED");
        } else {

            colorDisplayArea.setBackgroundColor(COLOR_BLUE_BG); // Đặt màu xanh
            Log.d(TAG, "updateBackgroundColor: Color area set to BLUE");

        }
    }
}