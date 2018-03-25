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

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;

import com.waldou.momourulib.R;
import com.waldou.momourulib.screens.LibraryActivity;

/**
 * @author Waldo Urribarri - www.waldou.com
 *
 *
 */
public class GenericTask extends AsyncTask<Object, Object, Object> implements TaskConstants {

    protected LibraryActivity ctx;
    protected ProgressDialog progressDialog;
    protected String progressTitle;
    protected String progressMessage;
    protected boolean showProgressDialog;
    protected Response response;

    public GenericTask(LibraryActivity ctx) {
        this.ctx = ctx;
        this.progressTitle = ctx.getString(R.string.default_task_progress_title);
        this.progressMessage = ctx.getString(R.string.default_task_progress_message);
        this.showProgressDialog = true;
    }

    public GenericTask(LibraryActivity ctx, boolean showProgressDialog) {
        this(ctx);
        this.showProgressDialog = showProgressDialog;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ctx.setNetIconVisibility(View.VISIBLE);
        if(showProgressDialog) {
            progressDialog = ProgressDialog.show(ctx, progressTitle, progressMessage, true, true);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    cancel(false);
                }
            });
        }
    }

    @Override
    protected Object doInBackground(Object... params) {
        return null;
    }

    @Override
    protected void onPostExecute(Object result) {
        if (ctx != null)
            ctx.setNetIconVisibility(View.INVISIBLE);
        if (progressDialog != null && showProgressDialog)
            progressDialog.dismiss();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (ctx != null)
            ctx.setNetIconVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCancelled(Object result) {
        super.onCancelled(result);
        if (ctx != null)
            ctx.setNetIconVisibility(View.INVISIBLE);
    }

    protected void destroy() {
        ctx = null;
        progressDialog = null;
        progressTitle = null;
        progressMessage = null;
        response = null;
    }

    protected Response getResponse() { return response; }
    protected void createResponse(int responseCode) {
        response = new Response(responseCode, MessageResolver.resolve(responseCode, ctx));
    }

    static class Response {
        private int responseCode;
        private String responseMessage;
        public Response(int responseCode, String responseMessage) {
            this.responseCode = responseCode;
            this.responseMessage = responseMessage;
        }
        public int getCode() { return responseCode; }
        public String getMessage() { return responseMessage; }
    }

}
