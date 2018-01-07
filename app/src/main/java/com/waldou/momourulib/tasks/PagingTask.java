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

import android.widget.Toast;

import com.waldou.momourulib.MessageResolver;
import com.waldou.momourulib.Globals;
import com.waldou.momourulib.Utils;
import com.waldou.momourulib.framework.LibraryItem;
import com.waldou.momourulib.framework.SearchArguments;
import com.waldou.momourulib.framework.exceptions.LibraryException;
import com.waldou.momourulib.screens.LibraryActivity;
import com.waldou.momourulib.screens.ResultsFragment;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

/**
 * @author Waldo Urribarri - www.waldou.com
 *
 * This class does the search of books based on the
 * arguments passed as parameters, and updates the UI
 * according to the response. It adds
 * results to the current adapter, so it is used
 * just to get additional results.
 *
 */
public class PagingTask extends GenericTask {

    private int responseCode;
    private String responseMessage;

    public PagingTask(LibraryActivity ctx) {
        super(ctx);
    }

    @Override
    protected Object doInBackground(Object... params) {
        List<LibraryItem> items = null;
        if(!Utils.isNetworkAvailable(ctx)) {
            this.responseCode = -3;
        } else {
            try {
                SearchArguments searchArguments = (SearchArguments) params[0];
                items = Globals.getLibrary().more(searchArguments);
            } catch (LibraryException e) {
                this.responseCode = e.getResponseCode();
                this.responseMessage = e.getMessage();
            } catch (IOException e) {
                this.responseCode = -2;
                if (e instanceof SocketTimeoutException)
                    this.responseCode = -1;
            } catch (Exception e) {
                this.responseCode = -99;
            }
        }
        if(responseCode == 0) {
            if(items == null || items.isEmpty()) {
                this.responseCode = 1;
            }
        }
        if(responseMessage == null)
            responseMessage = MessageResolver.resolve(responseCode, ctx);
        return items;
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        try {
            if (responseCode == 0) {
                if(!(ctx.getCurrentFragment() instanceof ResultsFragment))
                    return; // Just omit the response
                ResultsFragment resultsFragment = (ResultsFragment) ctx.getCurrentFragment();
                List<LibraryItem> items = (List<LibraryItem>) result;
                resultsFragment.addItems(items);
                Utils.sendToast(ctx, responseMessage, Toast.LENGTH_SHORT);
            } else {
                Utils.sendToast(ctx, responseMessage, Toast.LENGTH_LONG);
            }
        } catch(Exception e) {
            // TODO
        }
        responseMessage = null;
        ctx = null;
    }

}
