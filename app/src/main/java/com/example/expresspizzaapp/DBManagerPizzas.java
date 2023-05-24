package com.example.expresspizzaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DBManagerPizzas {
    private DBHelperPizzas dbHelperPizzas;
    private Context context;
    private SQLiteDatabase db;

    // constructor
    public DBManagerPizzas(Context c) {
        context = c;
    }

    // open / close
    public DBManagerPizzas open() throws SQLException {
        dbHelperPizzas = new DBHelperPizzas(context);
        db = dbHelperPizzas.getWritableDatabase();

        initMenu();

        return this;
    }

    public void close() {
        deleteRecords();
        dbHelperPizzas.close();
    }

    public void insertPizza(String pizza, double price) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelperPizzas.PIZZA, pizza);
        contentValues.put(DBHelperPizzas.PRICE, price);

        db.insert(DBHelperPizzas.TABLE_NAME, null, contentValues);
    }

    public String[][] getPizzas() {
        String [][] pizzas = new String[8][2];
        Cursor c = db.rawQuery("SELECT * FROM PIZZAS", null);

        int i = 1;

        if (c != null ) {
            c.moveToFirst();

            String pizza = c.getString(1);
            String points = c.getDouble(2) + "";

            pizzas[0][0] = pizza;
            pizzas[0][1] = points;

            while (c.moveToNext())
            {
                pizza = c.getString(1);
                points = c.getDouble(2) + "";

                pizzas[i][0] = pizza;
                pizzas[i][1] = points;

                i++;
            }
        }

        return pizzas;
    }

    public void initMenu() {
        try {
            InputStream is = context.getAssets().open("pizzas.csv");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String delimiter = ",";
            String data;
            String row[];

            while ((data = br.readLine()) != null) {
                row = data.split(delimiter);
                insertPizza(row[0], Double.parseDouble(row[1]));
                Log.d("ADDED TO DB: ", row[0]);
            }

            br.close();
            isr.close();
        } catch (IOException e) {
            Log.d("File error",e.getMessage());
        }
    }

    public void deleteRecords() {
        db.execSQL("DELETE FROM PIZZAS");
    }
}