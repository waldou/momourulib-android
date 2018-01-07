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

/**
 * @author Waldo Urribarri - www.waldou.com
 *
 * This is a helper class to return messages based on
 * a response code.
 *
 */
public class MessageResolver {

    public static String resolve(int responseCode, Context ctx) {
        String message = null;
        try {
            switch (responseCode) {
                case 1:
                    message = ctx.getResources().getString(R.string.error_no_results);
                    break;
                case 0:
                    message = ctx.getResources().getString(R.string.results_found);
                    break;
                case -1:
                    message = ctx.getResources().getString(R.string.error_timeout);
                    break;
                case -2:
                    message = ctx.getResources().getString(R.string.error_io);
                    break;
                case -3:
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
