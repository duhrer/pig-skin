package com.blogspot.tonyatkins.pigskin.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
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
					char letter = searchString.charAt(0);
					List<String> words = new ArrayList<String>();
					List<String> letterWords = getWordsForLetter(letter);
					
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
    
    private List<String> getWordsForLetter(char letter) {
    	int resourceId = getResources().getIdentifier("words_" + letter, "array", "com.blogspot.tonyatkins.pigskin");
    	
    	if (resourceId != 0) {
    		try {
				String[] stringArray = getResources().getStringArray(resourceId);
				return Arrays.asList(stringArray);
			} catch (NotFoundException e) {
				Log.e(getClass().toString(), "Can't find letter resource", e);
			}
    	}
    	
    	return new ArrayList<String>();
    }
}