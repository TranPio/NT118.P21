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
    View colorDisplayArea; // View hiển thị màu
    Button btnStartMySetting;

    // Key của preference
    public static final String KEY_PREF_BG_COLOR_RED = "background_color_red_pref";

    // Mã màu
    private final int COLOR_RED_BG = Color.RED;
    private final int COLOR_BLUE_BG = Color.BLUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai1_2);
        Log.d(TAG, "onCreate: Activity started");

        colorDisplayArea = findViewById(R.id.colorDisplayArea);
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
        // Cập nhật màu khi quay lại Activity
        updateBackgroundColor();
    }

    /**
     * Đọc giá trị từ SharedPreferences và cập nhật màu nền cho colorDisplayArea.
     * Nếu chưa có cài đặt, nền sẽ là màu trắng.
     */
    private void updateBackgroundColor() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // *** KIỂM TRA XEM KEY CÀI ĐẶT ĐÃ TỒN TẠI HAY CHƯA ***
        if (prefs.contains(KEY_PREF_BG_COLOR_RED)) {
            // Key đã tồn tại (nghĩa là người dùng đã vào Setting ít nhất 1 lần)
            boolean isCheckedForRed = prefs.getBoolean(KEY_PREF_BG_COLOR_RED, false); // Lấy giá trị đã lưu

            if (isCheckedForRed) {
                colorDisplayArea.setBackgroundColor(COLOR_RED_BG); // Checked -> Màu đỏ
                Log.d(TAG, "updateBackgroundColor: Pref exists. Set to RED");
            } else {
                colorDisplayArea.setBackgroundColor(COLOR_BLUE_BG); // Unchecked -> Màu xanh
                Log.d(TAG, "updateBackgroundColor: Pref exists. Set to BLUE");
            }
        } else {
            // Key chưa tồn tại (lần đầu chạy hoặc chưa vào Setting)
            colorDisplayArea.setBackgroundColor(Color.WHITE); // Đặt màu trắng
            Log.d(TAG, "updateBackgroundColor: Pref does not exist. Set to WHITE");
        }
    }
}