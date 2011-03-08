package com.blogspot.tonyatkins.pigskin.db;

import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

public class SearchListAdapter implements ListAdapter{
	private Context context;
	private List<String> words;
	private Typeface scrabbleFont;
	
	public SearchListAdapter(Context context, List<String> words) {
		super();
		this.context = context;
		this.words = words;
		
		scrabbleFont = Typeface.createFromAsset(context.getAssets(), "fonts/scramble.ttf");
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
	}

	@Override
	public int getCount() {
		return words.size();
	}

	@Override
	public Object getItem(int position) {
		return words.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView view = new TextView(context);
		view.setText(words.get(position));
		view.setTypeface(scrabbleFont);
		
		return view;
	}

	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return (words.size() > 0);
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}
}
