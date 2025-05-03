package com.example.lab4;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Bai1_2 extends AppCompatActivity {
    private EditText etDisplayName, etEmail;
    private Button btnSave, btnLoad;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai1_2);

        etDisplayName = findViewById(R.id.etDisplayName);
        etEmail       = findViewById(R.id.etEmail);
        btnSave       = findViewById(R.id.btnSaveSettings);
        btnLoad       = findViewById(R.id.btnLoadSettings);

        prefs = getSharedPreferences("SettingsPrefs", Context.MODE_PRIVATE);

        btnSave.setOnClickListener(v -> {
            String name = etDisplayName.getText().toString();
            String email = etEmail.getText().toString();
            prefs.edit()
                    .putString("display_name", name)
                    .putString("email", email)
                    .apply();
            Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show();
        });

        btnLoad.setOnClickListener(v -> {
            String name = prefs.getString("display_name", "");
            String email = prefs.getString("email", "");
            etDisplayName.setText(name);
            etEmail.setText(email);
            Toast.makeText(this, "Settings loaded", Toast.LENGTH_SHORT).show();
        });
    }
}
