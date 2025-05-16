package com.example.lab4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "QuanLySinhVien.db"; // Giữ nguyên hoặc đổi tùy ý
    private static final int DATABASE_VERSION = 2; // *** TĂNG VERSION LÊN 2 VÌ THAY ĐỔI CẤU TRÚC ***

    // --- Thông tin bảng Lớp học (tbllop) ---
    public static final String TABLE_LOP = "tbllop";
    public static final String LOP_COL_ID = "_id";
    public static final String LOP_COL_CODE = "malop";
    public static final String LOP_COL_NAME = "tenlop";
    public static final String LOP_COL_COUNT = "siso";

    // --- Thông tin bảng Người dùng (tbluser) ---
    public static final String TABLE_USER = "tbluser";
    public static final String USER_COL_ID = "_id";
    public static final String USER_COL_USERNAME = "username";
    public static final String USER_COL_PASSWORD = "password"; // Sẽ lưu mật khẩu đã mã hóa

    // --- Câu lệnh tạo bảng ---
    private static final String CREATE_TABLE_LOP =
            "CREATE TABLE " + TABLE_LOP + " (" +
                    LOP_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    LOP_COL_CODE + " TEXT UNIQUE NOT NULL, " +
                    LOP_COL_NAME + " TEXT NOT NULL, " +
                    LOP_COL_COUNT + " INTEGER)";

    private static final String CREATE_TABLE_USER =
            "CREATE TABLE " + TABLE_USER + " (" +
                    USER_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    USER_COL_USERNAME + " TEXT UNIQUE NOT NULL, " + // Username không null và duy nhất
                    USER_COL_PASSWORD + " TEXT NOT NULL)";         // Mật khẩu (đã mã hóa) không null

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "DatabaseHelper: Constructor called. Version: " + DATABASE_VERSION);
    }

    // Gọi khi CSDL được tạo lần đầu (hoặc sau khi xóa)
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: Creating database tables...");
        db.execSQL(CREATE_TABLE_LOP); // Tạo bảng lớp học
        Log.i(TAG, "onCreate: Table " + TABLE_LOP + " created.");
        db.execSQL(CREATE_TABLE_USER); // Tạo bảng người dùng
        Log.i(TAG, "onCreate: Table " + TABLE_USER + " created.");
    }

    // Gọi khi nâng cấp CSDL (thay đổi DATABASE_VERSION từ cũ lên mới)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "onUpgrade: Upgrading database from version " + oldVersion + " to " + newVersion);
        // Xóa các bảng cũ
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOP);
        Log.w(TAG, "onUpgrade: Dropped table " + TABLE_LOP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        Log.w(TAG, "onUpgrade: Dropped table " + TABLE_USER);
        // Tạo lại cấu trúc mới
        onCreate(db);
    }

    // --- Các phương thức CRUD cho bảng Lớp học (tbllop) - Giữ nguyên như Bài 4.1 ---

    public long insertClass(String code, String name, int count) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LOP_COL_CODE, code);
        values.put(LOP_COL_NAME, name);
        values.put(LOP_COL_COUNT, count);
        long newRowId = -1;
        try {
            newRowId = db.insertOrThrow(TABLE_LOP, null, values);
            Log.i(TAG, "insertClass: Success for code " + code + ", ID: " + newRowId);
        } catch (android.database.sqlite.SQLiteConstraintException e) {
            Log.e(TAG, "insertClass: Failed for code " + code + ". Constraint violation.", e);
        } catch (Exception e) {
            Log.e(TAG, "insertClass: Failed for code " + code, e);
        } finally {
            db.close();
        }
        return newRowId;
    }

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

    public Cursor getAllClasses() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_LOP, null, null, null,
                    null, null, null);
            Log.d(TAG, "getAllClasses: Query executed. Row count: " +
                    (cursor != null ? cursor.getCount() : "null cursor"));
        } catch (Exception e) {
            Log.e(TAG, "getAllClasses: Query failed", e);
            if (cursor != null) cursor.close();
            cursor = null;
        }
        return cursor;
    }

    // --- Các phương thức cho bảng Người dùng (tbluser) ---

    /**
     * Đăng ký người dùng mới.
     * @param username Tên đăng nhập (phải là duy nhất).
     * @param hashedPassword Mật khẩu đã được mã hóa (ví dụ: MD5).
     * @return ID của người dùng mới nếu thành công, -1 nếu tên đăng nhập đã tồn tại, -2 nếu có lỗi khác.
     */
    public long registerUser(String username, String hashedPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_COL_USERNAME, username);
        values.put(USER_COL_PASSWORD, hashedPassword);

        long newUserId = -2; // Mặc định là lỗi không xác định
        try {
            newUserId = db.insertOrThrow(TABLE_USER, null, values); // insertOrThrow để bắt lỗi UNIQUE
            Log.i(TAG, "registerUser: Success for username " + username + ", ID: " + newUserId);
        } catch (android.database.sqlite.SQLiteConstraintException e) {
            Log.e(TAG, "registerUser: Failed for username " + username + ". Username already exists.", e);
            newUserId = -1; // Lỗi -1: Username đã tồn tại
        } catch (Exception e) {
            Log.e(TAG, "registerUser: Failed for username " + username + ". Unknown error.", e);
            newUserId = -2; // Lỗi -2: Lỗi khác
        } finally {
            db.close();
        }
        return newUserId;
    }

    /**
     * Lấy thông tin người dùng dựa vào username.
     * Chủ yếu để kiểm tra đăng nhập (lấy mật khẩu đã hash).
     * @param username Tên đăng nhập cần tìm.
     * @return Cursor trỏ đến dữ liệu người dùng (chỉ 1 dòng nếu tìm thấy), hoặc null nếu lỗi.
     * Cursor chứa các cột của bảng User. Cần đóng Cursor sau khi sử dụng.
     */
    public Cursor getUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        String selection = USER_COL_USERNAME + " = ?";
        String[] selectionArgs = { username };

        try {
            cursor = db.query(
                    TABLE_USER,         // Tên bảng
                    null,               // Các cột cần lấy (null là tất cả)
                    selection,          // Điều kiện WHERE
                    selectionArgs,      // Giá trị cho điều kiện WHERE
                    null,               // GROUP BY
                    null,               // HAVING
                    null                // ORDER BY
            );
            Log.d(TAG, "getUser: Query executed for username " + username + ". Row count: " + (cursor != null ? cursor.getCount() : "null cursor"));
        } catch (Exception e) {
            Log.e(TAG, "getUser: Query failed for username " + username, e);
            if (cursor != null) cursor.close();
            cursor = null;
        }
        // Không đóng db ở đây
        return cursor;
    }

    /**
     * (Optional) Kiểm tra xem username đã tồn tại hay chưa.
     * @param username Tên đăng nhập cần kiểm tra.
     * @return true nếu tồn tại, false nếu không tồn tại hoặc lỗi.
     */
    public boolean checkUserExists(String username) {
        Cursor cursor = getUser(username);
        boolean exists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) {
            cursor.close(); // Nhớ đóng cursor sau khi kiểm tra
        }
        Log.d(TAG, "checkUserExists: Username '" + username + "' exists: " + exists);
        return exists;
    }
}