package com.example.practice;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout; // Import LinearLayout
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets; // Import Insets

public class lab3_bai1_ac2 extends AppCompatActivity {

    TextView tvHoTen, tvCMND, tvBangCap, tvSoThich, tvThongTinBoSung;
    Button btnDong;
    LinearLayout rootLayout; // Thêm biến cho layout gốc

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Bước 1: Cho phép vẽ tràn viền (quan trọng: gọi trước setContentView)
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        // -------------------------

        setContentView(R.layout.activity_lab3_bai1_ac2); // Đảm bảo tên layout đúng

        // --- Ánh xạ Views ---
        rootLayout = findViewById(R.id.root_layout_ac2); // Ánh xạ layout gốc (cần thêm ID trong XML)
        tvHoTen = findViewById(R.id.tvHoTen);
        tvCMND = findViewById(R.id.tvCMND);
        tvBangCap = findViewById(R.id.tvBangCap);
        tvSoThich = findViewById(R.id.tvSoThich);
        tvThongTinBoSung = findViewById(R.id.tvThongTinBoSung);
        btnDong = findViewById(R.id.btnDong);
        // --------------------

        // Bước 2: Lắng nghe và áp dụng WindowInsets cho layout gốc
        ViewCompat.setOnApplyWindowInsetsListener(rootLayout, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), insets.top, v.getPaddingRight(), insets.bottom);
            return windowInsets;
        });
        // -------------------------

        // --- Nhận dữ liệu và hiển thị ---

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tvHoTen.setText(extras.getString("hoTen"));
            tvCMND.setText(extras.getString("cmnd"));
            tvBangCap.setText(extras.getString("bangCap"));
            tvSoThich.setText(extras.getString("soThich"));


            // Xử lý nếu thông tin bổ sung trống
            String boSung = extras.getString("thongTinBoSung");
            if (TextUtils.isEmpty(boSung)) {
                tvThongTinBoSung.setVisibility(View.GONE);
                // tvThongTinBoSung.setText("Không có thông tin bổ sung.");
            } else {
                tvThongTinBoSung.setText(boSung);
                tvThongTinBoSung.setVisibility(View.VISIBLE);
            }
        }

        // --- Xử lý nút Đóng ---
        btnDong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
