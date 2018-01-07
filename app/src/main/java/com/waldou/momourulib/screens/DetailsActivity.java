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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.waldou.momourulib.R;
import com.waldou.momourulib.framework.LibraryItem;

/**
 * @author Waldo Urribarri - www.waldou.com
 *
 * This activity is used to show the details of a particular
 * LibraryItem.
 *
 */
public class DetailsActivity extends AppCompatActivity {

    public static final String KEY_LIBRARY_ITEM = "libraryItem";
    private static final String SPACE = " ";
    private static final String SEPARATOR = ", ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        LibraryItem item = (LibraryItem) getIntent().getSerializableExtra(KEY_LIBRARY_ITEM);

        TextView itemName = findViewById(R.id.item_name);
        itemName.setText(item.getName());

        TextView itemDescription = findViewById(R.id.item_description);
        itemDescription.setText(item.getDescription());

        TextView itemKeywords = findViewById(R.id.item_keywords);

        StringBuilder sb = new StringBuilder();
        sb.append(getResources().getString(R.string.keywords_label));
        if(item.getKeywords() != null && item.getKeywords().size() > 0) {
            sb.append(SPACE).append(item.getKeywords().get(0));
            for(int i = 1; i < item.getKeywords().size(); i++)
                sb.append(SEPARATOR).append(item.getKeywords().get(i));
        }
        itemKeywords.setText(sb.toString());

        TextView itemCode = findViewById(R.id.item_code);
        itemCode.setText(item.getCode());

        Toolbar mToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.details_title);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
