package com.example.lab4;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Bai1_1 extends AppCompatActivity {
    private EditText edtUsername, edtPassword;
    private CheckBox chkRemember;
    private Button btnLogin;
    private SharedPreferences sharedPreferences;
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
        chkRemember = findViewById(R.id.chkRemember);
        btnLogin = findViewById(R.id.btnLogin);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Load saved login info if "remember" is true
        if (sharedPreferences.getBoolean(KEY_REMEMBER, false)) {
            edtUsername.setText(sharedPreferences.getString(KEY_USERNAME, ""));
            edtPassword.setText(sharedPreferences.getString(KEY_PASSWORD, ""));
            chkRemember.setChecked(true);
        }

        btnLogin.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (username.equals("tranhoaiphu") && password.equals("22521106")) {
                Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                if (chkRemember.isChecked()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(KEY_USERNAME, username);
                    editor.putString(KEY_PASSWORD, password);
                    editor.putBoolean(KEY_REMEMBER, true);
                    editor.apply();
                }
            } else {
                Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear(); // Clear saved data if login fails
                editor.apply();
                edtUsername.setText(""); // Reset EditText fields
                edtPassword.setText("");
                chkRemember.setChecked(false);
            }
        });
    }
}