package com.example.expresspizzaapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelperOrders extends SQLiteOpenHelper {
    // table name
    public static final String TABLE_NAME = "ORDERS";

    // table columns
    public static final String ID = "_id";
    public static final String DESCRIPTION = "description";
    public static final String PRICE = "price";
    public static final String ADDRESS = "address";
    public static final String EMAIL = "email";

    // db filename
    static final String DB_NAME = "Orders.DB";

    // db version
    static final int DB_VERSION = 2;

    // table creation query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + DESCRIPTION + " TEXT NOT NULL, " + PRICE + " REAL, " + ADDRESS + " TEXT NOT NULL, "
            + EMAIL + " TEXT NOT NULL );";

    // constructor
    public DBHelperOrders(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDB) {
        Log.d("DBHelperOrders", "onCreate");
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