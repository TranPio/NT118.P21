package com.example.lab4;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Hiển thị PreferenceFragmentCompat
        // Sử dụng android.R.id.content làm container mặc định của Activity
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, new MySettingsFragment())
                    .commit();
        }

        // Thêm nút "Up" (Back) trên ActionBar (tùy chọn)
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("My Setting.."); // Đặt tiêu đề cho màn hình Settings
        }
    }

    // Xử lý sự kiện khi nhấn nút "Up" trên ActionBar
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Đóng SettingsActivity và quay lại màn hình trước
        return true;
    }

    // Inner class cho PreferenceFragmentCompat
    public static class MySettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            // Load các preferences từ file XML
            setPreferencesFromResource(R.xml.preferences, rootKey);
        }
    }
}