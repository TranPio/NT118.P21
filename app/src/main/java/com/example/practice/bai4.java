package com.example.practice;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.text.TextPaint;

import androidx.appcompat.app.AppCompatActivity;

public class bai4 extends AppCompatActivity {

    private EditText edtResult;
    private GridLayout gridLayoutKeys;

    private final float MAX_TEXT_SIZE_SP = 60f;
    private final float MIN_TEXT_SIZE_SP = 12f;
    private final float TEXT_STEP_SP = 2f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai4);

        edtResult = findViewById(R.id.edtResult);
        gridLayoutKeys = findViewById(R.id.gridKeys);

        // Set text size mặc định
        edtResult.setTextSize(TypedValue.COMPLEX_UNIT_SP, MAX_TEXT_SIZE_SP);

        // Gắn TextWatcher để tự điều chỉnh kích thước khi text thay đổi
        edtResult.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                adjustTextSizeToFit(edtResult);
            }
        });

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
                            edtResult.setText("");
                        } else if (btnText.equals("=")) {
                            String expression = edtResult.getText().toString();
                            expression = expression.replace("×", "*")
                                    .replace("÷", "/")
                                    .replace(",", ".");
                            try {
                                double result = evaluateExpression(expression);
                                if(result == (int) result) {
                                    edtResult.setText(String.valueOf((int) result));
                                } else {
                                    edtResult.setText(String.valueOf(result));
                                }
                            } catch (Exception e) {
                                edtResult.setText("Error");
                            }
                        } else if (btnText.equals("+/-")) {
                            String current = edtResult.getText().toString();
                            if (!current.isEmpty()) {
                                if (current.startsWith("-"))
                                    edtResult.setText(current.substring(1));
                                else
                                    edtResult.setText("-" + current);
                            }
                        } else {
                            edtResult.append(btnText);
                        }
                    }
                });
            }
        }
    }

    // Hàm điều chỉnh kích thước chữ của EditText khi quá dài
    private void adjustTextSizeToFit(EditText editText) {
        int viewWidth = editText.getWidth()
                - editText.getPaddingLeft()
                - editText.getPaddingRight();

        if (viewWidth <= 0) return;

        String text = editText.getText().toString();
        if (text.isEmpty()) {
            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, MAX_TEXT_SIZE_SP);
            return;
        }

        TextPaint paint = editText.getPaint();
        float trySize = MAX_TEXT_SIZE_SP;
        paint.setTextSize(spToPx(trySize));

        while (trySize > MIN_TEXT_SIZE_SP && paint.measureText(text) > viewWidth) {
            trySize -= TEXT_STEP_SP;
            paint.setTextSize(spToPx(trySize));
        }

        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, trySize);
    }

    private float spToPx(float sp) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                sp,
                getResources().getDisplayMetrics()
        );
    }

    /**
     * Đánh giá biểu thức số học
     */
    private double evaluateExpression(final String str) {
        return new Object() {
            int pos = -1, ch;
            void nextChar() { pos++; ch = pos < str.length() ? str.charAt(pos) : -1; }
            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) { nextChar(); return true; }
                return false;
            }
            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }
            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }
            double parseTerm() {
                double x = parseFactor();
                for (;;) {
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
