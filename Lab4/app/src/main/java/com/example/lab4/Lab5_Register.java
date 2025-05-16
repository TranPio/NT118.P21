package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab4.api.ApiClient;
import com.example.lab4.api.ApiService;
import com.example.lab4.model.AuthResponse;
import com.example.lab4.model.User;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Lab5_Register extends AppCompatActivity {

    private static final String TAG = "Lab5_Register";

    EditText edtRegUsername, edtRegPassword, edtRegConfirmPassword;
    Button btnRegisterLab5;
    TextView tvBackToLoginLab5;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab5_register);
        Log.d(TAG, "onCreate: Register Activity started");

        edtRegUsername = findViewById(R.id.edtRegUsername_lab5);
        edtRegPassword = findViewById(R.id.edtRegPassword_lab5);
        edtRegConfirmPassword = findViewById(R.id.edtRegConfirmPassword_lab5);
        btnRegisterLab5 = findViewById(R.id.btnRegister_lab5);
        tvBackToLoginLab5 = findViewById(R.id.tvBackToLogin_lab5);

        apiService = ApiClient.getClient().create(ApiService.class);

        btnRegisterLab5.setOnClickListener(view -> registerUserViaApi());

        tvBackToLoginLab5.setOnClickListener(view -> finish());
    }

    private void registerUserViaApi() {
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

        String hashedPassword = getMd5Hash(password);
        if (hashedPassword == null) {
            Toast.makeText(this, "Lỗi mã hóa mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "Registering user: " + username + " with hashed password: " + hashedPassword);

        User userRequest = new User(username, hashedPassword);
        Call<AuthResponse> call = apiService.registerUser(userRequest);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    Toast.makeText(Lab5_Register.this, authResponse.getMessage(), Toast.LENGTH_LONG).show();
                    if ("success".equals(authResponse.getStatus())) {
                        Log.i(TAG, "Registration successful for user: " + username);
                        finish(); // Quay lại màn hình Login
                    } else {
                        Log.w(TAG, "Registration failed: " + authResponse.getMessage());
                        if (authResponse.getMessage() != null && authResponse.getMessage().toLowerCase().contains("username already exists")) {
                            edtRegUsername.setError("Tên đăng nhập đã tồn tại");
                            edtRegUsername.requestFocus();
                        }
                    }
                } else {
                    Toast.makeText(Lab5_Register.this, "Đăng ký thất bại! Lỗi server: " + response.code(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Registration error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(Lab5_Register.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "Registration failure: ", t);
            }
        });
    }

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
            Log.e("MD5", "MD5 algorithm not found", e);
            return null;
        }
    }
}