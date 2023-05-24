package com.example.expresspizzaapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelperUsers extends SQLiteOpenHelper {
    // table name
    public static final String TABLE_NAME = "USERS";

    // table columns
    public static final String ID = "_id";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String POINTS = "points";
    public static final String DISCOUNT = "discount"; // 0 - false, 1 - true

    // db filename
    static final String DB_NAME = "Users.DB";

    // db version
    static final int DB_VERSION = 1;

    // table creation query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT NOT NULL, " + EMAIL +
            " TEXT NOT NULL, " + PASSWORD +
            " TEXT NOT NULL, " + POINTS + " REAL, " + DISCOUNT + " REAL );";

    // constructor
    public DBHelperUsers(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDB) {
        Log.d("DBHelperUsers", "onCreate");
        sqLiteDB.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDB, int oldVersion, int newVersion) {
        Log.d("version change", "oldVersion: " + oldVersion + ", newVersion: " +
        newVersion);
        sqLiteDB.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDB);
    }
}
