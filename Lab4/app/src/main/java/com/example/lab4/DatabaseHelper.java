package com.example.lab4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "QuanLySinhVien.db"; // Đổi tên DB nếu muốn
    private static final int DATABASE_VERSION = 1; // Bắt đầu version 1

    // Thông tin bảng Lớp học (tbllop)
    public static final String TABLE_LOP = "tbllop";
    public static final String LOP_COL_ID = "_id"; // Bắt buộc cho CursorAdapter
    public static final String LOP_COL_CODE = "malop";
    public static final String LOP_COL_NAME = "tenlop";
    public static final String LOP_COL_COUNT = "siso";

    // Câu lệnh tạo bảng Lớp học
    private static final String CREATE_TABLE_LOP =
            "CREATE TABLE " + TABLE_LOP + " (" +
                    LOP_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    LOP_COL_CODE + " TEXT UNIQUE NOT NULL, " + // Mã lớp không null và duy nhất
                    LOP_COL_NAME + " TEXT NOT NULL, " +        // Tên lớp không null
                    LOP_COL_COUNT + " INTEGER)";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "DatabaseHelper: Constructor called.");
    }

    // Gọi khi CSDL được tạo lần đầu
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: Creating database tables...");
        db.execSQL(CREATE_TABLE_LOP); // Tạo bảng lớp học
        Log.i(TAG, "onCreate: Table " + TABLE_LOP + " created.");
        // TODO: Sẽ thêm tạo bảng User ở Bài 4.2 tại đây
    }

    // Gọi khi nâng cấp CSDL (thay đổi DATABASE_VERSION)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "onUpgrade: Upgrading database from version " + oldVersion + " to " + newVersion);
        // Chính sách đơn giản: xóa bảng cũ và tạo lại
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOP);
        Log.w(TAG, "onUpgrade: Dropped table " + TABLE_LOP);
        // TODO: Sẽ thêm xóa bảng User ở Bài 4.2 tại đây
        onCreate(db); // Gọi lại onCreate để tạo cấu trúc mới
    }

    // --- Các phương thức CRUD cho bảng Lớp học (tbllop) ---

    /**
     * Thêm một lớp học mới vào CSDL.
     * @param code Mã lớp (phải là duy nhất).
     * @param name Tên lớp.
     * @param count Sĩ số.
     * @return ID của dòng mới được chèn, hoặc -1 nếu lỗi (ví dụ trùng mã lớp).
     */
    public long insertClass(String code, String name, int count) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LOP_COL_CODE, code);
        values.put(LOP_COL_NAME, name);
        values.put(LOP_COL_COUNT, count);

        long newRowId = -1;
        try {
            newRowId = db.insertOrThrow(TABLE_LOP, null, values); // insertOrThrow sẽ báo lỗi nếu vi phạm ràng buộc (vd: unique)
            Log.i(TAG, "insertClass: Success for code " + code + ", ID: " + newRowId);
        } catch (android.database.sqlite.SQLiteConstraintException e) {
            Log.e(TAG, "insertClass: Failed for code " + code + ". Constraint violation (likely duplicate code).", e);
        } catch (Exception e) {
            Log.e(TAG, "insertClass: Failed for code " + code, e);
        } finally {
            db.close(); // Luôn đóng DB sau khi dùng xong
        }
        return newRowId;
    }

    /**
     * Cập nhật thông tin lớp học dựa vào mã lớp.
     * @param code Mã lớp cần cập nhật (không đổi được mã lớp).
     * @param newName Tên lớp mới.
     * @param newCount Sĩ số mới.
     * @return Số lượng dòng được cập nhật (thường là 1 nếu thành công, 0 nếu không tìm thấy mã lớp).
     */
    public int updateClass(String code, String newName, int newCount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LOP_COL_NAME, newName);
        values.put(LOP_COL_COUNT, newCount);

        String selection = LOP_COL_CODE + " = ?";
        String[] selectionArgs = { code };

        int countAffected = 0;
        try {
            countAffected = db.update(TABLE_LOP, values, selection, selectionArgs);
            Log.i(TAG, "updateClass: Success for code " + code + ". Rows affected: " + countAffected);
        } catch (Exception e) {
            Log.e(TAG, "updateClass: Failed for code " + code, e);
        } finally {
            db.close();
        }
        return countAffected;
    }

    /**
     * Xóa một lớp học dựa vào mã lớp.
     * @param code Mã lớp cần xóa.
     * @return Số lượng dòng bị xóa (thường là 1 nếu thành công, 0 nếu không tìm thấy mã lớp).
     */
    public int deleteClass(String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = LOP_COL_CODE + " = ?";
        String[] selectionArgs = { code };

        int countDeleted = 0;
        try {
            countDeleted = db.delete(TABLE_LOP, selection, selectionArgs);
            Log.i(TAG, "deleteClass: Success for code " + code + ". Rows affected: " + countDeleted);
        } catch (Exception e) {
            Log.e(TAG, "deleteClass: Failed for code " + code, e);
        } finally {
            db.close();
        }
        return countDeleted;
    }

    /**
     * Lấy con trỏ (Cursor) chứa tất cả các lớp học.
     * @return Cursor trỏ đến dữ liệu, hoặc null nếu có lỗi. Cursor cần được đóng sau khi sử dụng.
     */
    public Cursor getAllClasses() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            // Query đơn giản lấy tất cả các cột, tất cả các dòng
            cursor = db.query(TABLE_LOP, null, null, null, null, null, null);
            Log.d(TAG, "getAllClasses: Query executed. Row count: " + (cursor != null ? cursor.getCount() : "null cursor"));
        } catch (Exception e) {
            Log.e(TAG, "getAllClasses: Query failed", e);
            if (cursor != null) cursor.close(); // Đóng cursor nếu query lỗi
            cursor = null;
        }
        // Không đóng db ở đây vì Cursor cần nó mở
        return cursor;
    }
}