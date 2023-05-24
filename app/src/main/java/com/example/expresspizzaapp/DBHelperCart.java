package com.example.expresspizzaapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelperCart extends SQLiteOpenHelper {
    // table name
    public static final String TABLE_NAME = "CART";

    // table columns
    public static final String ID = "_id";
    public static final String PIZZA = "pizza";
    public static final String QUANTITY = "quantity";
    public static final String PRICE = "price";
    public static final String EMAIL = "email";

    // db filename
    static final String DB_NAME = "Cart.DB";

    // db version
    static final int DB_VERSION = 1;

    // table creation query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + PIZZA + " TEXT NOT NULL, " + QUANTITY +
            " REAL, " + PRICE +
            " REAL, " + EMAIL + " TEXT NOT NULL );";

    // constructor
    public DBHelperCart(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDB) {
        Log.d("DBHelperCart", "onCreate");
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
