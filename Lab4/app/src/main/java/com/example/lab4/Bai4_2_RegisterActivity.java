package com.example.lab4;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Bai4_2_RegisterActivity extends AppCompatActivity { // Đổi tên lớp

    private static final String TAG = "RegisterActivity_SQLite"; // Đổi TAG

    EditText edtRegUsername, edtRegPassword, edtRegConfirmPassword;
    Button btnRegister;
    TextView tvBackToLogin;
    DatabaseHelper dbHelper; // Sử dụng AppDatabaseHelper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Đảm bảo tên layout khớp với file XML bạn tạo
        setContentView(R.layout.activity_bai4_2_register);
        Log.d(TAG, "onCreate: Register Activity started");

        edtRegUsername = findViewById(R.id.edtRegUsername);
        edtRegPassword = findViewById(R.id.edtRegPassword);
        edtRegConfirmPassword = findViewById(R.id.edtRegConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        dbHelper = new DatabaseHelper(this); // Khởi tạo Helper

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        tvBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng activity này để quay lại màn hình trước (Login)
            }
        });
    }

    private void registerUser() {
        Log.d(TAG, "registerUser: Attempting registration...");
        String username = edtRegUsername.getText().toString().trim();
        String password = edtRegPassword.getText().toString().trim();
        String confirmPassword = edtRegConfirmPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            edtRegConfirmPassword.setError("Mật khẩu không khớp");
            edtRegConfirmPassword.requestFocus();
            return;
        }

        String hashedPassword = getMd5Hash(password); // Mã hóa mật khẩu
        if (hashedPassword == null) {
            Toast.makeText(this, "Lỗi mã hóa mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "registerUser: Password hashed (MD5): " + hashedPassword);

        long result = dbHelper.registerUser(username, hashedPassword); // Gọi Helper để đăng ký

        if (result == -1) {
            Toast.makeText(this, "Đăng ký thất bại! Tên đăng nhập đã tồn tại.", Toast.LENGTH_LONG).show();
            edtRegUsername.setError("Tên đăng nhập đã tồn tại");
            edtRegUsername.requestFocus();
        } else if (result == -2) {
            Toast.makeText(this, "Đăng ký thất bại! Lỗi không xác định.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "registerUser: Registration successful for user: " + username + ", ID: " + result);
            // Tự động quay lại màn hình Đăng nhập sau khi đăng ký thành công
            finish();
        }
    }

    // Hàm mã hóa MD5
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
            Log.e("MD5_Register", "MD5 algorithm not found", e);
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