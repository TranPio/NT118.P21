package com.example.lab4;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Không cần file layout XML riêng cho SettingsActivity nếu chỉ dùng Fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, new MySettingsFragment())
                    .commit();
        }

        // Hiển thị nút Up (Back) trên ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("My Setting..");
        }
    }

    // Xử lý khi nút Up được nhấn
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Đóng SettingsActivity và quay lại màn hình trước đó
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Inner class cho PreferenceFragmentCompat
    public static class MySettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            // Load các preferences từ file XML
            setPreferencesFromResource(R.xml.preferences, rootKey);
        }
        // PreferenceFragmentCompat tự động lưu giá trị vào SharedPreferences khi người dùng thay đổi.
        // Không cần hiển thị Toast true/false ở đây.
    }
}