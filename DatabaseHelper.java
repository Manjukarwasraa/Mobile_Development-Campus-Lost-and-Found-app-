package com.example.project1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "campus_lost_found.db";
    private static final int DB_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE items (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "type TEXT NOT NULL," +
                "title TEXT NOT NULL," +
                "description TEXT NOT NULL," +
                "category TEXT," +
                "date TEXT," +
                "imageUri TEXT," +
                "phone TEXT," +
                "email TEXT," +
                "resolved INTEGER DEFAULT 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS items");
        onCreate(db);
    }

    public long addItem(Item item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("type", item.type);
        values.put("title", item.title);
        values.put("description", item.description);
        values.put("category", item.category);
        values.put("date", item.date);
        values.put("imageUri", item.imageUri);
        values.put("phone", item.phone);
        values.put("email", item.email);
        values.put("resolved", 0);
        return db.insert("items", null, values);
    }

    public ArrayList<Item> getAllItems() {
        return getItems("", null);
    }

    public ArrayList<Item> getActiveItems() {
        return getItems("resolved = 0", null);
    }

    public ArrayList<Item> getItemsByType(String type) {
        return getItems("type = ?", new String[]{type});
    }

    public ArrayList<Item> getResolvedItems() {
        return getItems("resolved = 1", null);
    }

    private ArrayList<Item> getItems(String selection, String[] args) {
        ArrayList<Item> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("items", null, selection.isEmpty() ? null : selection,
                args, null, null, "id DESC");

        while (cursor.moveToNext()) {
            list.add(cursorToItem(cursor));
        }
        cursor.close();
        return list;
    }

    public Item getItem(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("items", null, "id = ?", new String[]{String.valueOf(id)},
                null, null, null);
        Item item = null;
        if (cursor.moveToFirst()) {
            item = cursorToItem(cursor);
        }
        cursor.close();
        return item;
    }

    public boolean markResolved(int id, int resolved) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("resolved", resolved);
        return db.update("items", values, "id = ?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean deleteItem(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete("items", "id = ?", new String[]{String.valueOf(id)}) > 0;
    }

    private Item cursorToItem(Cursor c) {
        return new Item(
                c.getInt(c.getColumnIndexOrThrow("id")),
                c.getString(c.getColumnIndexOrThrow("type")),
                c.getString(c.getColumnIndexOrThrow("title")),
                c.getString(c.getColumnIndexOrThrow("description")),
                c.getString(c.getColumnIndexOrThrow("category")),
                c.getString(c.getColumnIndexOrThrow("date")),
                c.getString(c.getColumnIndexOrThrow("imageUri")),
                c.getString(c.getColumnIndexOrThrow("phone")),
                c.getString(c.getColumnIndexOrThrow("email")),
                c.getInt(c.getColumnIndexOrThrow("resolved"))
        );
    }
}
