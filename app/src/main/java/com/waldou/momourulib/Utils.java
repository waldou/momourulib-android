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

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Waldo Urribarri - www.waldou.com
 *
 * This class contains several utility methods.
 *
 */
public class Utils {

    /**
     * Helper function to show a Toast.
     * @param ctx       App context.
     * @param msg       Message String.
     * @param duration  Toast duration.
     */
    public static void sendToast(final Activity ctx, final String msg, final int duration) {
        if(ctx != null) {
            ctx.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LayoutInflater inflater = ctx.getLayoutInflater();
                    final View toastLayout = inflater.inflate(R.layout.custom_toast,
                            (ViewGroup) ctx.findViewById(R.id.toast_layout_root));
                    Toast t = Toast.makeText(ctx.getApplicationContext(), "", duration);
                    TextView text = (TextView) toastLayout.findViewById(R.id.text);
                    text.setText(msg);
                    t.setView(toastLayout);
                    t.show();
                }
            });
        }
    }

    /**
     * Checks if a network connection is available.
     * @return	<b>true</b> if a connection is available.
     */
    public static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
