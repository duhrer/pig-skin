package com.blogspot.tonyatkins.pigskin.db;

import java.util.List;

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

public class SearchListAdapter implements ListAdapter{
	private Context context;
	private List<String> words;
	private Typeface scrabbleFont;
	private final DictionaryFactory dictionaryFactory = new DictionaryFactory();
	
	private	final SharedPreferences preferences; 
	
	public SearchListAdapter(Context context, List<String> words) {
		super();
		this.context = context;
		this.words = words;
		this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
		
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
		String word = words.get(position);
		view.setText(word);
		view.setTypeface(scrabbleFont);
		view.setOnLongClickListener(new LookupListener(word,this.context));
		
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
