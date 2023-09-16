package com.theshekharmaharaj.contactmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ContactsDatabase.db";
    private static final int DATABASE_VERSION = 1;

    // Define the table and column names
    public static final String TABLE_CONTACTS = "Contacts";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_EMAIL = "Email";
    public static final String COLUMN_PHONE_NUMBER = "PhoneNumber";
    public static final String COLUMN_BIRTHDAY = "Birthday";

    // SQL statement to create the "Contacts" table
    private static final String DATABASE_CREATE = "create table " + TABLE_CONTACTS + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_EMAIL + " text, "
            + COLUMN_PHONE_NUMBER + " text, "
            + COLUMN_BIRTHDAY + " text);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        // Create the database table
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implement database upgrade if needed
        // This method is called when the database version changes
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
    }
}

