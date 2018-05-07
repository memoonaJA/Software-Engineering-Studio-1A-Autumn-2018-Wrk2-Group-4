package com.example.softwaregroup4.group4_fitnessapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Manny on 13/4/18.
 */

public class UserAccountDatabase extends SQLiteOpenHelper {

    private static final int db_vers = 1;
    private static final String db_users = "Users.db";
    private static final String TableName = "AppUsers";
    private static final String COL_ID = "ID";
    private static final String COL_FirstName = "FirstName";
    private static final String COL_LastName = "LastName";
    private static final String COL_Email = "Email";
    private static final String COL_Username = "Username";
    private static final String COL_Password = "Password";
    private static final String CreateTable = ("create table " + TableName + "(ID INTEGER PRIMARY KEY NOT NULL, FirstName TEXT NOT NULL, LastName TEXT NOT NULL, Email TEXT NOT NULL, Username TEXT NOT NULL, Password TEXT NOT NULL)");

    SQLiteDatabase db;


    public UserAccountDatabase(Context context) {
        super(context, db_users, null, db_vers);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CreateTable);
        this.db = db;
    }

    public void addUser(User user) {

        db = this.getWritableDatabase();
        ContentValues VALUES = new ContentValues();

        String countQuery = "select * from " + TableName;
        Cursor cursor = db.rawQuery(countQuery, null);
        int userCount = cursor.getCount();

        VALUES.put(COL_ID, userCount);
        VALUES.put(COL_FirstName, user.getFirstName());
        VALUES.put(COL_LastName, user.getLastName());
        VALUES.put(COL_Email, user.getEmail());
        VALUES.put(COL_Username, user.getUsername());
        VALUES.put(COL_Password, user.getPassword());

        db.insert(TableName, null, VALUES);
        db.close();

    }

    public String searchForPassword(String username) {
        db = this.getReadableDatabase();
        String TableQuery = "select * from " + TableName;
        Cursor cursor = db.rawQuery(TableQuery, null);
        String userNAME, passWORD;
        passWORD = "No stored value in database";
        if (cursor.moveToFirst()) {
            do {
                userNAME = cursor.getString(4);


                if (userNAME.equals(username)) {
                    passWORD = cursor.getString(5);
                    break;
                }
            }
            while (cursor.moveToNext());
        }

        return passWORD;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String TableQuery = "DROP TABLE IF EXISTS " + TableName;
        db.execSQL(TableQuery);
        this.onCreate(db);
    }
}