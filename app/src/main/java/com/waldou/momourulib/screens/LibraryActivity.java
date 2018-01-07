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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.waldou.momourulib.Globals;
import com.waldou.momourulib.R;
import com.waldou.momourulib.Utils;
import com.waldou.momourulib.framework.LibraryItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Waldo Urribarri - www.waldou.com
 *
 * This fragment manages the main application cycle.
 *
 */
public class LibraryActivity extends AppCompatActivity {

    private FragmentActivity ctx;

    private Toolbar mToolbar;
    protected DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    protected String mTitle;

    private FragmentManager fragmentManager;
    private Fragment currentFragment;
    private Fragment[] fragmentScreens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        ctx = this;

        fragmentScreens = new Fragment[3];
        currentFragment = new SearchFragment();
        fragmentScreens[0] = currentFragment;
        fragmentScreens[1] = currentFragment;
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, currentFragment)
                .commit();

        setupMenu();
        initializeApp(ctx);
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawers();
        else {
            if (currentFragment instanceof SearchFragment){
                ctx.finish();
            } else {
                // TODO simplify this
                fragmentManager.popBackStackImmediate();
                if(fragmentManager.getBackStackEntryCount() == 0) {
                    currentFragment = fragmentScreens[0];
                    ctx.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, fragmentScreens[0])
                            .commit();
                } else {
                    currentFragment = fragmentManager.getFragments().get(0);
                }
                setDefaultToolbarTitle(getString(R.string.drawer_closed));
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ctx = null;
        mToolbar = null;
        mDrawerLayout = null;
        mDrawerList = null;
        mDrawerToggle = null;
        mTitle = null;
        fragmentManager = null;
        currentFragment = null;
        fragmentScreens = null;
        Globals.nullify();
    }

    /**
     * Contains the code to setup the toolbar and drawer.
     */
    private void setupMenu() {

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.left_drawer);
        View header = getLayoutInflater().inflate(R.layout.nav_header, null);
        mDrawerList.addHeaderView(header);
        final int[] menuIcons = new int[] { R.drawable.ic_search_black_24dp, R.drawable.ic_star_black_24dp};
        String[] menuText = getResources().getStringArray(R.array.nav_options);
        ArrayAdapter<String> adapter =
            new ArrayAdapter<String>(this, R.layout.nav_item, R.id.nav_item_textview, menuText) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    final View view;
                    final ImageView icon;
                    final TextView text;
                    view = getLayoutInflater().inflate(R.layout.nav_item, parent, false);
                    icon = view.findViewById(R.id.nav_item_icon);
                    text = view.findViewById(R.id.nav_item_textview);
                    text.setText(getItem(position));
                    icon.setImageResource(menuIcons[position]);
                    if(position == 1)
                        icon.setColorFilter(ContextCompat.getColor(ctx, R.color.myYellow), android.graphics.PorterDuff.Mode.MULTIPLY);
                    return view;
                }
            };
        adapter.setDropDownViewResource(R.layout.nav_item);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(final AdapterView<?> parent, final View view,
                                    final int position, long id) {

                // No change
                if(currentFragment == fragmentScreens[position]) {
                    mDrawerLayout.closeDrawers();
                    return;
                }

                switch (position) {
                    case 0:
                    case 1:
                        if (fragmentScreens[0] == null) {
                            fragmentScreens[0] = new SearchFragment();
                            fragmentScreens[1] = fragmentScreens[0];
                        }
                        setDefaultToolbarTitle(getString(R.string.drawer_closed));
                        break;
                    case 2:
                        if(Globals.getFavoritesManager().count() == 0) {
                            Utils.sendToast(ctx, getString(R.string.error_no_favorites), Toast.LENGTH_LONG);
                            mDrawerLayout.closeDrawers();
                            return;
                        } else {
                            if(fragmentScreens[position] == null ||
                                    ((ResultsFragment)fragmentScreens[position]).getVersion() !=
                                            Globals.getFavoritesManager().getVersion()) {
                                ResultsFragment favoritesFragment = new ResultsFragment();
                                Type type = new TypeToken<List<LibraryItem>>(){}.getType();
                                String strItemsList =
                                        new Gson().toJson(
                                                new ArrayList<LibraryItem>(Globals.getFavoritesManager().getAll()), type);
                                Bundle bundle = new Bundle();
                                bundle.putString(ResultsFragment.KEY_ITEMS_LIST, strItemsList);
                                favoritesFragment.setArguments(bundle);
                                favoritesFragment.setVersion(Globals.getFavoritesManager().getVersion());
                                fragmentScreens[position] = favoritesFragment;
                            }
                        }
                        setDefaultToolbarTitle(getResources().getStringArray(R.array.nav_options)[position - 1]);
                        break;
                }

                currentFragment = fragmentScreens[position];
                fragmentManager.popBackStack();

                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, currentFragment)
                        .commit();

                mDrawerLayout.closeDrawers();
            }

        });

        mToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final String mDrawerTitle = getString(R.string.drawer_open);
        setDefaultToolbarTitle(getString(R.string.drawer_closed));

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.drawer_open, R.string.drawer_closed) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                mToolbar.setTitle(mTitle);
                invalidateOptionsMenu();
                syncState();
            }
            public void onDrawerOpened(View drawerView) {
                super.onDrawerClosed(drawerView);
                mToolbar.setTitle(mDrawerTitle);
                invalidateOptionsMenu();
                syncState();
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

    }

    private void initializeApp(Context ctx) {
        Globals.createFavoritesManager(ctx);
    }

    /**
     * Set net usage icon visibility.
     * @param visibility    Visibility flag.
     */
    public void setNetIconVisibility(int visibility) {
        ImageView netIcon = findViewById(R.id.net_imageview);
        if(netIcon != null)
            netIcon.setVisibility(visibility);
    }

    /**
     * Set the toolbar title.
     * @param title
     */
    public void setDefaultToolbarTitle(String title) {
        mTitle = title;
        mToolbar.setTitle(mTitle);
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }
    public void setCurrentFragment(Fragment fragment) {
        currentFragment = fragment;
    }

}