package com.example.medialert;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "MedicineDB";
    private static final int DB_VERSION = 8;   // version increase kelay

    private static final String TABLE = "medicines";
    private static final String USER_TABLE = "users";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // ================= MEDICINES TABLE =================
        db.execSQL("CREATE TABLE " + TABLE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "dose TEXT, " +
                "time TEXT, " +
                "repeatType TEXT)");

        // ================= USERS TABLE (Intermediate Level) =================
        db.execSQL("CREATE TABLE " + USER_TABLE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "fullName TEXT, " +
                "email TEXT UNIQUE, " +
                "mobile TEXT, " +
                "dob TEXT, " +
                "gender TEXT, " +
                "bloodGroup TEXT, " +
                "password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        onCreate(db);
    }

    // ================= USER METHODS =================

    // Insert User (Intermediate Fields)
    public boolean insertUser(String fullName, String email, String mobile,
                              String dob, String gender, String bloodGroup,
                              String password) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("fullName", fullName);
        cv.put("email", email);
        cv.put("mobile", mobile);
        cv.put("dob", dob);
        cv.put("gender", gender);
        cv.put("bloodGroup", bloodGroup);
        cv.put("password", password);

        long result = db.insert(USER_TABLE, null, cv);
        db.close();

        return result != -1;
    }

    // Login Check (Email + Password)
    public boolean checkUser(String email, String password) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + USER_TABLE +
                        " WHERE email=? AND password=?",
                new String[]{email, password}
        );

        boolean exists = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return exists;
    }

    // Check Email Already Exists
    public boolean isEmailExists(String email) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + USER_TABLE +
                        " WHERE email=?",
                new String[]{email}
        );

        boolean exists = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return exists;
    }

    // Get Total User Count (App Usage Tracking)
    public int getUserCount() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE, null);

        int count = cursor.getCount();

        cursor.close();
        db.close();

        return count;
    }

    // ================= MEDICINE METHODS =================

    public long addMedicine(String name, String dose, String time, String repeat) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("dose", dose);
        cv.put("time", time);
        cv.put("repeatType", repeat);

        long id = db.insert(TABLE, null, cv);

        db.close();
        return id;
    }

    public ArrayList<MedicineModel> getAllMedicines() {

        ArrayList<MedicineModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE + " ORDER BY id DESC", null);

        if (c.moveToFirst()) {
            do {
                int id = c.getInt(c.getColumnIndexOrThrow("id"));
                String name = c.getString(c.getColumnIndexOrThrow("name"));
                String dose = c.getString(c.getColumnIndexOrThrow("dose"));
                String time = c.getString(c.getColumnIndexOrThrow("time"));
                String repeat = c.getString(c.getColumnIndexOrThrow("repeatType"));

                list.add(new MedicineModel(id, name, dose, time, repeat));

            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return list;
    }

    public void deleteMedicine(int id) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }
}