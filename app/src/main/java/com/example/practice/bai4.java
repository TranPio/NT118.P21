package com.example.practice;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;

public class bai4 extends AppCompatActivity {

    private EditText edtResult;
    private GridLayout gridLayoutKeys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai4);

        edtResult = findViewById(R.id.edtResult);
        gridLayoutKeys = findViewById(R.id.gridKeys);

        int childCount = gridLayoutKeys.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = gridLayoutKeys.getChildAt(i);
            if (view instanceof Button) {
                Button btn = (Button) view;
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String btnText = ((Button) v).getText().toString();

                        if (btnText.equals("AC")) {
                            // Xóa toàn bộ nội dung
                            edtResult.setText("");
                        } else if (btnText.equals("=")) {
                            // Thực hiện đánh giá biểu thức
                            String expression = edtResult.getText().toString();
                            // Thay thế các kí tự đặc biệt: "×" thành "*" và "÷" thành "/"
                            expression = expression.replace("×", "*")
                                    .replace("÷", "/")
                                    .replace(",", "."); // nếu sử dụng dấu phẩy cho số thực
                            try {
                                double result = evaluateExpression(expression);
                                // Hiển thị kết quả, nếu kết quả là số nguyên thì không hiện phần thập phân
                                if(result == (int) result) {
                                    edtResult.setText(String.valueOf((int) result));
                                } else {
                                    edtResult.setText(String.valueOf(result));
                                }
                            } catch (Exception e) {
                                edtResult.setText("Error");
                            }
                        } else if (btnText.equals("+/-")) {
                            // Đảo dấu số hiển thị hiện tại
                            String current = edtResult.getText().toString();
                            if (!current.isEmpty()) {
                                if (current.startsWith("-"))
                                    edtResult.setText(current.substring(1));
                                else
                                    edtResult.setText("-" + current);
                            }
                        } else {
                            // Với các nút số và phép toán: nối thêm kí tự vào EditText
                            edtResult.append(btnText);
                        }
                    }
                });
            }
        }
    }

    /**
     * Phương thức đánh giá biểu thức toán học với các phép cộng, trừ, nhân, chia
     * Sử dụng thuật toán đệ quy descent để phân tích biểu thức (hỗ trợ thứ tự ưu tiên).
     */
    private double evaluateExpression(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                pos++;
                ch = pos < str.length() ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }
                return x;
            }

            // Xử lý phép cộng và trừ
            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+')) {
                        x += parseTerm(); // Phép cộng
                    } else if (eat('-')) {
                        x -= parseTerm(); // Phép trừ
                    } else {
                        return x;
                    }
                }
            }

            // Xử lý phép nhân và chia
            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('*')) {
                        x *= parseFactor();
                    } else if (eat('/')) {
                        x /= parseFactor();
                    } else {
                        return x;
                    }
                }
            }

            // Xử lý số và dấu +/-
            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();

                double x;
                int startPos = this.pos;
                // Nếu là số hoặc dấu chấm thập phân
                if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }
                return x;
            }
        }.parse();
    }
}
