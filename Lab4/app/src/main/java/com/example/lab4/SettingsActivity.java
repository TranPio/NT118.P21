package com.example.lab4;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Không cần setContentView nếu chỉ hiển thị Fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new SettingsFragment()) // Dùng android.R.id.content làm container
                .commit();
        // Thêm nút back trên ActionBar (optional)
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("My Setting.."); // Đặt tiêu đề
        }
    }

    // Xử lý nút back trên ActionBar (optional)
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    // Inner class cho PreferenceFragmentCompat
    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);
        }
    }
}