package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Bai2 extends AppCompatActivity {

    private static final String FILE_NAME = "notes.txt"; // Tên file lưu trữ
    private EditText edtInput;
    private ListView listView;
    private ArrayList<String> notesList;
    private ArrayAdapter<String> adapter;
    private int selectedIndex = -1; // Vị trí ghi chú đang chọn để sửa, -1 là không chọn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai2);

        edtInput = findViewById(R.id.edtInput);
        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnUpdate = findViewById(R.id.btnUpdate);
        listView = findViewById(R.id.listNotes);

        // Đọc ghi chú từ file khi ứng dụng khởi chạy
        notesList = readNotesFromFile();

        // Thiết lập Adapter cho ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notesList);
        listView.setAdapter(adapter);

        // Xử lý sự kiện khi chọn một item trong ListView để sửa
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edtInput.setText(notesList.get(position)); // Hiển thị nội dung lên EditText
                selectedIndex = position; // Lưu lại vị trí đã chọn
            }
        });

        // Xử lý sự kiện nút Thêm
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = edtInput.getText().toString().trim();
                if (!note.isEmpty()) {
                    notesList.add(note); // Thêm vào danh sách
                    writeNotesToFile(notesList); // Ghi lại vào file
                    adapter.notifyDataSetChanged(); // Cập nhật ListView
                    edtInput.setText(""); // Xóa nội dung EditText
                    selectedIndex = -1; // Reset vị trí chọn
                    Toast.makeText(Bai2.this, "Đã thêm ghi chú", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Bai2.this, "Vui lòng nhập ghi chú", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý sự kiện nút Cập nhật
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = edtInput.getText().toString().trim();
                // Chỉ cập nhật nếu có nội dung và đã chọn một ghi chú
                if (!note.isEmpty() && selectedIndex != -1) {
                    notesList.set(selectedIndex, note); // Cập nhật tại vị trí đã chọn
                    writeNotesToFile(notesList); // Ghi lại toàn bộ file
                    adapter.notifyDataSetChanged(); // Cập nhật ListView
                    edtInput.setText(""); // Xóa nội dung EditText
                    selectedIndex = -1; // Reset vị trí chọn
                    Toast.makeText(Bai2.this, "Đã cập nhật ghi chú", Toast.LENGTH_SHORT).show();
                } else if (selectedIndex == -1) {
                    Toast.makeText(Bai2.this, "Vui lòng chọn ghi chú cần cập nhật", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Bai2.this, "Vui lòng nhập nội dung cập nhật", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Phương thức đọc danh sách ghi chú từ file trong Internal Storage
    private ArrayList<String> readNotesFromFile() {
        ArrayList<String> list = new ArrayList<>();
        FileInputStream fis = null;
        try {
            fis = openFileInput(FILE_NAME); // Mở file để đọc
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);
            String line;
            while ((line = reader.readLine()) != null) { // Đọc từng dòng
                list.add(line);
            }
            reader.close();
        } catch (IOException e) {
            // Nếu file không tồn tại hoặc lỗi đọc, trả về danh sách rỗng
            Log.e("Bai2", "Error reading file: " + e.getMessage());
            // Toast.makeText(this, "Chưa có ghi chú nào", Toast.LENGTH_SHORT).show();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    Log.e("Bai2", "Error closing FileInputStream: " + e.getMessage());
                }
            }
        }
        return list;
    }

    // Phương thức ghi danh sách ghi chú vào file trong Internal Storage
    private void writeNotesToFile(ArrayList<String> list) {
        FileOutputStream fos = null;
        try {
            // Mở file để ghi, MODE_PRIVATE sẽ ghi đè nội dung cũ
            fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            for (String note : list) {
                fos.write((note + "\n").getBytes()); // Ghi từng ghi chú kèm ký tự xuống dòng
            }
            Log.d("Bai2", "Notes written to file successfully.");
        } catch (IOException e) {
            Log.e("Bai2", "Error writing file: " + e.getMessage());
            Toast.makeText(this, "Lỗi khi lưu ghi chú!", Toast.LENGTH_SHORT).show();
        } finally {
            if (fos != null) {
                try {
                    fos.close(); // Luôn đóng file sau khi xong việc
                } catch (IOException e) {
                    Log.e("Bai2", "Error closing FileOutputStream: " + e.getMessage());
                }
            }
        }
    }
}