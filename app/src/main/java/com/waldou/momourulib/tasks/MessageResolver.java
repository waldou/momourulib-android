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

import android.content.Context;

import com.waldou.momourulib.R;

/**
 * @author Waldo Urribarri - www.waldou.com
 *
 * This is a helper class to return messages based on
 * a response code.
 *
 */
public class MessageResolver implements TaskConstants {

    public static String resolve(int responseCode, Context ctx) {
        String message = null;
        try {
            switch (responseCode) {
                case RESPONSE_CODE_NO_RESULTS:
                    message = ctx.getResources().getString(R.string.error_no_results);
                    break;
                case RESPONSE_CODE_RESULTS_FOUND:
                    message = ctx.getResources().getString(R.string.results_found);
                    break;
                case RESPONSE_CODE_SERVER_OFFLINE:
                    message = ctx.getResources().getString(R.string.error_timeout);
                    break;
                case RESPONSE_CODE_IO_ERROR:
                    message = ctx.getResources().getString(R.string.error_io);
                    break;
                case RESPONSE_CODE_NO_INTERNET:
                    message = ctx.getResources().getString(R.string.error_network_unavailable);
                    break;
                default:
                    message = ctx.getResources().getString(R.string.error_general);
                    break;
            }
        } catch(Exception e) {
            message = "Error!";
        }
        return message;
    }

}
