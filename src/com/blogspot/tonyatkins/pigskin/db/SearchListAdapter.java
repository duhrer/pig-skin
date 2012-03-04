package com.blogspot.tonyatkins.pigskin.db;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.blogspot.tonyatkins.pigskin.Constants;
import com.blogspot.tonyatkins.pigskin.model.Dictionary;
import com.blogspot.tonyatkins.pigskin.model.DictionaryFactory;
import com.blogspot.tonyatkins.pigskin.view.TiledView;

public class SearchListAdapter implements ListAdapter{
	private Activity activity;
	private List<String> words;
	private final DictionaryFactory dictionaryFactory = new DictionaryFactory();
	
	private	final SharedPreferences preferences; 
	
	public SearchListAdapter(Activity activity, List<String> words) {
		super();
		this.activity = activity;
		this.words = words;
		this.preferences = PreferenceManager.getDefaultSharedPreferences(activity);
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
		TiledView view = new TiledView(activity, words.get(position));
		view.setOnLongClickListener(new LookupListener(words.get(position),this.activity));
		
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
	
	private class LookupListener implements OnLongClickListener {
		private final Context context;
		private final String query;
		
		public LookupListener(String query, Context context) {
			this.query = query;
			this.context = context;
		}

		@Override
		public boolean onLongClick(View v) {
    		String prefix = preferences.getString(Constants.DICTIONARY_PREF, Constants.DEFAULT_DICTIONARY);

			Dictionary dictionary = dictionaryFactory.getDictionary(prefix);
			if (dictionary != null) {
				String url = dictionary.getLookupURL(this.query);
				Intent i = new Intent(Intent.ACTION_VIEW);  
				i.setData(Uri.parse(url));  
				context.startActivity(i);  
			}
				
			return true;
		}
		
	}
}
