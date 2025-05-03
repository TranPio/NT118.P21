package com.example.practice;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

// Thêm thư viện
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class bai4 extends AppCompatActivity {

    private TextView edtResult;
    private GridLayout gridKeys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai4);

        edtResult = findViewById(R.id.edtResult);
        gridKeys = findViewById(R.id.gridKeys);

        int count = gridKeys.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = gridKeys.getChildAt(i);
            if (view instanceof Button) {
                Button btn = (Button) view;
                btn.setOnClickListener(this::onButtonClick);
            }
        }
    }

    private void onButtonClick(View view) {
        String text = ((Button) view).getText().toString();
        String current = edtResult.getText().toString();

        switch (text) {
            case "AC":
                edtResult.setText("0");
                break;
            case "=":
                calculateResult();
                break;
            case "+/-":
                if (!current.equals("0") && !current.equals("Error")) {
                    if (current.startsWith("-")) {
                        edtResult.setText(current.substring(1));
                    } else {
                        edtResult.setText("-" + current);
                    }
                }
                break;
            default:
                if (current.equals("0") || current.equals("Error")) {
                    edtResult.setText(text);
                } else {
                    edtResult.append(text);
                }
                break;
        }
    }

    private void calculateResult() {
        try {
            String expr = edtResult.getText().toString()
                    .replace("×", "*")
                    .replace("÷", "/")
                    .replace(",", ".");

            Expression expression = new ExpressionBuilder(expr).build();
            double result = expression.evaluate();

            if (result == (long) result) {
                edtResult.setText(String.valueOf((long) result));
            } else {
                edtResult.setText(String.valueOf(result));
            }
        } catch (Exception e) {
            edtResult.setText("Error");
        }
    }
}
