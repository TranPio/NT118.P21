package com.example.practice;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class bai4 extends AppCompatActivity {

    private TextView edtResult;
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
                            edtResult.setText("0");
                        } else if (btnText.equals("=")) {
                            String expression = edtResult.getText().toString()
                                    .replace("×", "*")
                                    .replace("÷", "/")
                                    .replace(",", ".");
                            try {
                                double result = evaluateExpression(expression);
                                if (result == (int) result) {
                                    edtResult.setText(String.valueOf((int) result));
                                } else {
                                    edtResult.setText(String.valueOf(result));
                                }
                            } catch (Exception e) {
                                edtResult.setText("Error");
                            }
                        } else if (btnText.equals("+/-")) {
                            String current = edtResult.getText().toString();
                            if (!current.equals("0") && !current.equals("Error")) {
                                if (current.startsWith("-"))
                                    edtResult.setText(current.substring(1));
                                else
                                    edtResult.setText("-" + current);
                            }
                        } else {
                            String current = edtResult.getText().toString();
                            if (current.equals("0") || current.equals("Error")) {
                                edtResult.setText(btnText);
                            } else {
                                edtResult.append(btnText);
                            }
                        }
                    }
                });
            }
        }
    }

    /**
     * Đánh giá biểu thức số học
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
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();

                double x;
                int startPos = this.pos;
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
