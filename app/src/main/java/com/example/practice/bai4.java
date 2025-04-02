package com.example.practice;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class bai4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai4);

        GridLayout gridLayout = findViewById(R.id.gridKeys);
        int childCount = gridLayout.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View view = gridLayout.getChildAt(i);
            if (view instanceof Button) {
                Button btn = (Button) view;
                String tag = (String) btn.getTag();
                btn.setText(tag);  // Gán text = tag
            }
        }
    }

}