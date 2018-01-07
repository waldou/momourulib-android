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
package com.waldou.momourulib.screens;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.waldou.momourulib.R;
import com.waldou.momourulib.Utils;
import com.waldou.momourulib.framework.Library;
import com.waldou.momourulib.framework.SearchArguments;
import com.waldou.momourulib.tasks.SearchTask;

/**
 * @author Waldo Urribarri - www.waldou.com
 *
 * The SearchFragment is used to show the search arguments UI elements.
 *
 */
public class SearchFragment extends Fragment {

    private FragmentActivity ctx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ctx = getActivity();
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        final EditText expresion = (EditText) rootView.findViewById(R.id.expresion_edittext);
        final Spinner campos = (Spinner) rootView.findViewById(R.id.campos_spinner);
        final Spinner expres = (Spinner) rootView.findViewById(R.id.expre_spinner);

        Button searchButton = (Button) rootView.findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strExpresion = expresion.getText().toString().trim();
                if(strExpresion.equals("")) {
                    Utils.sendToast(ctx, getString(R.string.error_missing_expresion), Toast.LENGTH_LONG);
                    return;
                }
                SearchArguments args = new SearchArguments(campos.getSelectedItem().toString(),
                        strExpresion, expres.getSelectedItem().toString());
                args.setEncoding(Library.CONTENT_ENCODING);
                new SearchTask((LibraryActivity) ctx).execute(args);
            }
        });

        return rootView;

    }

}
