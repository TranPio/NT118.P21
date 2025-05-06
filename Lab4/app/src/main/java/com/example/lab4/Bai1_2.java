package com.example.lab4; // Thay bằng package của bạn

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager; // Import PreferenceManager

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout; // Import LinearLayout

public class Bai1_2 extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "Bai1_2Activity";
    LinearLayout mainLayout; // Tham chiếu đến LinearLayout gốc của activity_bai1_2.xml
    Button btnStartMySetting;
    SharedPreferences sharedPreferences;

    // Key của preference đã định nghĩa trong preferences.xml
    public static final String KEY_PREF_BG_COLOR_RED = "background_color_red_pref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai1_2);
        Log.d(TAG, "onCreate: Activity started");

        mainLayout = findViewById(R.id.mainLayout_bai1_2); // Ánh xạ LinearLayout
        btnStartMySetting = findViewById(R.id.btnStartMySetting);

        // Lấy SharedPreferences mặc định của ứng dụng
        // Các preference từ PreferenceFragmentCompat sẽ được lưu vào đây
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Đăng ký lắng nghe sự thay đổi của SharedPreferences
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        // Cập nhật màu nền ban đầu khi Activity được tạo
        updateBackgroundColor();

        btnStartMySetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở SettingsActivity
                Intent intent = new Intent(Bai1_2.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Hàm cập nhật màu nền dựa trên giá trị trong SharedPreferences.
     */
    private void updateBackgroundColor() {
        // Đọc giá trị boolean từ preference có key là KEY_PREF_BG_COLOR_RED
        // Giá trị mặc định là false (tức là màu xanh nếu không có cài đặt)
        boolean useRedBackground = sharedPreferences.getBoolean(KEY_PREF_BG_COLOR_RED, false);

        if (useRedBackground) {
            mainLayout.setBackgroundColor(Color.RED);
            Log.d(TAG, "updateBackgroundColor: Set to RED");
        } else {
            mainLayout.setBackgroundColor(Color.BLUE);
            Log.d(TAG, "updateBackgroundColor: Set to BLUE");
        }
    }

    /**
     * Được gọi khi một SharedPreferences thay đổi.
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG, "onSharedPreferenceChanged: Key changed: " + key);
        // Kiểm tra xem key thay đổi có phải là key màu nền không
        if (key.equals(KEY_PREF_BG_COLOR_RED)) {
            updateBackgroundColor(); // Cập nhật lại màu nền
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Activity resumed, updating background color");
        // Có thể gọi updateBackgroundColor() ở đây để đảm bảo màu nền luôn đúng
        // khi quay lại Activity, nhưng onSharedPreferenceChanged đã xử lý việc này
        // nếu preference thay đổi khi SettingsActivity đóng.
        // Tuy nhiên, gọi ở đây để đảm bảo khi Activity mới được tạo hoặc resume
        // mà không có thay đổi preference trước đó, màu vẫn được áp dụng đúng.
        updateBackgroundColor();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Unregistering SharedPreferences listener");
        // Hủy đăng ký lắng nghe để tránh rò rỉ bộ nhớ
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}