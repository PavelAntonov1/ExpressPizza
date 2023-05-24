package com.example.expresspizzaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class DBManagerCart {
    private DBHelperCart dbHelperCart;
    private Context context;
    private SQLiteDatabase db;

    // constructor
    public DBManagerCart(Context c) {
        context = c;
    }

    // open / close
    public DBManagerCart open() throws SQLException {
        dbHelperCart = new DBHelperCart(context);
        db = dbHelperCart.getWritableDatabase();

        return this;
    }

    public void close() {
        deleteRecords();
        dbHelperCart.close();
    }

    public void deleteAllItems() {
        deleteRecords();
    }

    public String[] findItem(String pizza, String email, double price) {
        Cursor c = db.rawQuery("SELECT * FROM CART WHERE PIZZA = ? AND EMAIL = ? AND PRICE = ?", new String[] {
                pizza, email, price + ""
        });

        if (c.getCount() > 0) {
            c.moveToFirst();
            return new String[] {c.getString(0), c.getString(2)};
        } else {
            return new String[] { };
        }
    }

    public void insertItem(String pizza, int quantity, double price, String email) {
        Log.d("Search for item: ", pizza + " " + price + " " + email);
        String item[] = findItem(pizza, email, price);

        if (item.length > 0) {
            Log.d("ITEM FOUND ", pizza + " in " + email);

            updateItemQuantity(item[0], Integer.parseInt(item[1]) + 1);
        } else {
            Log.d("ITEM NOT FOUND ", "");

            ContentValues contentValues = new ContentValues();

            contentValues.put(DBHelperCart.PIZZA, pizza);
            contentValues.put(DBHelperCart.PRICE, price);
            contentValues.put(DBHelperCart.QUANTITY, quantity);
            contentValues.put(DBHelperCart.EMAIL, email);

            db.insert(DBHelperCart.TABLE_NAME, null, contentValues);
        }
    }

    public ArrayList<ArrayList<String>> getItems(String email) {
        ArrayList<ArrayList<String>>  items = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM CART WHERE EMAIL = ?", new String[] {email});

        if (c != null && c.moveToFirst()) {
            String pizza = c.getString(1);
            String quantity = c.getInt(2) + "";
            String price = c.getDouble(3) + "";

            ArrayList<String> item = new ArrayList<>();

            item.add(pizza);
            item.add(quantity);
            item.add(price);

            items.add(item);

            while (c.moveToNext())
            {
                pizza = c.getString(1);
                quantity = c.getInt(2) + "";
                price = c.getDouble(3) + "";

                item = new ArrayList<>();

                item.add(pizza);
                item.add(quantity);
                item.add(price);

                items.add(item);
            }
        }

        return items;
    }

    public void deleteItem(String pizza, String email, double price) {
        String item[] = findItem(pizza, email, price);

        if (item.length <= 0) {
            Log.d("NOTHING TO DELETE", "");
        }

        if (item.length > 0) {
            updateItemQuantity(item[0], Integer.parseInt(item[1]) - 1);
        }
    }

    public void updateItemQuantity(String id, int quantity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelperCart.QUANTITY, quantity);

        Log.d("UPDATING ", quantity + "");

        if (quantity > 0) {
            db.update(DBHelperCart.TABLE_NAME, contentValues, DBHelperCart.ID + " = "
                    + id, null);
        } else if (quantity == 0) {
            db.delete(DBHelperCart.TABLE_NAME, DBHelperCart.ID + " = "
                    + id, null);
        }

        Log.d("UPDATED QUANTITY ", "");
    }

    public void deleteRecords() {
        db.execSQL("DELETE FROM CART");
    }
}
