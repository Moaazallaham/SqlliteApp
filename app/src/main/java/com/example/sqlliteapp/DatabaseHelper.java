package com.example.sqlliteapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.view.View;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper{

    public static final int dbVersion=1;
    public static final String DATABASE_NAME="student.db";
    public static final String TABLE_NAME="student_table";
    public static final String COL1="ID";
    public static final String COL2="Name";
    public static final String COL3 = "Date";
    public static final String COL4 = "Time";
    public static final String COL5 = "Image";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 5);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table "+ TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,DATE TEXT,TIME TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);

    }
    public boolean insertData(String name,String date,String time)
    {
        SQLiteDatabase db=this. getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(COL2,name);
        contentValues.put(COL3,date);
        contentValues.put(COL4,time);




        long resault = db.insert(TABLE_NAME,null,contentValues);

        if (resault==-1)
            return false;
        else
            return true;


    }
    public Cursor getAllData()
    {
        SQLiteDatabase db=this. getWritableDatabase();
        Cursor res= db.rawQuery("select * from "+ TABLE_NAME,null);
        return res;
    }






    public boolean updateData(String id, String name,String date,String time)
    {
        SQLiteDatabase db=this. getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(COL1,id);
        contentValues.put(COL2,name);
        contentValues.put(COL3,date);
        contentValues.put(COL4,time);


        db.update(TABLE_NAME,contentValues,"id = ?",new String[]{id});
        return true;

    }
    public Integer deleteData(String id)
    {
        SQLiteDatabase db=this. getWritableDatabase();
        return db.delete(TABLE_NAME,"ID = ?",new String[] {id});

    }
}
