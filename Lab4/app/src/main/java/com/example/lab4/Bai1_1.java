package com.example.lab4; // Thay bằng package của bạn

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Bai1_1 extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    CheckBox chkRememberMe;
    Button btnConfirm;
    SharedPreferences sharedPreferences;

    private static final String PREF_NAME = "LoginPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_REMEMBER = "remember";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai1_1);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        chkRememberMe = findViewById(R.id.chkRememberMe);
        btnConfirm = findViewById(R.id.btnConfirm);

        // Khởi tạo SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // Load thông tin đăng nhập đã lưu (nếu có)
        loadLoginInfo();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                // Thực hiện kiểm tra đăng nhập ở đây (ví dụ đơn giản)
                if (!username.isEmpty() && !password.isEmpty()) {
                    // Nếu đăng nhập thành công
                    Toast.makeText(Bai1_1.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                    // Lưu thông tin nếu người dùng chọn "Nhớ đăng nhập"
                    if (chkRememberMe.isChecked()) {
                        saveLoginInfo(username, password, true);
                    } else {
                        // Xóa thông tin đã lưu nếu không chọn "Nhớ đăng nhập"
                        clearLoginInfo();
                    }

                    // Chuyển sang màn hình khác (nếu cần)
                    // Intent intent = new Intent(Bai1_1.this, MainActivity.class);
                    // startActivity(intent);

                } else {
                    Toast.makeText(Bai1_1.this, "Vui lòng nhập username và password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveLoginInfo(String username, String password, boolean remember) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password); // Lưu ý: không nên lưu mật khẩu dạng rõ!
        editor.putBoolean(KEY_REMEMBER, remember);
        editor.apply(); // Lưu bất đồng bộ
    }

    private void loadLoginInfo() {
        boolean remember = sharedPreferences.getBoolean(KEY_REMEMBER, false);
        if (remember) {
            String savedUsername = sharedPreferences.getString(KEY_USERNAME, "");
            String savedPassword = sharedPreferences.getString(KEY_PASSWORD, ""); // Không nên làm thế này trong thực tế
            edtUsername.setText(savedUsername);
            edtPassword.setText(savedPassword);
            chkRememberMe.setChecked(true);
        }
    }

    private void clearLoginInfo() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_USERNAME); // hoặc editor.clear(); để xóa hết
        editor.remove(KEY_PASSWORD);
        editor.remove(KEY_REMEMBER);
        editor.apply();
    }
}