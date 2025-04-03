package com.example.practice;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class quytrinhthietke extends AppCompatActivity {

    TextView txtNoiDung; // toàn cục

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quytrinhthietke);
        // ánh xạ
        txtNoiDung = findViewById(R.id.textViewNoiDung);

        // viết code
        txtNoiDung.setText("Quy trình thiết kế ứng dụng");

    }
}