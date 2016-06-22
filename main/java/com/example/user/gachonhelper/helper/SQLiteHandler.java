package com.example.user.gachonhelper.helper;

/**
 * Created by wangki on 2016-05-07.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    //DB버전
    private static final int DATABASE_VERSION = 1;

    //DB이름
    private static final String DATABASE_NAME = "HELPER";

    //테이블 이름
    private static final String TABLE_USER = "user";

    //컬럼
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_STUDENTID = "stu_id";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_TICKETS = "tickets";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";


    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 테이블 생성
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"  + KEY_NAME + " TEXT,"
                + KEY_STUDENTID + " TEXT," + KEY_UID + " TEXT," + KEY_CATEGORY + " TEXT,"+ KEY_TICKETS + " TEXT," + KEY_CREATED_AT + " TEXT" +")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "테이블 생성 완료");
    }

    //DB 업데이트
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        //테이블 생성
        onCreate(db);
    }

    /**
     * 유저 테이블 데이터 추가하기
     * */
    public void addUser(String name, String stu_id, String uid,String category,String tickets, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, name); // Name
        values.put(KEY_STUDENTID, stu_id); // Student ID
        values.put(KEY_UID, uid);
        values.put(KEY_CATEGORY,category); // category
        values.put(KEY_TICKETS,tickets); // number of tickets;
        values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "새로운 유저가 등록되었습니다.: " + id);
    }

    /**
     * 유저 정보 가져오기
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("stu_id", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("category",cursor.getString(4));
            user.put("tickets",cursor.getString(5));
            user.put("created_at", cursor.getString(6));
        }
        cursor.close();
        db.close();
        Log.d(TAG, "Sqlite에서 가져온 정보: " + user.toString());
        return user;
    }

    /**
     * 데이터베이스 삭제
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // 모든 row 삭제
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "모든 데이터를 삭제하였습니다.");
    }
}
