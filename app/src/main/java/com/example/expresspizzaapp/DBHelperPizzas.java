package com.example.expresspizzaapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelperPizzas extends SQLiteOpenHelper {
    // table name
    public static final String TABLE_NAME = "PIZZAS";

    // table columns
    public static final String ID = "_id";
    public static final String PIZZA = "pizza";
    public static final String PRICE = "price";

    // db filename
    static final String DB_NAME = "Pizzas.DB";

    // db version
    static final int DB_VERSION = 1;

    // table creation query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + PIZZA + " TEXT NOT NULL, " + PRICE + " REAL );";

    // constructor
    public DBHelperPizzas(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDB) {
        Log.d("DBHelperPizzas", "onCreate");
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