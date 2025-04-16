package com.example.practice; // Giữ nguyên package nếu cùng project

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout; // Import LinearLayout
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

// Thêm thư viện tính toán biểu thức
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class lab3_bai2 extends AppCompatActivity {

    private TextView tvResultLab3Bai2;
    private GridLayout gridKeysLab3Bai2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Đảm bảo sử dụng đúng file layout XML MỚI (với LinearLayout lồng trong GridLayout)
        setContentView(R.layout.activity_lab3_bai2);

        // Ánh xạ các thành phần giao diện
        tvResultLab3Bai2 = findViewById(R.id.tvResultLab3Bai2);
        gridKeysLab3Bai2 = findViewById(R.id.gridKeysLab3Bai2);

        // *** Cập nhật logic gán Listener ***

        // 1. Tìm nút DELETE bằng ID và gán listener (vì nó nằm trong LinearLayout con)
        Button btnDelete = findViewById(R.id.btnDeleteLab3Bai2);
        if (btnDelete != null) {
            btnDelete.setOnClickListener(this::onButtonClick);
        }

        // 2. Duyệt qua các con trực tiếp của GridLayout để gán listener cho các nút số/toán tử
        int count = gridKeysLab3Bai2.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = gridKeysLab3Bai2.getChildAt(i);
            // Bỏ qua LinearLayout ở hàng đầu tiên
            if (view instanceof LinearLayout) {
                continue; // Đi tiếp đến phần tử con tiếp theo của GridLayout
            }
            // Chỉ gán listener cho các Button (là các nút số và toán tử)
            if (view instanceof Button) {
                Button btn = (Button) view;
                // Không cần kiểm tra ID ở đây nữa vì LinearLayout đã được bỏ qua
                btn.setOnClickListener(this::onButtonClick);
            }
        }
        // *** Kết thúc cập nhật logic gán Listener ***

        // Đặt giá trị khởi đầu
        tvResultLab3Bai2.setText("0");
    }



    // Hàm xử lý sự kiện click chung cho TẤT CẢ các nút
    private void onButtonClick(View view) {
        String text = ((Button) view).getText().toString();
        String current = tvResultLab3Bai2.getText().toString();

        switch (text) {
            case "DELETE":
                if (!current.equals("0") && !current.equals("Error") && current.length() > 0) {
                    String newText = current.substring(0, current.length() - 1);
                    if (newText.isEmpty() || newText.equals("-")) {
                        tvResultLab3Bai2.setText("0");
                    } else {
                        tvResultLab3Bai2.setText(newText);
                    }
                } else {
                    tvResultLab3Bai2.setText("0");
                }
                break;
            case "=":
                calculateResult();
                break;

            default: // Numbers, operators, dot
                if (current.equals("0") || current.equals("Error")) {
                    if (text.equals(".")) {
                        tvResultLab3Bai2.setText("0.");
                    } else if (text.matches("[+\\-×÷]")) {
                        tvResultLab3Bai2.setText("0" + text);
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

    // Hàm tính toán kết quả (Không thay đổi)
    private void calculateResult() {
        try {
            String expr = tvResultLab3Bai2.getText().toString()
                    .replace("×", "*")
                    .replace("÷", "/")
                    .replace(",", ".");

            if (expr.matches(".*[+\\-*/.]$") && expr.length() > 1) {
                expr = expr.substring(0, expr.length() - 1);
            }

            if (expr.isEmpty() || expr.equals("-")) {
                tvResultLab3Bai2.setText("0");
                return;
            }
            if (expr.matches("^[+\\-*/]$")) {
                tvResultLab3Bai2.setText("Error");
                return;
            }

            Expression expression = new ExpressionBuilder(expr).build();
            double result = expression.evaluate();

            if (result == (long) result) {
                tvResultLab3Bai2.setText(String.valueOf((long) result));
            } else {
                String formattedResult = String.format("%.4f", result).replaceAll("\\.?0*$", "");
                if (formattedResult.equals("-0")) formattedResult = "0";
                tvResultLab3Bai2.setText(formattedResult);
            }
        } catch (Exception e) {
            tvResultLab3Bai2.setText("Error");
        }
    }
}