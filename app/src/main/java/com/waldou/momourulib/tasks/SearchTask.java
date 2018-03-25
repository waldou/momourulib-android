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
package com.waldou.momourulib.tasks;

import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.waldou.momourulib.Globals;
import com.waldou.momourulib.R;
import com.waldou.momourulib.Utils;
import com.waldou.momourulib.framework.LibraryItem;
import com.waldou.momourulib.framework.SearchArguments;
import com.waldou.momourulib.framework.exceptions.LibraryException;
import com.waldou.momourulib.screens.LibraryActivity;
import com.waldou.momourulib.screens.ResultsFragment;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.List;

/**
 * @author Waldo Urribarri - www.waldou.com
 *
 * This class does the search of books based on the
 * arguments passed as parameters, and updates the UI
 * according to the response.
 *
 */
public class SearchTask extends GenericTask {

    private SearchArguments searchArguments;

    public SearchTask(LibraryActivity ctx) {
        super(ctx);
    }
    public SearchTask(LibraryActivity ctx, boolean showProgressDialog) {
        super(ctx, showProgressDialog);
    }

    @Override
    protected Object doInBackground(Object... params) {
        List<LibraryItem> items = null;
        if(!Utils.isNetworkAvailable(ctx)){
            createResponse(RESPONSE_CODE_NO_INTERNET);
        } else {
            try {
                searchArguments = (SearchArguments) params[0];
                items = Globals.getLibrary().search(searchArguments);
            } catch (LibraryException e) {
                createResponse(e.getResponseCode());
            } catch (IOException e) {
                if (e instanceof SocketTimeoutException)
                    createResponse(RESPONSE_CODE_SERVER_OFFLINE);
                else
                    createResponse(RESPONSE_CODE_IO_ERROR);
            } catch (Exception e) {
                createResponse(RESPONSE_CODE_UNKNOWN);
            }
        }
        if(getResponse() == null) {
            if (items == null || items.isEmpty()) {
                createResponse(RESPONSE_CODE_NO_RESULTS);
            } else {
                createResponse(RESPONSE_CODE_RESULTS_FOUND);
            }
        }
        return items;
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        try {
            if (getResponse().getCode() == RESPONSE_CODE_RESULTS_FOUND) {
                // Get items
                List<LibraryItem> items = (List<LibraryItem>) result;

                // Create bundle
                Type type = new TypeToken<List<LibraryItem>>(){}.getType();
                Gson gson = new Gson();
                String strItemsList = gson.toJson(items, type);
                String strSearchArguments = gson.toJson(searchArguments, SearchArguments.class);
                Bundle bundle = new Bundle();
                bundle.putString(ResultsFragment.KEY_ITEMS_LIST, strItemsList);
                bundle.putString(ResultsFragment.KEY_SEARCH_ARGUMENTS, strSearchArguments);

                // Set arguments
                ResultsFragment resultsFragment = new ResultsFragment();
                resultsFragment.setArguments(bundle);

                // Set current fragment
                ctx.setCurrentFragment(resultsFragment);
                ctx.getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.content_frame, resultsFragment)
                        .commit();
                ctx.setDefaultToolbarTitle(ctx.getResources().getString(R.string.results_title));
            } else {
                Utils.sendToast(ctx, getResponse().getMessage(), Toast.LENGTH_LONG);
            }
        } catch(Exception e) {
            // TODO
        } finally {
            destroy();
        }
    }

    @Override
    protected void destroy() {
        searchArguments = null;
        super.destroy();
    }

}
