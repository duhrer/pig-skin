package com.blogspot.tonyatkins.pigskin.db;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.blogspot.tonyatkins.pigskin.model.Word;

public class SearchListAdapter implements ListAdapter{
	private Context context;
	private Cursor words;

	public SearchListAdapter(Context context, Cursor words) {
		super();
		this.context = context;
		this.words = words;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
	}

	@Override
	public int getCount() {
		if (words.getCount() > 0) {
			return words.getCount();
		}
		else {
			return 1;
		}
	}

	@Override
	public Object getItem(int position) {
		words.moveToPosition(position);
		return words.getString(words.getColumnIndex(Word.WORD));
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
		if ( words.getCount() <= 0) {
			view.setText("No Results Found");
		}
		else {
			words.moveToPosition(position);
			view.setText(words.getString(words.getColumnIndex(Word.WORD)));
		}

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
		if (words.getCount() > 0) return false;
		
		return true;
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
