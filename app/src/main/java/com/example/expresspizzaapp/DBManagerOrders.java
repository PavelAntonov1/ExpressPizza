package com.example.expresspizzaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class DBManagerOrders {
    private DBHelperOrders dbHelperOrders;
    private Context context;
    private SQLiteDatabase db;

    // constructor
    public DBManagerOrders(Context c) {
        context = c;
    }

    // open / close
    public DBManagerOrders open() throws SQLException {
        dbHelperOrders = new DBHelperOrders(context);
        db = dbHelperOrders.getWritableDatabase();

        return this;
    }

    public void close() {
        dbHelperOrders.close();
    }

    public String[] findItem(String description, String email) {
        Cursor c = db.rawQuery("SELECT * FROM ORDERS WHERE DESCRIPTION = ? AND EMAIL = ?", new String[] {
                description, email
        });

        if (c.getCount() > 0) {
            c.moveToFirst();
            return new String[] {c.getString(0)};
        } else {
            return new String[] { };
        }
    }

    public void insertOrder(String description, double price, String address, String email) {
        Log.d("Search for item: ", description + " " + price + " " + email);

        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelperOrders.DESCRIPTION, description);
        contentValues.put(DBHelperOrders.PRICE, price);
        contentValues.put(DBHelperOrders.ADDRESS, address);
        contentValues.put(DBHelperOrders.EMAIL, email);

        db.insert(DBHelperOrders.TABLE_NAME, null, contentValues);
    }

    public ArrayList<ArrayList<String>> getOrders(String email) {
        ArrayList<ArrayList<String>>  orders = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM ORDERS WHERE EMAIL = ?", new String[] {email});

        if (c != null && c.moveToFirst()) {
            String id = c.getInt(0) + "";
            String description = c.getString(1);
            String price = c.getDouble(2) + "";
            String address = c.getString(3);

            ArrayList<String> order = new ArrayList<>();

            order.add(id);
            order.add(description);
            order.add(price);
            order.add(address);

            orders.add(order);

            while (c.moveToNext())
            {
                id = c.getInt(0) + "";
                description = c.getString(1);
                price = c.getDouble(2) + "";
                address = c.getString(3);

                order = new ArrayList<>();

                order.add(id);
                order.add(description);
                order.add(price);
                order.add(address);

                orders.add(order);
            }
        }

        return orders;
    }

    public void deleteItem(String id) {
//        String item[] = findItem(description, email);

        db.delete(DBHelperOrders.TABLE_NAME, DBHelperOrders.ID + " = "
                + id, null);
    }


    public void deleteRecords() {
        db.execSQL("DELETE FROM CART");
    }
}
