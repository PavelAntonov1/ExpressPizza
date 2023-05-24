package com.example.expresspizzaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBManagerUsers {
    private DBHelperUsers dbHelperUsers;
    private Context context;
    private SQLiteDatabase db;

    // constructor
    public DBManagerUsers(Context c) {
        context = c;
    }

    // open / close
    public DBManagerUsers open() throws SQLException {
        dbHelperUsers = new DBHelperUsers(context);
        db = dbHelperUsers.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelperUsers.close();
    }

    public String[] findUser(String email) {
        Cursor c = db.rawQuery("SELECT * FROM USERS WHERE EMAIL = ?", new String[] {
                email
        });

        if (c.getCount() > 0) {
            c.moveToFirst();
            return new String[] {c.getString(0)};
        } else {
            return new String[] { };
        }
    }

    // user authorization without password
    public boolean authorizeUser(String email) {
        Cursor c = db.rawQuery("SELECT * FROM USERS WHERE EMAIL = ?", new String[] {
                email
        });

        if (c.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    // user authorization with password
    public boolean authorizeUserPassword(String email, String password) {
        Cursor c = db.rawQuery("SELECT * FROM USERS WHERE EMAIL = ? AND PASSWORD = ?", new String[] {
                email, password
        });

        if (c.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    // user info extraction
    public String[] getUserInfo(String email) {
        Cursor c = db.rawQuery("SELECT * FROM USERS WHERE EMAIL = ?", new String[] {
                email
        });

        if (c.getCount() > 0) {
            c.moveToFirst();

            String userName = c.getString(1);
            String userPoints = c.getDouble(4) + "";
            String userDiscount = c.getInt(5) + "";

            return new String[] {userName, userPoints, userDiscount};
        } else {
            return new String[] { };
        }
    }

    // user addition
    public void insertUser(String name, String email, String password, double points, int discount) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelperUsers.NAME, name);
        contentValues.put(DBHelperUsers.EMAIL, email);
        contentValues.put(DBHelperUsers.PASSWORD, password);
        contentValues.put(DBHelperUsers.POINTS, points);
        contentValues.put(DBHelperUsers.DISCOUNT, discount);

        db.insert(DBHelperUsers.TABLE_NAME, null, contentValues);
    }

    // user points update
    public void updateUserPoints(String email, double points) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelperUsers.POINTS, points);

        String id = findUser(email)[0];

        db.update(DBHelperUsers.TABLE_NAME, contentValues, DBHelperUsers.ID + " = "
        + id, null);
    }
}
