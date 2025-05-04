package com.example.lab4;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView; // Đảm bảo import TextView
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets; // Import để dùng UTF-8

public class Bai3 extends AppCompatActivity {

    private static final String FILE_NAME = "external.txt";
    private static final int REQUEST_CODE_WRITE_STORAGE = 101;
    private static final String TAG = "Bai3Activity"; // Thêm TAG để log cho dễ

    // Khai báo các View
    EditText edtInput;
    Button btnSave, btnLoad;
    TextView txtResult; // Khai báo TextView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai3);
        Log.d(TAG, "onCreate: Activity created");

        // Ánh xạ View từ layout
        edtInput = findViewById(R.id.edtInput);
        btnSave = findViewById(R.id.btnSave);
        btnLoad = findViewById(R.id.btnLoad);
        txtResult = findViewById(R.id.txtResult); // Ánh xạ TextView

        // Gán sự kiện click cho các nút
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Save button pressed");
                saveDataToExternalStorage();
            }
        });

        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Load button pressed");
                loadDataFromExternalStorage();
            }
        });

        // Có thể tùy chọn load dữ liệu lần đầu khi mở app
        // loadDataFromExternalStorage();
    }

    /**
     * Kiểm tra xem bộ nhớ ngoài có sẵn để ghi không.
     * @return true nếu có thể ghi, false nếu không.
     */
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        boolean isWritable = Environment.MEDIA_MOUNTED.equals(state);
        Log.d(TAG, "isExternalStorageWritable: state=" + state + ", isWritable=" + isWritable);
        return isWritable;
    }

    /**
     * Lấy đối tượng File trỏ đến file dữ liệu trong thư mục riêng của ứng dụng trên External Storage.
     * @return Đối tượng File hoặc null nếu không truy cập được thư mục.
     */
    private File getExternalAppSpecificFile() {
        File directory = getExternalFilesDir(null); // Thư mục files trong Android/data/package.name/
        if (directory == null) {
            Log.e(TAG, "getExternalAppSpecificFile: External storage directory not available.");
            return null;
        }
        File file = new File(directory, FILE_NAME);
        Log.d(TAG, "getExternalAppSpecificFile: Path=" + file.getAbsolutePath());
        return file;
    }

    /**
     * Lưu dữ liệu từ EditText vào file trên External Storage.
     */
    private void saveDataToExternalStorage() {
        if (!isExternalStorageWritable()) {
            Toast.makeText(this, "Bộ nhớ ngoài không sẵn sàng để ghi!", Toast.LENGTH_SHORT).show();
            return;
        }

        String data = edtInput.getText().toString();
        // Có thể không cần kiểm tra data.isEmpty() nếu muốn cho phép lưu file rỗng

        File file = getExternalAppSpecificFile();
        if (file == null) {
            Toast.makeText(this, "Không thể truy cập thư mục lưu trữ ngoài!", Toast.LENGTH_SHORT).show();
            return;
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file); // Mở file để ghi đè
            fos.write(data.getBytes(StandardCharsets.UTF_8)); // Ghi dữ liệu dạng byte với encoding UTF-8
            // Không xóa EditText để người dùng thấy nội dung vừa nhập/lưu
            // edtInput.setText("");
            Toast.makeText(this, "Dữ liệu đã lưu vào External Storage", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "saveDataToExternalStorage: Data saved successfully to " + file.getAbsolutePath());
        } catch (IOException e) {
            Log.e(TAG, "saveDataToExternalStorage: Error writing to file", e);
            Toast.makeText(this, "Lỗi khi lưu file!", Toast.LENGTH_SHORT).show();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                    Log.d(TAG, "saveDataToExternalStorage: FileOutputStream closed.");
                } catch (IOException e) {
                    Log.e(TAG, "saveDataToExternalStorage: Error closing FileOutputStream", e);
                }
            }
        }
    }

    /**
     * Đọc dữ liệu từ file trên External Storage và hiển thị lên EditText và TextView.
     */
    private void loadDataFromExternalStorage() {
        // Mặc dù chỉ đọc, kiểm tra luôn trạng thái writable thường không hại
        if (!isExternalStorageWritable()) {
            String errorMsg = "Bộ nhớ ngoài không sẵn sàng!";
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
            edtInput.setText(errorMsg); // Hiển thị lỗi trên EditText
            txtResult.setText(errorMsg); // Hiển thị lỗi trên TextView
            return;
        }

        File file = getExternalAppSpecificFile();
        if (file == null) {
            String errorMsg = "Không thể truy cập thư mục lưu trữ ngoài!";
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
            edtInput.setText(errorMsg);
            txtResult.setText(errorMsg);
            return;
        }

        if (file.exists()) {
            Log.d(TAG, "loadDataFromExternalStorage: File exists, attempting to read.");
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                // Đọc toàn bộ nội dung file (phù hợp với file nhỏ)
                byte[] data = new byte[(int) file.length()];
                int bytesRead = fis.read(data); // Đọc dữ liệu vào mảng byte
                Log.d(TAG, "loadDataFromExternalStorage: Bytes read: " + bytesRead);

                // Chuyển đổi byte array sang String sử dụng UTF-8
                String fileContent = new String(data, StandardCharsets.UTF_8);

                // Hiển thị nội dung lên cả EditText và TextView
                edtInput.setText(fileContent);
                txtResult.setText(fileContent);

                // Thông báo thành công
                Toast.makeText(this, "Đọc dữ liệu từ External Storage thành công", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "loadDataFromExternalStorage: Data loaded successfully from " + file.getAbsolutePath());

            } catch (IOException e) {
                Log.e(TAG, "loadDataFromExternalStorage: Error reading from file", e);
                String errorMsg = "Lỗi khi đọc file!";
                edtInput.setText(""); // Xóa EditText nếu đọc lỗi
                txtResult.setText(errorMsg); // Hiển thị lỗi trên TextView
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
            } catch (OutOfMemoryError e) {
                Log.e(TAG, "loadDataFromExternalStorage: OutOfMemoryError reading large file", e);
                String errorMsg = "Lỗi: File quá lớn để đọc!";
                edtInput.setText("");
                txtResult.setText(errorMsg);
                Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                        Log.d(TAG, "loadDataFromExternalStorage: FileInputStream closed.");
                    } catch (IOException e) {
                        Log.e(TAG, "loadDataFromExternalStorage: Error closing FileInputStream", e);
                    }
                }
            }
        } else {
            // File không tồn tại
            String notFoundMsg = "Không có dữ liệu trong file " + FILE_NAME;
            Log.w(TAG, "loadDataFromExternalStorage: File not found: " + file.getAbsolutePath());
            edtInput.setText(""); // Xóa EditText
            txtResult.setText(notFoundMsg); // Hiển thị thông báo trên TextView
            Toast.makeText(this, "File " + FILE_NAME + " không tồn tại", Toast.LENGTH_SHORT).show();
        }
    }

}