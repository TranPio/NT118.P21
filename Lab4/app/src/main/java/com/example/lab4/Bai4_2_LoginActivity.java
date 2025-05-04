package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

// Import hàm MD5 (giả sử nó nằm trong Bai4_2_RegisterActivity hoặc AppDatabaseHelper)
// Nếu để trong RegisterActivity:
// import static com.example.lab4.Bai4_2_RegisterActivity.getMd5Hash;
// Nếu muốn để hàm dùng chung, có thể tạo lớp Util riêng hoặc để trong AppDatabaseHelper

import java.math.BigInteger; // Cần cho MD5 nếu copy hàm vào đây
import java.security.MessageDigest; // Cần cho MD5 nếu copy hàm vào đây
import java.security.NoSuchAlgorithmException; // Cần cho MD5 nếu copy hàm vào đây


public class Bai4_2_LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity_SQLite";

    EditText edtLoginUsername, edtLoginPassword;
    Button btnLoginConfirm;
    TextView tvGoToRegister;
    DatabaseHelper dbHelper; // Sử dụng AppDatabaseHelper (hoặc tên bạn đặt)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai4_2_login);
        Log.d(TAG, "onCreate: Login Activity started");

        edtLoginUsername = findViewById(R.id.edtLoginUsername);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);
        btnLoginConfirm = findViewById(R.id.btnLoginConfirm);
        tvGoToRegister = findViewById(R.id.tvGoToRegister);

        dbHelper = new DatabaseHelper(this); // Khởi tạo Helper

        btnLoginConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

        tvGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang màn hình Đăng ký
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

        String hashedInputPassword = getMd5Hash(password); // Mã hóa mật khẩu nhập vào
        if (hashedInputPassword == null) {
            Toast.makeText(this, "Lỗi mã hóa mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "performLogin: Input password hashed (MD5): " + hashedInputPassword);

        Cursor cursor = null;
        try {
            cursor = dbHelper.getUser(username); // Lấy thông tin user

            if (cursor != null && cursor.moveToFirst()) {
                Log.d(TAG, "performLogin: User found: " + username);
                int passwordColIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.USER_COL_PASSWORD);
                String storedHashedPassword = cursor.getString(passwordColIndex);
                Log.d(TAG, "performLogin: Stored hashed password: " + storedHashedPassword);

                if (hashedInputPassword.equals(storedHashedPassword)) {
                    Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "performLogin: Login successful for user: " + username);
                    // TODO: Chuyển sang màn hình chính sau khi đăng nhập
                    // Ví dụ:
                    // Intent mainIntent = new Intent(Bai4_2_LoginActivity.this, MainActivity.class);
                    // startActivity(mainIntent);
                    // finish(); // Đóng màn hình login
                } else {
                    Toast.makeText(this, "Sai mật khẩu!", Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "performLogin: Incorrect password for user: " + username);
                    edtLoginPassword.requestFocus();
                }
            } else {
                Toast.makeText(this, "Tên đăng nhập không tồn tại!", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "performLogin: Username not found: " + username);
                edtLoginUsername.requestFocus();
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