package com.example.practice;

import android.os.Bundle;
// Xóa các import không dùng đến như View, Button, GridLayout, GridView nếu chúng không cần thiết nữa
// import android.view.View;
// import android.widget.Button;
// import android.widget.GridLayout;
// import android.widget.GridView; // GridView không được sử dụng trong layout

import androidx.appcompat.app.AppCompatActivity;
// Các import cho EdgeToEdge và WindowInsetsCompat có thể giữ lại nếu bạn dùng tính năng đó
// import androidx.activity.EdgeToEdge;
// import androidx.core.graphics.Insets;
// import androidx.core.view.ViewCompat;
// import androidx.core.view.WindowInsetsCompat;

public class bai4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // EdgeToEdge.enable(this); // Bỏ comment nếu bạn muốn dùng EdgeToEdge
        setContentView(R.layout.activity_bai4);

        // ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> { // Thay R.id.main bằng ID layout gốc của bạn nếu dùng EdgeToEdge
        //     Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        //     v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
        //     return insets;
        // });

        // --- BỎ ĐOẠN CODE GÂY LỖI Ở ĐÂY ---
        /*
        GridLayout gridLayout = findViewById(R.id.gridKeys);
        int childCount = gridLayout.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View view = gridLayout.getChildAt(i);
            if (view instanceof Button) {
                Button btn = (Button) view;
                // Lấy tag (sẽ là null vì không được định nghĩa trong XML)
                String tag = (String) btn.getTag();
                // Đặt text của button thành null, làm mất text gốc trong XML
                btn.setText(tag);
            }
        }
        */
        // --- KẾT THÚC ĐOẠN CODE BỊ BỎ ---

        // Các xử lý sự kiện click cho button có thể thêm vào đây nếu cần
    }
}
