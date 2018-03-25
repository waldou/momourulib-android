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

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.waldou.momourulib.FavoritesManager;
import com.waldou.momourulib.R;
import com.waldou.momourulib.framework.LibraryItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Waldo Urribarri - www.waldou.com
 *
 * This class manages the results list.
 *
 */
public class ResultsListViewAdapter extends BaseAdapter {

	private LibraryActivity ctx;
	private LayoutInflater inflater;
	private List<LibraryItem> list = null;
	private List<LibraryItem> itemList;
	private FavoritesManager favoritesManager;

	ResultsListViewAdapter(LibraryActivity ctx, LayoutInflater inf, List<LibraryItem> itemList, FavoritesManager favoritesManager) {
		this.ctx = ctx;
		this.inflater = inf;
		this.itemList = itemList;
		this.list = new ArrayList<LibraryItem>();
		this.list.addAll(itemList);
		this.favoritesManager = favoritesManager;
	}

	static class ViewHolder {
		@BindView(R.id.result_card_view) CardView cardView;
		@BindView(R.id.item_name) TextView name;
		@BindView(R.id.item_code) TextView code;
		@BindView(R.id.item_favorite_icon) ImageView favoriteIcon;
		Boolean isFavorite;
		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public LibraryItem getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			view = inflater.inflate(R.layout.result_list_view_item, null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		final LibraryItem item = list.get(position);

		holder.cardView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(ctx, DetailsActivity.class);
				intent.putExtra(DetailsActivity.KEY_LIBRARY_ITEM, item);
				ctx.startActivity(intent);
			}
		});

		// Set the results into the Views
		holder.name.setText(item.getName());
		holder.code.setText(item.getCode());
		holder.isFavorite = (favoritesManager.get(item.getId()) != null) ? true : false;
		// Change icon if its a favorite
		if(holder.isFavorite) {
			Drawable wrappedIcon =
					DrawableCompat.wrap(inflater.getContext().getResources().getDrawable(R.drawable.ic_star_black_24dp));
			DrawableCompat.setTint(wrappedIcon, inflater.getContext().getResources().getColor(R.color.myYellow));
			holder.favoriteIcon.setImageDrawable(wrappedIcon);
		} else {
			holder.favoriteIcon.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.ic_star_border_black_24dp));
		}

		holder.favoriteIcon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(holder.isFavorite) {
					holder.isFavorite = false;
					holder.favoriteIcon.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.ic_star_border_black_24dp));
					favoritesManager.remove(item);
				} else {
					holder.isFavorite = true;
					Drawable wrappedIcon =
							DrawableCompat.wrap(inflater.getContext().getResources().getDrawable(R.drawable.ic_star_black_24dp));
					DrawableCompat.setTint(wrappedIcon, inflater.getContext().getResources().getColor(R.color.myYellow));
					holder.favoriteIcon.setImageDrawable(wrappedIcon);
					favoritesManager.add(item);
				}
			}
		});
		view.setOnClickListener(null);
		return view;
	}

	public void addItems(List<LibraryItem> newItems) {
		itemList.addAll(newItems);
		list.clear();
		list.addAll(itemList);
		notifyDataSetChanged();
	}

	/**
	 * Used to filter by search string
	 * @param charText Search String.
	 */
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		list.clear();
		if (charText.length() == 0) {
			list.addAll(itemList);
		} else {
			for (LibraryItem item : itemList) {
				if (item.getName().toLowerCase(Locale.getDefault()).contains(charText))
					list.add(item);
			}
		}
		notifyDataSetChanged();
	}

}
