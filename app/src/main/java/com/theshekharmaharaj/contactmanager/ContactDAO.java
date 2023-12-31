package com.theshekharmaharaj.contactmanager;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContactDAO {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private Context context;

    public ContactDAO(Context ctx) {
        context = ctx;
    }

    // Open the database for writing
    public ContactDAO open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    // Close the database
    public void close() {
        dbHelper.close();
    }

    // Insert a new contact into the database
    public long insertContact(ContactModel contact) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, contact.getName());
        values.put(DatabaseHelper.COLUMN_EMAIL, contact.getEmail());
        values.put(DatabaseHelper.COLUMN_PHONE_NUMBER, contact.getPhoneNumber());
        values.put(DatabaseHelper.COLUMN_BIRTHDAY, contact.getBirthday());

        return database.insert(DatabaseHelper.TABLE_CONTACTS, null, values);
    }

    // Update an existing contact's information
    public int updateContact(ContactModel contact) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, contact.getName());
        values.put(DatabaseHelper.COLUMN_EMAIL, contact.getEmail());
        values.put(DatabaseHelper.COLUMN_PHONE_NUMBER, contact.getPhoneNumber());
        values.put(DatabaseHelper.COLUMN_BIRTHDAY, contact.getBirthday());

        return database.update(
                DatabaseHelper.TABLE_CONTACTS,
                values,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(contact.getId())});
    }

    // Delete a contact from the database
    public void deleteContact(long contactId) {
        database.delete(
                DatabaseHelper.TABLE_CONTACTS,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(contactId)});
    }

    // Retrieve a list of all contacts from the database
    public List<ContactModel> getAllContacts() {
        List<ContactModel> contactList = new ArrayList<>();
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_CONTACTS,
                null,
                null,
                null,
                null,
                null,
                null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                ContactModel contact = cursorToContact(cursor);
                contactList.add(contact);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return contactList;
    }

    // Query contacts by a specific attribute (e.g., name, email, phone number, or birthday)
    public List<ContactModel> getContactsByAttribute(String columnName, String attributeValue) {
        List<ContactModel> contactList = new ArrayList<>();
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_CONTACTS +
                " WHERE " + columnName + " = ?";
        Cursor cursor = database.rawQuery(query, new String[]{attributeValue});

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                ContactModel contact = cursorToContact(cursor);
                contactList.add(contact);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return contactList;
    }

    public List<ContactModel> sortContacts(String sortBy) {
        List<ContactModel> contactList = getAllContacts();

        switch (sortBy) {
            case "Name":
                contactList.sort(Comparator.comparing(ContactModel::getName));
                break;

            case "Email Address":
                contactList.sort(Comparator.comparing(ContactModel::getEmail));
                break;

            case "Birthday":
                contactList.sort(Comparator.comparing(ContactModel::getBirthday));
                break;

            case "None":
            default:
                // No sorting needed
                break;
        }

        return contactList;
    }

    // Search contacts by name or email
    public List<ContactModel> searchContacts(String query) {
        List<ContactModel> contactList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_NAME,
                DatabaseHelper.COLUMN_EMAIL,
                DatabaseHelper.COLUMN_PHONE_NUMBER,
                DatabaseHelper.COLUMN_BIRTHDAY
        };

        // Define the WHERE clause to search by name or email
        String selection = DatabaseHelper.COLUMN_NAME + " LIKE ? OR " +
                DatabaseHelper.COLUMN_EMAIL + " LIKE ?";

        // Arguments for the WHERE clause
        String[] selectionArgs = {"%" + query + "%", "%" + query + "%"};

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_CONTACTS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                ContactModel contact = cursorToContact(cursor);
                contactList.add(contact);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return contactList;
    }

    // Disable suppression to show IDE errors
    @SuppressLint("Range")
    private ContactModel cursorToContact(Cursor cursor) {
        ContactModel contact = new ContactModel();
        contact.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
        contact.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)));
        contact.setEmail(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL)));
        contact.setPhoneNumber(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE_NUMBER)));
        contact.setBirthday(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_BIRTHDAY)));
        return contact;
    }
}

