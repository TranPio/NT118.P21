package com.example.practice; // Giữ nguyên package nếu cùng project

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

// Thêm thư viện tính toán biểu thức
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class lab3_bai2 extends AppCompatActivity {

    private TextView tvResultLab3Bai2;
    private GridLayout gridKeysLab3Bai2;
    private Button btnDeleteLab3Bai2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Sử dụng layout activity_lab3_bai2.xml
        setContentView(R.layout.activity_lab3_bai2);

        // Ánh xạ các thành phần giao diện
        tvResultLab3Bai2 = findViewById(R.id.tvResultLab3Bai2);
        gridKeysLab3Bai2 = findViewById(R.id.gridKeysLab3Bai2);
        btnDeleteLab3Bai2 = findViewById(R.id.btnDeleteLab3Bai2);

        // Đặt listener cho nút DELETE
        btnDeleteLab3Bai2.setOnClickListener(this::onDeleteClick);

        // Đặt listener cho các nút trong GridLayout
        int count = gridKeysLab3Bai2.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = gridKeysLab3Bai2.getChildAt(i);
            if (view instanceof Button) {
                Button btn = (Button) view;
                // Bỏ qua nút DELETE vì đã set listener riêng
                if (btn.getId() != R.id.btnDeleteLab3Bai2) {
                    btn.setOnClickListener(this::onButtonClick);
                }
            }
        }
    }

    // Xử lý sự kiện click cho các nút số và phép tính
    private void onButtonClick(View view) {
        String text = ((Button) view).getText().toString();
        String current = tvResultLab3Bai2.getText().toString();

        switch (text) {
            case "=":
                calculateResult();
                break;

            default: // Các nút số, phép tính, dấu chấm
                if (current.equals("0") || current.equals("Error")) {
                    // Thay thế "0" hoặc "Error" bằng ký tự mới (trừ khi là phép tính hoặc dấu chấm bắt đầu)
                    if (text.matches("[+\\-×÷]") && current.equals("0")) {
                        tvResultLab3Bai2.append(text); // Cho phép bắt đầu bằng phép tính nếu đang là 0
                    } else if (text.equals(".") && current.equals("0")) {
                        tvResultLab3Bai2.setText("0."); // Bắt đầu bằng 0.
                    }
                    else {
                        tvResultLab3Bai2.setText(text);
                    }
                } else {
                    tvResultLab3Bai2.append(text);
                }
                break;
        }
    }

    // Xử lý sự kiện click cho nút DELETE
    private void onDeleteClick(View view) {
        String current = tvResultLab3Bai2.getText().toString();
        if (!current.equals("0") && !current.equals("Error") && current.length() > 0) {
            String newText = current.substring(0, current.length() - 1);
            if (newText.isEmpty() || newText.equals("-")) { // Nếu xóa hết hoặc chỉ còn dấu "-"
                tvResultLab3Bai2.setText("0");
            } else {
                tvResultLab3Bai2.setText(newText);
            }
        } else {
            // Nếu đang là "0" hoặc "Error" thì không làm gì cả
            tvResultLab3Bai2.setText("0");
        }
    }


    // Hàm tính toán kết quả
    private void calculateResult() {
        try {
            String expr = tvResultLab3Bai2.getText().toString()
                    .replace("×", "*") // Thay thế ký tự hiển thị bằng ký tự tính toán
                    .replace("÷", "/")
                    .replace(",", "."); // Đảm bảo dùng dấu chấm cho số thập phân

            // Xử lý trường hợp kết thúc bằng phép tính hoặc dấu chấm
            if (expr.matches(".*[+\\-*/.]$")) {
                expr = expr.substring(0, expr.length() - 1);
            }

            // Kiểm tra nếu biểu thức rỗng sau khi xóa
            if (expr.isEmpty()) {
                tvResultLab3Bai2.setText("0");
                return;
            }


            Expression expression = new ExpressionBuilder(expr).build();
            double result = expression.evaluate();

            // Hiển thị kết quả: số nguyên nếu không có phần thập phân
            if (result == (long) result) {
                tvResultLab3Bai2.setText(String.valueOf((long) result));
            } else {
                // Làm tròn hoặc định dạng nếu cần
                tvResultLab3Bai2.setText(String.format("%.4f", result).replaceAll("\\.?0*$", "")); // Ví dụ làm tròn 4 chữ số và bỏ số 0 thừa
                // tvResultLab3Bai2.setText(String.valueOf(result)); // Hoặc hiển thị đầy đủ
            }
        } catch (Exception e) {
            tvResultLab3Bai2.setText("Error"); // Hiển thị lỗi nếu biểu thức không hợp lệ
        }
    }
}