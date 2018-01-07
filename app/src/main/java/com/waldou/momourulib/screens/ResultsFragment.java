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

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.waldou.momourulib.Globals;
import com.waldou.momourulib.R;
import com.waldou.momourulib.framework.LibraryItem;
import com.waldou.momourulib.framework.SearchArguments;
import com.waldou.momourulib.tasks.PagingTask;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;

/**
 * @author Waldo Urribarri - www.waldou.com
 *
 * The ResultsFragment is used to show the books that
 * come from the SearchTask, or to show the favorites.
 *
 */
public class ResultsFragment extends Fragment {

    public static final String KEY_ITEMS_LIST = "itemsList";
    public static final String KEY_SEARCH_ARGUMENTS = "searchArguments";

    private FragmentActivity ctx;

    protected MenuItem mSearchAction;
    private EditText edtSeach;
    protected boolean isSearchOpened;

    private int version;
    private ResultsListViewAdapter adapter;
    private SearchArguments searchArguments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ctx = getActivity();
        View rootView = inflater.inflate(R.layout.fragment_results, container, false);

        Gson gson = new Gson();

        searchArguments = gson.fromJson(getArguments().getString(KEY_SEARCH_ARGUMENTS), SearchArguments.class);

        final ListView listView = rootView.findViewById( R.id.listview );
        Type type = new TypeToken<List<LibraryItem>>(){}.getType();
        List<LibraryItem> itemsList = gson.fromJson(getArguments().getString(KEY_ITEMS_LIST), type);
        adapter = new ResultsListViewAdapter((LibraryActivity) ctx, getLayoutInflater(), itemsList, Globals.getFavoritesManager());
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE && getVersion() == 0) {
                    int threshold = 1;
                    int count = listView.getCount();
                    if (count >= searchArguments.getTo() && listView.getLastVisiblePosition() >= count - threshold) {
                        boolean doSearch = !isSearchOpened;
                        if(!doSearch && edtSeach.getText().toString().equals("")) {
                            closeSearch();
                            doSearch = true;
                        }
                        if(doSearch) {
                            searchArguments.setFrom(listView.getCount() + 1);
                            new PagingTask((LibraryActivity) ctx).execute(searchArguments);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.results, menu);
        mSearchAction = menu.findItem(R.id.menu_search);
        Drawable icon = mSearchAction.getIcon();
        Drawable wrappedIcon = DrawableCompat.wrap(icon);
        DrawableCompat.setTint(wrappedIcon, getResources().getColor(R.color.myWhite));
        mSearchAction.setIcon(wrappedIcon);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_search) {
            handleMenuSearch();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void handleMenuSearch(){
        ActionBar action = ((AppCompatActivity) ctx).getSupportActionBar(); //get the actionbar
        if(isSearchOpened){ //test if the search is open
            closeSearch();
            //hides the keyboard
            InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(edtSeach.getWindowToken(), 0);
            }
        } else { //open the search entry
            if (action != null) {
                action.setDisplayShowCustomEnabled(true); //enable it to display a
                // custom view in the action bar.
                action.setCustomView(R.layout.search_bar);//add the custom view
                action.setDisplayShowTitleEnabled(false); //hide the title
                edtSeach = action.getCustomView().findViewById(R.id.edtSearch); //the text editor
                edtSeach.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable arg0) {
                        String text = edtSeach.getText().toString().toLowerCase(Locale.getDefault());
                        adapter.filter(text);
                    }
                    @Override
                    public void beforeTextChanged(
                            CharSequence arg0, int arg1, int arg2, int arg3) {
                    }
                    @Override
                    public void onTextChanged(
                            CharSequence arg0, int arg1, int arg2, int arg3) {
                    }
                });
                edtSeach.requestFocus();
                //open the keyboard focused in the edtSearch
                InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(edtSeach, InputMethodManager.SHOW_IMPLICIT);
                }
                //add the close icon
                Drawable wrappedIcon = DrawableCompat.wrap(getResources().getDrawable(R.drawable.ic_clear_black_24dp));
                DrawableCompat.setTint(wrappedIcon, getResources().getColor(R.color.myWhite));
                mSearchAction.setIcon(wrappedIcon);
                isSearchOpened = true;
            }
        }
    }

    protected void closeSearch() {
        ActionBar action = ((AppCompatActivity) ctx).getSupportActionBar(); //get the actionbar
        if (action != null) {
            action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
            action.setDisplayShowTitleEnabled(true); //show the title in the action bar
            action.setTitle(((LibraryActivity) ctx).mTitle);
        }
        if(mSearchAction != null) {
            Drawable wrappedIcon = DrawableCompat.wrap(getResources().getDrawable(R.drawable.ic_search_black_24dp));
            DrawableCompat.setTint(wrappedIcon, getResources().getColor(R.color.myWhite));
            mSearchAction.setIcon(wrappedIcon);
        }
        adapter.filter("");
        isSearchOpened = false;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        closeSearch();
        super.onStop();
    }

    /**
     * Version zero means this is the result of a search.
     * Other version number would mean this is a Favorites view.
     * @return Version number.
     */
    public int getVersion() {
        return version;
    }
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * This method is used after a paging call.
     * @param newItems Items to be added to the adapter.
     */
    public void addItems(List<LibraryItem> newItems) {
        adapter.addItems(newItems);
    }

}
