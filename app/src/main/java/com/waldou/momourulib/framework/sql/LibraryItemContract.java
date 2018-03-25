package com.waldou.momourulib.framework.sql;

import android.content.ContentValues;
import android.provider.BaseColumns;

import com.google.gson.Gson;
import com.waldou.momourulib.framework.LibraryItem;

/**
 * @author Waldo Urribarri - www.waldou.com
 *
 *
 */
public class LibraryItemContract {

    public static abstract class LibraryItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "items";

        public static final String ID = "id";
        public static final String CODE = "code";
        public static final String CATEGORY = "category";
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
        public static final String KEYWORDS = "keywords";
    }

    public static ContentValues toContentValues(LibraryItem item) {
        ContentValues values = new ContentValues();
        values.put(LibraryItemContract.LibraryItemEntry.ID, item.getId());
        values.put(LibraryItemContract.LibraryItemEntry.CODE, item.getCode());
        values.put(LibraryItemContract.LibraryItemEntry.CATEGORY, item.getCategory());
        values.put(LibraryItemContract.LibraryItemEntry.NAME, item.getName());
        values.put(LibraryItemContract.LibraryItemEntry.DESCRIPTION, item.getDescription());
        Gson gson = new Gson();
        values.put(LibraryItemContract.LibraryItemEntry.KEYWORDS, gson.toJson(item.getKeywords()));
        return values;
    }

}
