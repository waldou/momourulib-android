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

import com.waldou.momourulib.framework.Library;
import com.waldou.utils.Connection;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Waldo Urribarri - www.waldou.com
 *
 * This class contains several global constants and objects
 * used across the app.
 *
 */
public class Globals {

    public static final boolean debugMode = false;

    public static final String APP_PREFERENCES = "momourulib-preferences";
    public static final String APP_USER_AGENT = "MomoURULib/" + BuildConfig.VERSION_NAME + " (Android)";

    private static Library library;
    private static FavoritesManager favoritesManager;

    /**
     * Gets the Library singleton.
     * @return Library object.
     */
    public static Library getLibrary() {
        if(library == null) {
            Map<String, String> requestProperties = new HashMap<String, String>();
            requestProperties.put("Content-Type", "application/x-www-form-urlencoded");
            requestProperties.put("User-Agent", APP_USER_AGENT);
            if(!debugMode)
                library = new Library(new Connection(requestProperties));
            else
                library = new Library(new FakeConnection(requestProperties));
        }
        return library;
    }

    /**
     * Gets the favorites manager singleton.
     * @return FavoritesManager object.
     */
    public static FavoritesManager getFavoritesManager() {
        return favoritesManager;
    }

    /**
     * Creates the favorites manager singleton.
     */
    public static void createFavoritesManager(Context ctx) {
        if(favoritesManager == null)
            favoritesManager = new FavoritesManager(ctx);
    }

    /**
     * Called when exiting the app.
     */
    public static void nullify() {
        library = null;
        favoritesManager = null;
    }

}