package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context; // Thêm import Context
import android.content.Intent;
import android.content.SharedPreferences; // Thêm import SharedPreferences
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox; // Thêm import CheckBox
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Bai4_2_LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity_SQLite";

    EditText edtLoginUsername, edtLoginPassword;
    Button btnLoginConfirm;
    TextView tvGoToRegister;
    CheckBox chkLoginRememberMe; // *** KHAI BÁO CHECKBOX ***
    DatabaseHelper dbHelper;

    // *** THÊM CÁC HẰNG SỐ CHO SHAREDPREFERENCES ***
    private static final String PREF_LOGIN_REMEMBER = "LoginRememberPrefs"; // Tên file Prefs mới
    private static final String KEY_REMEMBER_CHECKED = "login_remember_checked";
    private static final String KEY_REMEMBER_USER = "login_remember_user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai4_2_login);
        Log.d(TAG, "onCreate: Login Activity started");

        edtLoginUsername = findViewById(R.id.edtLoginUsername);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);
        btnLoginConfirm = findViewById(R.id.btnLoginConfirm);
        tvGoToRegister = findViewById(R.id.tvGoToRegister);
        chkLoginRememberMe = findViewById(R.id.chkLoginRememberMe); // *** ÁNH XẠ CHECKBOX ***

        dbHelper = new DatabaseHelper(this);

        // *** LOAD TRẠNG THÁI NHỚ ĐĂNG NHẬP KHI KHỞI ĐỘNG ***
        loadRememberMeStatus();

        btnLoginConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

        tvGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Bai4_2_LoginActivity.this, Bai4_2_RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void performLogin() {
        Log.d(TAG, "performLogin: Attempting login...");
        String username = edtLoginUsername.getText().toString().trim();
        String password = edtLoginPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập username và password", Toast.LENGTH_SHORT).show();
            return;
        }

        String hashedInputPassword = getMd5Hash(password);
        if (hashedInputPassword == null) {
            Toast.makeText(this, "Lỗi mã hóa mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "performLogin: Input password hashed (MD5): " + hashedInputPassword);

        Cursor cursor = null;
        try {
            cursor = dbHelper.getUser(username);

            if (cursor != null && cursor.moveToFirst()) {
                Log.d(TAG, "performLogin: User found: " + username);
                int passwordColIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.USER_COL_PASSWORD);
                String storedHashedPassword = cursor.getString(passwordColIndex);
                Log.d(TAG, "performLogin: Stored hashed password: " + storedHashedPassword);

                if (hashedInputPassword.equals(storedHashedPassword)) {
                    // --- Đăng nhập thành công ---
                    Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "performLogin: Login successful for user: " + username);

                    // *** LƯU TRẠNG THÁI NHỚ ĐĂNG NHẬP ***
                    saveRememberMeStatus(username, chkLoginRememberMe.isChecked());

                    // TODO: Chuyển sang màn hình chính
                    // Intent mainIntent = new Intent(Bai4_2_LoginActivity.this, MainActivity.class);
                    // startActivity(mainIntent);
                    // finish();

                } else {
                    // --- Sai mật khẩu ---
                    Toast.makeText(this, "Sai mật khẩu!", Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "performLogin: Incorrect password for user: " + username);
                    edtLoginPassword.requestFocus();
                    // *** XÓA TRẠNG THÁI NHỚ ĐĂNG NHẬP NẾU LOGIN SAI ***
                    // saveRememberMeStatus("", false); // Hoặc chỉ xóa username
                }

            } else {
                // --- Không tìm thấy user ---
                Toast.makeText(this, "Tên đăng nhập không tồn tại!", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "performLogin: Username not found: " + username);
                edtLoginUsername.requestFocus();
                // *** XÓA TRẠNG THÁI NHỚ ĐĂNG NHẬP NẾU USER KHÔNG TỒN TẠI ***
                // saveRememberMeStatus("", false); // Hoặc chỉ xóa username
            }

        } catch (Exception e) {
            Log.e(TAG, "performLogin: Error during login process", e);
            Toast.makeText(this, "Lỗi trong quá trình đăng nhập!", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                Log.d(TAG, "performLogin: Cursor closed.");
            }
        }
    }

    // *** THÊM HÀM LƯU TRẠNG THÁI NHỚ ĐĂNG NHẬP ***
    private void saveRememberMeStatus(String username, boolean remember) {
        // Sử dụng tên file SharedPreferences riêng cho chức năng này
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_LOGIN_REMEMBER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_REMEMBER_CHECKED, remember);
        if (remember) {
            editor.putString(KEY_REMEMBER_USER, username); // Lưu username nếu chọn nhớ
            Log.d(TAG, "saveRememberMeStatus: Saved username: " + username);
        } else {
            editor.remove(KEY_REMEMBER_USER); // Xóa username đã lưu nếu không chọn nhớ
            Log.d(TAG, "saveRememberMeStatus: Cleared saved username.");
        }
        editor.apply(); // Lưu thay đổi
    }

    // *** THÊM HÀM LOAD TRẠNG THÁI NHỚ ĐĂNG NHẬP ***
    private void loadRememberMeStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_LOGIN_REMEMBER, Context.MODE_PRIVATE);
        boolean rememberChecked = sharedPreferences.getBoolean(KEY_REMEMBER_CHECKED, false); // Mặc định là false
        chkLoginRememberMe.setChecked(rememberChecked); // Đặt trạng thái checkbox

        if (rememberChecked) {
            String savedUsername = sharedPreferences.getString(KEY_REMEMBER_USER, ""); // Lấy username đã lưu
            edtLoginUsername.setText(savedUsername); // Điền username vào ô nhập
            // Focus vào ô mật khẩu nếu username đã được điền
            if (!savedUsername.isEmpty()) {
                edtLoginPassword.requestFocus();
            }
            Log.d(TAG, "loadRememberMeStatus: Loaded username: " + savedUsername);
        } else {
            Log.d(TAG, "loadRememberMeStatus: No username loaded.");
        }
    }


    // Hàm mã hóa MD5 (Copy từ Bai4_2_RegisterActivity hoặc tạo lớp Util riêng)
    public static String getMd5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            Log.e("MD5_Login", "MD5 algorithm not found", e);
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        if (dbHelper != null) {
            dbHelper.close();
            Log.d(TAG, "onDestroy: DatabaseHelper closed.");
        }
        super.onDestroy();
    }
}