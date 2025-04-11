package com.example.practice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class lab3_bai1 extends AppCompatActivity {

    EditText edtHoTen, edtCMND, edtThongTinBoSung;
    TextView tvErrorHoTen, tvErrorCMND, tvErrorSoThich;
    RadioGroup rgBangCap;
    CheckBox cbDocBao, cbDocSach, cbDocCoding;
    Button btnGuiThongTin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab3_bai1);

        edtHoTen = findViewById(R.id.edtHoTen);
        edtCMND = findViewById(R.id.edtCMND);
        edtThongTinBoSung = findViewById(R.id.edtThongTinBoSung);

        tvErrorHoTen = findViewById(R.id.tvErrorHoTen);
        tvErrorCMND = findViewById(R.id.tvErrorCMND);
        tvErrorSoThich = findViewById(R.id.tvErrorSoThich);

        rgBangCap = findViewById(R.id.rgBangCap);
        cbDocBao = findViewById(R.id.cbDocBao);
        cbDocSach = findViewById(R.id.cbDocSach);
        cbDocCoding = findViewById(R.id.cbDocCoding);
        btnGuiThongTin = findViewById(R.id.btnGuiThongTin);

        btnGuiThongTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isValid = true;

                String hoTen = edtHoTen.getText().toString().trim();
                String cmnd = edtCMND.getText().toString().trim();
                String thongTinBoSung = edtThongTinBoSung.getText().toString().trim();

                // Reset lỗi
                tvErrorHoTen.setVisibility(View.GONE);
                tvErrorCMND.setVisibility(View.GONE);
                tvErrorSoThich.setVisibility(View.GONE);

                // Kiểm tra họ tên
                if (hoTen.length() < 3) {
                    tvErrorHoTen.setText("Họ tên phải có ít nhất 3 ký tự");
                    tvErrorHoTen.setVisibility(View.VISIBLE);
                    isValid = false;
                }

                // Kiểm tra CMND
                if (!cmnd.matches("\\d{9}")) {
                    tvErrorCMND.setText("CMND phải gồm đúng 9 chữ số");
                    tvErrorCMND.setVisibility(View.VISIBLE);
                    isValid = false;
                }

                // Kiểm tra sở thích
                StringBuilder soThich = new StringBuilder();
                if (cbDocBao.isChecked()) soThich.append("Đọc báo - ");
                if (cbDocSach.isChecked()) soThich.append("Đọc sách - ");
                if (cbDocCoding.isChecked()) soThich.append("Đọc coding - ");
                if (soThich.length() == 0) {
                    tvErrorSoThich.setText("Vui lòng chọn ít nhất một sở thích");
                    tvErrorSoThich.setVisibility(View.VISIBLE);
                    isValid = false;
                } else {
                    soThich.setLength(soThich.length() - 3); // xóa dấu " - "
                }

                if (!isValid) return;

                // Lấy bằng cấp
                RadioButton selectedBangCap = findViewById(rgBangCap.getCheckedRadioButtonId());
                String bangCap = selectedBangCap.getText().toString();

                // Tạo Intent để chuyển sang Activity 2
                Intent intent = new Intent(lab3_bai1.this, lab3_bai1_ac2.class);
                intent.putExtra("hoTen", hoTen);
                intent.putExtra("cmnd", cmnd);
                intent.putExtra("bangCap", bangCap);
                intent.putExtra("soThich", soThich.toString());
                intent.putExtra("thongTinBoSung", thongTinBoSung);

                startActivity(intent);
            }
        });
    }
}
