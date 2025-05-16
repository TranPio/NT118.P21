package com.example.lab4;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab4.R; // Đảm bảo R đúng package
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

public class Lab5_Login extends AppCompatActivity {

    private static final String TAG = "Lab5_Login";

    EditText edtLoginUsername, edtLoginPassword;
    Button btnLoginConfirm;
    TextView tvGoToRegisterLab5;
    CheckBox chkLoginRememberMe;
    ApiService apiService;

    private static final String PREF_LOGIN_REMEMBER_API = "LoginRememberPrefsAPI";
    private static final String KEY_REMEMBER_CHECKED_API = "login_remember_checked_api";
    private static final String KEY_REMEMBER_USER_API = "login_remember_user_api";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab5_login); // Đảm bảo tên layout đúng
        Log.d(TAG, "onCreate: Login Activity started");

        edtLoginUsername = findViewById(R.id.edtLoginUsername_lab5); // Cập nhật ID nếu bạn đổi
        edtLoginPassword = findViewById(R.id.edtLoginPassword_lab5);
        btnLoginConfirm = findViewById(R.id.btnLoginConfirm_lab5);
        tvGoToRegisterLab5 = findViewById(R.id.tvGoToRegister_lab5);
        chkLoginRememberMe = findViewById(R.id.chkLoginRememberMe_lab5);

        apiService = ApiClient.getClient().create(ApiService.class);

        loadRememberMeStatus();

        btnLoginConfirm.setOnClickListener(view -> performLoginViaApi());

        tvGoToRegisterLab5.setOnClickListener(view -> {
            Intent intent = new Intent(Lab5_Login.this, Lab5_Register.class);
            startActivity(intent);
        });
    }

    private void performLoginViaApi() {
        String username = edtLoginUsername.getText().toString().trim();
        String password = edtLoginPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập username và password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Quan trọng: Gửi mật khẩu đã hash MD5 lên server để so sánh
        String hashedPassword = getMd5Hash(password);
        if (hashedPassword == null) {
            Toast.makeText(this, "Lỗi mã hóa mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "Attempting login for user: " + username + " with hashed password: " + hashedPassword);


        User userRequest = new User(username, hashedPassword);
        Call<AuthResponse> call = apiService.loginUser(userRequest);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    Toast.makeText(Lab5_Login.this, authResponse.getMessage(), Toast.LENGTH_LONG).show();
                    if ("success".equals(authResponse.getStatus())) {
                        Log.i(TAG, "Login successful for user: " + username);
                        saveRememberMeStatus(username, chkLoginRememberMe.isChecked());
                        // TODO: Chuyển sang màn hình chính của ứng dụng
                        // Intent mainAppIntent = new Intent(Lab5LoginActivity.this, YourMainActivity.class);
                        // startActivity(mainAppIntent);
                        // finish();
                    } else {
                        Log.w(TAG, "Login failed: " + authResponse.getMessage());
                        if (authResponse.getMessage() != null) {
                            if (authResponse.getMessage().toLowerCase().contains("username not found")) {
                                edtLoginUsername.requestFocus();
                            } else if (authResponse.getMessage().toLowerCase().contains("incorrect password")) {
                                edtLoginPassword.requestFocus();
                            }
                        }
                    }
                } else {
                    Toast.makeText(Lab5_Login.this, "Đăng nhập thất bại! Lỗi server: " + response.code(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Login error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(Lab5_Login.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "Login failure: ", t);
            }
        });
    }

    private void saveRememberMeStatus(String username, boolean remember) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_LOGIN_REMEMBER_API, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_REMEMBER_CHECKED_API, remember);
        if (remember) {
            editor.putString(KEY_REMEMBER_USER_API, username);
        } else {
            editor.remove(KEY_REMEMBER_USER_API);
        }
        editor.apply();
    }

    private void loadRememberMeStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_LOGIN_REMEMBER_API, Context.MODE_PRIVATE);
        boolean rememberChecked = sharedPreferences.getBoolean(KEY_REMEMBER_CHECKED_API, false);
        chkLoginRememberMe.setChecked(rememberChecked);
        if (rememberChecked) {
            String savedUsername = sharedPreferences.getString(KEY_REMEMBER_USER_API, "");
            edtLoginUsername.setText(savedUsername);
            if (!savedUsername.isEmpty()) {
                edtLoginPassword.requestFocus();
            }
        }
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