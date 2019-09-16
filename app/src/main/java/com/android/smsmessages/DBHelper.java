package com.android.smsmessages;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper {
    private SQLiteDatabase mDatabase;
    // 数据库文件名
    public static final String DB_NAME = "sms_forward.db";
    // 数据库表名
    public static final String TABLE_NAME = "dingtoken";
    // 数据库版本号
    public static final int DB_VERSION = 1;

    public static final String DingToken = "dingtoken";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mDatabase = this.getWritableDatabase();
    }

    // 当数据库文件创建时，执行初始化操作，并且只执行一次
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 建表
        String sql = "create table " +
                TABLE_NAME +
                "(_id integer primary key autoincrement, " +
                DingToken + " varchar" +")";
        db.execSQL(sql);

    }

    // 当数据库版本更新执行该方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void initData() {
        if (queryData() == "") {
            ContentValues values = new ContentValues();
            values.put(DBHelper.DingToken, "9d79c3e0b7fbe31cc30561d7f307956debc190df6347c74d5c8a2c5ef73a4f7e");
            mDatabase.insert(DBHelper.TABLE_NAME, null, values);
        }
    }

    public int updateData(String dingToken) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.DingToken, dingToken);
        int count = mDatabase.update(DBHelper.TABLE_NAME, values, null, null);
        return count;
    }

    public String queryData() {
        Cursor cursor = mDatabase.query(DBHelper.TABLE_NAME,
                new String[]{DBHelper.DingToken},
                null,
                null,
                null,
                null,
                null);

        int nameIndex = cursor.getColumnIndex(DBHelper.DingToken);
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            return "";
        }
        String dingToken = cursor.getString(nameIndex);
        return dingToken;
    }
}
