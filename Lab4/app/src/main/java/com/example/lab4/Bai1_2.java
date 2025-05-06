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
import android.widget.LinearLayout;

public class Bai1_2 extends AppCompatActivity {

    private static final String TAG = "Bai1_2Activity";
    LinearLayout mainLayout; // Layout gốc để đổi màu nền
    Button btnStartMySetting;

    // Key của preference đã định nghĩa trong preferences.xml
    public static final String KEY_PREF_BG_COLOR_RED = "background_color_red_pref";

    // Mã màu (bạn có thể dùng mã màu trong Lab4.pdf hoặc mã khác)
    private final int COLOR_RED_FROM_LAB_PDF = Color.RED; // Hoặc Color.parseColor("#FF0000")
    private final int COLOR_BLUE_FROM_LAB_PDF = Color.BLUE; // Hoặc Color.parseColor("#0000FF")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai1_2);
        Log.d(TAG, "onCreate: Activity started");

        mainLayout = findViewById(R.id.mainLayout_bai1_2);
        btnStartMySetting = findViewById(R.id.btnStartMySetting);

        btnStartMySetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở SettingsActivity
                Intent intent = new Intent(Bai1_2.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Activity resumed, updating background color");
        // Mỗi khi Activity này được focus lại (ví dụ sau khi SettingsActivity đóng),
        // gọi hàm cập nhật màu nền.
        updateBackgroundColor();
    }

    /**
     * Đọc giá trị từ SharedPreferences và cập nhật màu nền của mainLayout.
     */
    private void updateBackgroundColor() {
        // Lấy đối tượng SharedPreferences mặc định của ứng dụng
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Đọc giá trị boolean từ preference có key là KEY_PREF_BG_COLOR_RED
        // Giá trị mặc định là false (tức là màu xanh) nếu chưa có cài đặt
        boolean isCheckedForRed = prefs.getBoolean(KEY_PREF_BG_COLOR_RED, false);

        // Đặt màu nền dựa trên giá trị isCheckedForRed
        if (isCheckedForRed) {
            mainLayout.setBackgroundColor(COLOR_RED_FROM_LAB_PDF); // Đặt màu đỏ
            Log.d(TAG, "updateBackgroundColor: Background set to RED");
        } else {
            mainLayout.setBackgroundColor(COLOR_BLUE_FROM_LAB_PDF); // Đặt màu xanh
            Log.d(TAG, "updateBackgroundColor: Background set to BLUE");
        }
    }

    // Lưu ý: Trong luồng này, `onSharedPreferenceChanged` không thực sự cần thiết
    // vì `onResume` đã xử lý việc cập nhật khi quay lại từ SettingsActivity.
    // Tuy nhiên, nếu bạn muốn màu nền thay đổi *ngay lập tức* ở Bai1_2
    // trong trường hợp Bai1_2 và SettingsActivity hiển thị cùng lúc (ví dụ trên tablet dạng split-screen),
    // thì bạn mới cần implement OnSharedPreferenceChangeListener.
    // Với yêu cầu của Lab4.pdf (mở, cài đặt, quay lại), onResume là đủ.
}