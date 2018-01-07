/*******************************************************************************
 * Copyright 2017 Waldo Urribarri.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.waldou.momourulib;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.SharedPreferencesCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.waldou.momourulib.framework.LibraryItem;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Waldo Urribarri - www.waldou.com
 *
 * This class manages the favorite books, updating the
 * preferences file accordingly.
 *
 */
public class FavoritesManager {

    private static final String KEY_FAVORITES = "favorites";

    private SharedPreferences sharedPreferences;
    private ConcurrentHashMap<String, LibraryItem> favorites;

    // This value is used to determine if the favorites have changed
    private int version = 1;

    public FavoritesManager(Context ctx) {
        sharedPreferences = ctx.getSharedPreferences(Globals.APP_PREFERENCES, Context.MODE_PRIVATE);
        String strFavorites = sharedPreferences.getString(KEY_FAVORITES, null);
        if(strFavorites == null)
            favorites = new ConcurrentHashMap<String, LibraryItem>();
        else {
            try {
                Gson gson = new Gson();
                Type type = new TypeToken<ConcurrentHashMap<String, LibraryItem>>(){}.getType();
                favorites = gson.fromJson(strFavorites, type);
            } catch(Exception e) {
                // Corrupted favorites
                favorites = new ConcurrentHashMap<String, LibraryItem>();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(KEY_FAVORITES);
                SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
            }
        }
    }

    public LibraryItem get(String code) {
        return favorites.get(code);
    }

    public Collection<LibraryItem> getAll() {
        return favorites.values();
    }

    public synchronized void add(LibraryItem item) {
        favorites.put(item.getCode(), item);
        updatePreferences();
        version++;
    }

    public synchronized boolean remove(String code) {
        if(favorites.containsKey(code)) {
            favorites.remove(code);
            updatePreferences();
            version++;
            return true;
        }
        return false;
    }

    public synchronized boolean remove(LibraryItem item) {
        return remove(item.getCode());
    }

    public int count() { return favorites.size(); }

    public int getVersion() { return version; }

    private void updatePreferences() {
        Gson gson = new Gson();
        Type type = new TypeToken<ConcurrentHashMap<String, LibraryItem>>(){}.getType();
        String strFavorites = gson.toJson(favorites, type);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_FAVORITES, strFavorites);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

}
