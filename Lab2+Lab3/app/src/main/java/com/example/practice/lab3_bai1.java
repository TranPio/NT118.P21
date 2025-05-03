package com.example.practice;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout; // Import LinearLayout
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets; // Import Insets

import java.util.ArrayList;

public class lab3_bai1 extends AppCompatActivity {

    EditText edtHoTen, edtCMND, edtThongTinBoSung;
    TextView tvErrorHoTen, tvErrorCMND, tvErrorSoThich;
    RadioGroup rgBangCap;
    CheckBox cbDocBao, cbDocSach, cbDocCoding;
    Button btnGuiThongTin;
    LinearLayout mainLayout; // Thêm biến cho LinearLayout con của ScrollView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Bước 1: Cho phép vẽ tràn viền (quan trọng: gọi trước setContentView)
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        // -------------------------

        setContentView(R.layout.activity_lab3_bai1); // Đảm bảo tên layout đúng

        // --- Ánh xạ Views ---
        mainLayout = findViewById(R.id.main_content_layout);
        tvErrorHoTen = findViewById(R.id.tvErrorHoTen);
        tvErrorCMND = findViewById(R.id.tvErrorCMND);
        tvErrorSoThich = findViewById(R.id.tvErrorSoThich);
        edtHoTen = findViewById(R.id.edtHoTen);
        edtCMND = findViewById(R.id.edtCMND);
        edtThongTinBoSung = findViewById(R.id.edtThongTinBoSung);
        rgBangCap = findViewById(R.id.rgBangCap);
        cbDocBao = findViewById(R.id.cbDocBao);
        cbDocSach = findViewById(R.id.cbDocSach);
        cbDocCoding = findViewById(R.id.cbDocCoding);
        btnGuiThongTin = findViewById(R.id.btnGuiThongTin);
        // --------------------

        // Bước 2: Lắng nghe và áp dụng WindowInsets cho LinearLayout con
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), insets.top, v.getPaddingRight(), insets.bottom);
            return windowInsets;
        });

        btnGuiThongTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    sendDataToActivity2();
                }
            }
        });
    }

    private boolean validateInput() {
        boolean isValid = true;

        // Ẩn lỗi cũ
        tvErrorHoTen.setVisibility(View.GONE);
        tvErrorCMND.setVisibility(View.GONE);
        tvErrorSoThich.setVisibility(View.GONE);

        // Kiểm tra họ tên
        String hoTen = edtHoTen.getText().toString().trim();
        if (TextUtils.isEmpty(hoTen)) {
            tvErrorHoTen.setText("Họ tên không được để trống");
            tvErrorHoTen.setVisibility(View.VISIBLE);
            isValid = false;
        } else if (hoTen.length() < 3) {
            tvErrorHoTen.setText("Họ tên phải có ít nhất 3 ký tự");
            tvErrorHoTen.setVisibility(View.VISIBLE);
            isValid = false;
        }

        // Kiểm tra CMND
        String cmnd = edtCMND.getText().toString().trim();
        if (TextUtils.isEmpty(cmnd)) {
            tvErrorCMND.setText("CMND không được để trống");
            tvErrorCMND.setVisibility(View.VISIBLE);
            isValid = false;
        } else if (!cmnd.matches("\\d{9}")) { // Regex kiểm tra 9 chữ số
            tvErrorCMND.setText("CMND phải gồm đúng 9 chữ số");
            tvErrorCMND.setVisibility(View.VISIBLE);
            isValid = false;
        }

        // Kiểm tra sở thích
        if (!cbDocBao.isChecked() && !cbDocSach.isChecked() && !cbDocCoding.isChecked()) {
            tvErrorSoThich.setText("Vui lòng chọn ít nhất 1 sở thích");
            tvErrorSoThich.setVisibility(View.VISIBLE);
            isValid = false;
        }

        return isValid;
    }

    private void sendDataToActivity2() {
        // Lấy dữ liệu
        String hoTen = edtHoTen.getText().toString().trim();
        String cmnd = edtCMND.getText().toString().trim();
        String thongTinBoSung = edtThongTinBoSung.getText().toString().trim();

        // Lấy bằng cấp
        String bangCap = "";
        int selectedBangCapId = rgBangCap.getCheckedRadioButtonId();
        // Kiểm tra xem có RadioButton nào được chọn không (tránh NullPointerException)
        if (selectedBangCapId != -1) {
            RadioButton selectedBangCap = findViewById(selectedBangCapId);
            bangCap = selectedBangCap.getText().toString();
        } else {
            // Xử lý trường hợp không có bằng cấp nào được chọn (nếu có thể xảy ra)
            Toast.makeText(this, "Vui lòng chọn bằng cấp", Toast.LENGTH_SHORT).show();
            return; // Không gửi nếu chưa chọn
        }


        // Lấy sở thích
        StringBuilder soThich = new StringBuilder();
        if (cbDocBao.isChecked()) soThich.append("Đọc báo - ");
        if (cbDocSach.isChecked()) soThich.append("Đọc sách - ");
        if (cbDocCoding.isChecked()) soThich.append("Đọc coding - ");
        // Xóa dấu " - " cuối cùng nếu có sở thích được chọn
        if (soThich.length() > 0) {
            soThich.setLength(soThich.length() - 3);
        } else {
            // Trường hợp này đã được validateInput xử lý, nhưng để chắc chắn
            Toast.makeText(this, "Vui lòng chọn sở thích", Toast.LENGTH_SHORT).show();
            return; // Không gửi nếu chưa chọn
        }

        // Tạo Intent
        Intent intent = new Intent(lab3_bai1.this, lab3_bai1_ac2.class);
        Bundle bundle = new Bundle();
        bundle.putString("hoTen", hoTen);
        bundle.putString("cmnd", cmnd);
        bundle.putString("bangCap", bangCap);
        bundle.putString("soThich", soThich.toString());
        bundle.putString("thongTinBoSung", thongTinBoSung);
        intent.putExtras(bundle);

        startActivity(intent);


    }

}
