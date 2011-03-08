package com.blogspot.tonyatkins.pigskin.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.blogspot.tonyatkins.pigskin.R;
import com.blogspot.tonyatkins.pigskin.db.SearchListAdapter;

public class Search extends Activity {
	protected EditText searchText;
	protected ListView searchResults;
	protected Context context = this;
	private Map<String,List<String>> wordData = new HashMap<String,List<String>>();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        
        // wire up the text entry field
        searchText = (EditText) findViewById(R.id.searchPattern);

        // wire up the results list
        searchResults = (ListView) findViewById(R.id.searchResults);
        
        // wire up the search button
        Button searchButton = (Button) findViewById(R.id.searchButton);
    	searchButton.setOnClickListener(new SearchButtonListener());
    }
    
    private class SearchButtonListener implements OnClickListener {
		public void onClick(View v) {
			// Check to make sure the text entered is valid
			if (searchText == null || searchText.getText() == null) {
				Toast.makeText(context, "Invalid or empty search string...", Toast.LENGTH_LONG).show();
				searchResults.setAdapter(null);
				searchResults.invalidateViews();
			}
			else {
				String searchString = searchText.getText().toString().trim().toLowerCase();
				
				if (searchString.length() <= 1) {
					Toast.makeText(context, "Words must be at least two letters long...", Toast.LENGTH_LONG).show();
					searchResults.setAdapter(null);
					searchResults.invalidateViews();
				}
				else if (searchString.length() > 15) {
					Toast.makeText(context, "Words can only be 15 letters or less...", Toast.LENGTH_LONG).show();
					searchResults.setAdapter(null);
					searchResults.invalidateViews();
				}
				else if (searchString.matches(".*[^a-zA-Z]+.*")) {
					Toast.makeText(context, "Words can only contain letters...", Toast.LENGTH_LONG).show();
					searchResults.setAdapter(null);
					searchResults.invalidateViews();
				}
				else {
					// If the text is valid, search and wire in a new ListAdapter with the results
					String prefix = searchString.substring(0, 2).toLowerCase();
					List<String> words = new ArrayList<String>();
					List<String> letterWords = getWordsForPrefix(prefix);
					
					// look for words starting with the string first
					for (String word : letterWords) {
						if (word.toLowerCase().startsWith(searchString)) { words.add(word); }
					}

					// FIXME: match parts of words by iterating through all the sets of letters (likely expensive)
					
					if (words.size() <= 0) {
						words.add("No matches for '" + searchString + "'");
					}
					
					SearchListAdapter adapter = new SearchListAdapter(context, words);
					
					searchResults.setAdapter(adapter);
					searchResults.invalidateViews();
				}
			}
		}
    	
    }
    
    private List<String> getWordsForPrefix(String prefix) {
    	List<String> prefixWords = wordData.get(prefix);
    	
    	// if there's no data, load it
    	if (prefixWords == null) {
    		// load the required data
    		prefixWords = new ArrayList<String>();
    		wordData.put(prefix, prefixWords);
    		
    		InputStream in;
			try {
				in = getAssets().open("dictionary/words_" + prefix + ".txt");
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String line;
				while ((line = reader.readLine()) != null) {
					prefixWords.add(line.trim().toLowerCase());
				}
			} catch (IOException e) {
				Log.e(getClass().toString(), "Can't load word data", e);
			}
    	}
    	
    	return prefixWords;
   }
}