package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.Contect.VideoContect;

import java.util.ArrayList;
import java.util.List;

public class SQLHelper extends SQLiteOpenHelper {
    private static final String TAG = "SQLHelper";
    static final String DB_NAME = "Product.db";
    static final String DB_NAME_TABLE = "Product";
    static final int DB_VERSION = 5;

    SQLiteDatabase sqLiteDatabase;
    ContentValues contentValues;
    Cursor cursor;

    public SQLHelper(@Nullable Context context) {
        super(context, DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryCreaTable = "CREATE TABLE Product (" + "id INTEGER ,"
                +"title Text,"
                +"date Text,"
                +"link Text,"
                +"avatar Text ) ";
        db.execSQL(queryCreaTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("drop table if exists " + DB_NAME_TABLE);
            onCreate(db);
        }
    }
    public void insertProduct(String id,String avatar,String link,String date,String title){
        sqLiteDatabase = getWritableDatabase();
        contentValues = new ContentValues();
        contentValues.put("id",id);
        contentValues.put("avatar",avatar);
        contentValues.put("link",link);
        contentValues.put("date",date);
        contentValues.put("title",title);

        sqLiteDatabase.insert(DB_NAME_TABLE,null,contentValues);
        closeDB();
    }
    public List<VideoContect> getAllProductAdvanced(){
        List<VideoContect> videoContects = new ArrayList<>();
        VideoContect contect;

        sqLiteDatabase = getReadableDatabase();
        cursor = sqLiteDatabase.query(false, DB_NAME_TABLE, null, null, null
                , null, null, null, null);
        while (cursor.moveToNext()){
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String avatar = cursor.getString(cursor.getColumnIndex("avatar"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String link = cursor.getString(cursor.getColumnIndex("link"));
            contect = new VideoContect(title,date,avatar,link,id);
            videoContects.add(contect);
        }
        closeDB();
        return videoContects;
    }

    private void closeDB() {
        if (sqLiteDatabase != null) sqLiteDatabase.close();
        if (contentValues != null) contentValues.clear();
        if (cursor != null) cursor.close();
    }
}
