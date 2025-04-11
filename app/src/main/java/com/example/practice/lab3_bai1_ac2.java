package com.example.practice;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class lab3_bai1_ac2 extends AppCompatActivity {

    TextView tvHoTen, tvCMND, tvBangCap, tvSoThich, tvThongTinBoSung;
    Button btnDong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab3_bai1_ac2);

        tvHoTen = findViewById(R.id.tvHoTen);
        tvCMND = findViewById(R.id.tvCMND);
        tvBangCap = findViewById(R.id.tvBangCap);
        tvSoThich = findViewById(R.id.tvSoThich);
        tvThongTinBoSung = findViewById(R.id.tvThongTinBoSung);
        btnDong = findViewById(R.id.btnDong);

        // Nhận dữ liệu từ Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tvHoTen.setText(extras.getString("hoTen"));
            tvCMND.setText(extras.getString("cmnd"));
            tvBangCap.setText(extras.getString("bangCap"));
            tvSoThich.setText(extras.getString("soThich"));
            tvThongTinBoSung.setText(extras.getString("thongTinBoSung"));
        }

        btnDong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // đóng Activity 2, quay lại Activity 1
            }
        });
    }
}
