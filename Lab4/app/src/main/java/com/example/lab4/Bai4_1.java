package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class Bai4_1 extends AppCompatActivity {

    private static final String TAG = "Bai4_1Activity";

    EditText edtCode, edtName, edtCount;
    Button btnInsert, btnUpdate, btnDelete;
    ListView listView;
    DatabaseHelper dbHelper;
    SimpleCursorAdapter adapter;
    long selectedItemId = -1; // Lưu ID (_id) của item đang được chọn trong ListView

    // Biến lưu trữ index cột để tăng tốc độ truy cập cursor
    int colIdIndex = -1;
    int colCodeIndex = -1;
    int colNameIndex = -1;
    int colCountIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai4_1);
        Log.d(TAG, "onCreate: Activity started");

        // Ánh xạ Views
        edtCode = findViewById(R.id.edtClassCode);
        edtName = findViewById(R.id.edtClassName);
        edtCount = findViewById(R.id.edtStudentCount);
        btnInsert = findViewById(R.id.btnInsert);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        listView = findViewById(R.id.listView);

        // Khởi tạo DatabaseHelper
        dbHelper = new DatabaseHelper(this);
        Log.d(TAG, "onCreate: DatabaseHelper initialized");

        // Thiết lập Adapter cho ListView (ban đầu có thể rỗng)
        setupListViewAdapter();

        // Tải dữ liệu lần đầu
        loadData();

        // Gán sự kiện cho các nút
        btnInsert.setOnClickListener(v -> insertClass());
        btnUpdate.setOnClickListener(v -> updateClass());
        btnDelete.setOnClickListener(v -> deleteClass());

        // Sự kiện click trên ListView item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItemId = id; // id chính là giá trị cột _id
                Log.d(TAG, "onItemClick: Item selected with ID: " + selectedItemId + " at position " + position);

                Cursor cursor = (Cursor) adapter.getItem(position);
                if (cursor != null) {
                    // Lấy index cột (chỉ lần đầu nếu chưa có)
                    if (colCodeIndex == -1) getColumnIndices(cursor);

                    // Hiển thị dữ liệu lên EditTexts
                    edtCode.setText(cursor.getString(colCodeIndex));
                    edtName.setText(cursor.getString(colNameIndex));
                    edtCount.setText(String.valueOf(cursor.getInt(colCountIndex)));

                    edtCode.setEnabled(false); // Không cho sửa mã lớp
                    Log.d(TAG, "onItemClick: Data displayed for code " + cursor.getString(colCodeIndex));
                } else {
                    Log.e(TAG, "onItemClick: Cursor is null at position " + position);
                }
            }
        });
    }

    /** Lấy index các cột từ cursor để dùng lại */
    private void getColumnIndices(Cursor cursor) {
        if (cursor == null) return;
        try {
            colIdIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.LOP_COL_ID);
            colCodeIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.LOP_COL_CODE);
            colNameIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.LOP_COL_NAME);
            colCountIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.LOP_COL_COUNT);
            Log.d(TAG,"getColumnIndices: Indices obtained.");
        } catch (IllegalArgumentException e) {
            Log.e(TAG,"getColumnIndices: Error getting column indices", e);
        }
    }

    /** Thiết lập SimpleCursorAdapter cho ListView */
    private void setupListViewAdapter() {
        String[] fromColumns = {
                DatabaseHelper.LOP_COL_CODE,
                DatabaseHelper.LOP_COL_NAME,
                DatabaseHelper.LOP_COL_COUNT
        };
        int[] toViews = {
                R.id.itemClassCode,
                R.id.itemClassName,
                R.id.itemStudentCount
        };
        // Khởi tạo adapter với cursor là null ban đầu
        adapter = new SimpleCursorAdapter(
                this,
                R.layout.class_list_item,
                null, // Cursor sẽ được cung cấp sau trong loadData()
                fromColumns,
                toViews,
                0 // Flags
        );
        listView.setAdapter(adapter);
        Log.d(TAG, "setupListViewAdapter: Adapter set to ListView.");
    }

    /** Tải/Tải lại dữ liệu từ CSDL và cập nhật ListView */
    private void loadData() {
        Log.d(TAG, "loadData: Loading data...");
        Cursor newCursor = null;
        try {
            newCursor = dbHelper.getAllClasses(); // Lấy cursor mới
            if (newCursor != null) {
                // Lấy index cột nếu chưa có hoặc cursor thay đổi cấu trúc
                if (colCodeIndex == -1) getColumnIndices(newCursor);
                // Cập nhật cursor cho adapter
                adapter.changeCursor(newCursor); // Hàm này sẽ tự đóng cursor cũ (nếu có)
                Log.i(TAG, "loadData: Data loaded and adapter updated. Count: " + newCursor.getCount());
            } else {
                Log.e(TAG, "loadData: Failed to get cursor from database.");
                adapter.changeCursor(null); // Đặt cursor là null nếu lỗi
            }
        } catch (Exception e) {
            Log.e(TAG, "loadData: Error loading data", e);
            if (newCursor != null) newCursor.close(); // Đóng cursor nếu có lỗi xảy ra
            adapter.changeCursor(null);
            Toast.makeText(this, "Lỗi tải dữ liệu lớp học!", Toast.LENGTH_SHORT).show();
        }
        // Lưu ý: Không đóng newCursor ở đây vì adapter đang quản lý nó.
        // changeCursor sẽ xử lý cursor cũ.
    }

    /** Xóa trắng các ô nhập liệu và reset trạng thái */
    private void clearInput() {
        edtCode.setText("");
        edtName.setText("");
        edtCount.setText("");
        edtCode.setEnabled(true); // Cho phép nhập mã lớp lại
        selectedItemId = -1;
        listView.clearChoices(); // Xóa trạng thái chọn trên listview (nếu có)
        adapter.notifyDataSetChanged(); // Cập nhật listview để bỏ highlight (nếu có)
        Log.d(TAG, "clearInput: Input fields cleared.");
    }

    /** Xử lý thêm lớp */
    private void insertClass() {
        Log.d(TAG, "insertClass: Attempting to insert...");
        String code = edtCode.getText().toString().trim();
        String name = edtName.getText().toString().trim();
        String countStr = edtCount.getText().toString().trim();
        if (code.isEmpty() || name.isEmpty() || countStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            int count = Integer.parseInt(countStr);
            if (count < 0) {
                Toast.makeText(this, "Sĩ số không thể là số âm", Toast.LENGTH_SHORT).show();
                return;
            }
            long result = dbHelper.insertClass(code, name, count);
            if (result == -1) {
                Toast.makeText(this, "Thêm thất bại! Mã lớp có thể đã tồn tại.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                clearInput();
                loadData(); // Tải lại danh sách
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Sĩ số phải là một con số", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "insertClass: Invalid number format for count: " + countStr, e);
        }
    }

    /** Xử lý cập nhật lớp */
    private void updateClass() {
        Log.d(TAG, "updateClass: Attempting to update...");
        if (selectedItemId == -1) {
            Toast.makeText(this, "Vui lòng chọn lớp cần cập nhật từ danh sách", Toast.LENGTH_SHORT).show();
            return;
        }

        String code = edtCode.getText().toString().trim(); // Mã lớp lấy từ ô edt (đã disable)
        String name = edtName.getText().toString().trim();
        String countStr = edtCount.getText().toString().trim();

        if (name.isEmpty() || countStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên lớp và sĩ số mới", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int count = Integer.parseInt(countStr);
            if (count < 0) {
                Toast.makeText(this, "Sĩ số không thể là số âm", Toast.LENGTH_SHORT).show();
                return;
            }
            int result = dbHelper.updateClass(code, name, count);
            if (result > 0) {
                Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                clearInput();
                loadData(); // Tải lại danh sách
            } else {
                Toast.makeText(this, "Cập nhật thất bại! Không tìm thấy lớp.", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "updateClass: Failed. Class code " + code + " might not exist.");
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Sĩ số phải là một con số", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "updateClass: Invalid number format for count: " + countStr, e);
        }
    }

    /** Xử lý xóa lớp */
    private void deleteClass() {
        Log.d(TAG, "deleteClass: Attempting to delete...");
        if (selectedItemId == -1) {
            Toast.makeText(this, "Vui lòng chọn lớp cần xoá từ danh sách", Toast.LENGTH_SHORT).show();
            return;
        }

        String code = edtCode.getText().toString().trim(); // Mã lớp lấy từ ô edt (đã disable)

        int result = dbHelper.deleteClass(code);
        if (result > 0) {
            Toast.makeText(this, "Xoá thành công!", Toast.LENGTH_SHORT).show();
            clearInput();
            loadData(); // Tải lại danh sách
        } else {
            Toast.makeText(this, "Xoá thất bại! Không tìm thấy lớp.", Toast.LENGTH_SHORT).show();
            Log.w(TAG, "deleteClass: Failed. Class code " + code + " might not exist.");
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: Activity destroyed");
        // Đóng cursor mà adapter đang giữ để tránh rò rỉ
        Cursor cursor = adapter.getCursor();
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
            Log.d(TAG, "onDestroy: Adapter cursor closed.");
        }
        // Đóng database helper
        if (dbHelper != null) {
            dbHelper.close();
            Log.d(TAG, "onDestroy: DatabaseHelper closed.");
        }
        super.onDestroy();
    }
}