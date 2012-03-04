package com.blogspot.tonyatkins.pigskin.activity;

import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.blogspot.tonyatkins.pigskin.Constants;
import com.blogspot.tonyatkins.pigskin.R;
import com.blogspot.tonyatkins.pigskin.db.SearchListAdapter;
import com.blogspot.tonyatkins.pigskin.util.SearchManager;

public class Bingo extends Activity {
	protected EditText searchText;
	protected ListView searchResults;
	protected Activity activity = this;
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
				Toast.makeText(activity, "Invalid or empty search string...", Toast.LENGTH_LONG).show();
				searchResults.setAdapter(null);
				searchResults.invalidateViews();
			}
			else {
				String searchString = searchText.getText().toString().trim().toLowerCase();
				
				if (searchString.length() != 7) {
					Toast.makeText(activity, "You must enter seven letters to find a 'bingo'.", Toast.LENGTH_LONG).show();
					searchResults.setAdapter(null);
					searchResults.invalidateViews();
				}
				else if (searchString.matches(".*[^a-zA-Z]+.*")) {
					Toast.makeText(activity, "Words can only contain letters...", Toast.LENGTH_LONG).show();
					searchResults.setAdapter(null);
					searchResults.invalidateViews();
				}
				else {
					// If the text is valid, search and wire in a new ListAdapter with the results
					List<String> words = searchManager.findBingo(searchString);
						
					// FIXME: match parts of words by iterating through all the sets of letters (likely expensive)
					
					if (words.size() <= 0) {
						words.add("No 7-letter words found matching letters '" + searchString + "'");
					}
					
					SearchListAdapter adapter = new SearchListAdapter(activity, words);
					
					searchResults.setAdapter(adapter);
					searchResults.invalidateViews();
				}
			}
		}
    	
    }
    

}