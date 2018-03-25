package com.waldou.momourulib.framework.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.waldou.momourulib.framework.LibraryItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Waldo Urribarri - www.waldou.com
 *
 * This is a helper class to interface with the SQLLite database
 * of the Library.
 *
 */
public class LibraryDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Library.db";

    public LibraryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + LibraryItemContract.LibraryItemEntry.TABLE_NAME + " ("
                + LibraryItemContract.LibraryItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + LibraryItemContract.LibraryItemEntry.ID + " TEXT NOT NULL,"
                + LibraryItemContract.LibraryItemEntry.CODE + " TEXT NOT NULL,"
                + LibraryItemContract.LibraryItemEntry.CATEGORY + " TEXT NOT NULL,"
                + LibraryItemContract.LibraryItemEntry.NAME + " TEXT NOT NULL,"
                + LibraryItemContract.LibraryItemEntry.DESCRIPTION + " TEXT NOT NULL,"
                + LibraryItemContract.LibraryItemEntry.KEYWORDS + " TEXT,"
                + "UNIQUE (" + LibraryItemContract.LibraryItemEntry.ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO
    }

    /**
     * Inserts data to the database.
     * @param item Library item to save.
     * @return <b>true</b> if data was successfully inserted.
     */
    public boolean insertItem(LibraryItem item) {
        SQLiteDatabase db = getWritableDatabase();
        return (db.insert(LibraryItemContract.LibraryItemEntry.TABLE_NAME, null,
                LibraryItemContract.toContentValues(item)) == -1) ? false : true;
    }

    /**
     * Delets data from the database.
     * @param item Library item to delete.
     * @return Number of deleted rows.
     */
    public int deleteItem(LibraryItem item) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(LibraryItemContract.LibraryItemEntry.TABLE_NAME, "ID = ?", new String[] { item.getId() });
    }

    /**
     * Get list of all items from the database.
     * @return List of items.
     */
    public List<LibraryItem> getAllItems() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + LibraryItemContract.LibraryItemEntry.TABLE_NAME, null);

        List<LibraryItem> list = new ArrayList<LibraryItem>();
        if(result.getCount() > 0) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<String>>(){}.getType();
            while(result.moveToNext()) {
                int categoryId = result.getColumnIndex(LibraryItemContract.LibraryItemEntry.CATEGORY);
                int nameId = result.getColumnIndex(LibraryItemContract.LibraryItemEntry.NAME);
                int descriptionId = result.getColumnIndex(LibraryItemContract.LibraryItemEntry.DESCRIPTION);
                int codeId = result.getColumnIndex(LibraryItemContract.LibraryItemEntry.CODE);
                int keywordsId = result.getColumnIndex(LibraryItemContract.LibraryItemEntry.KEYWORDS);

                String category = result.getString(categoryId);
                String name = result.getString(nameId);
                String description = result.getString(descriptionId);
                String code = result.getString(codeId);
                List<String> keywords = gson.fromJson(result.getString(keywordsId), type);

                LibraryItem item = new LibraryItem(category, name, description, code, keywords);
                list.add(item);
            }
        }

        return list;
    }

}
