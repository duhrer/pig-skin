package com.blogspot.tonyatkins.pigskin.activity;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.blogspot.tonyatkins.pigskin.Constants;
import com.blogspot.tonyatkins.pigskin.R;
import com.blogspot.tonyatkins.pigskin.db.SearchListAdapter;
import com.blogspot.tonyatkins.pigskin.util.SearchManager;

public class Search extends Activity {
	protected EditText searchText;
	protected ListView searchResults;
	protected Context context = this;
	private SearchManager searchManager;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);

		boolean fullScreen = preferences.getBoolean(Constants.FULL_SCREEN_PREF,
				Constants.DEFAULT_FULL_SCREEN);
		if (fullScreen) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
        
        searchManager = new SearchManager(this);
        
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
					List<String> words = searchManager.getWordsStartingWith(searchString);
						
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
    

}