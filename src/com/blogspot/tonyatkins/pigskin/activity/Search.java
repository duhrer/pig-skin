package com.blogspot.tonyatkins.pigskin.activity;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.blogspot.tonyatkins.pigskin.R;
import com.blogspot.tonyatkins.pigskin.db.DbHelper;
import com.blogspot.tonyatkins.pigskin.db.SearchListAdapter;

public class Search extends Activity {
	protected EditText searchText;
	protected ListView searchResults;
	protected Context context = this;
	protected DbHelper dbHelper;
	protected SQLiteDatabase db;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        
        // Check to make sure the database is set up
        dbHelper = new DbHelper(this);
        
        // This appears to be incredibly costly.  Is there a better way internal to the parent class of DbHelper? (SQLiteOpenHelper)
        db = dbHelper.getReadableDatabase();
        
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
				String searchString = searchText.getText().toString().trim();
				
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
					// If the text is valid, search and wire in a new listadapter with the results
					
					// look for words starting with the string first
					Cursor words = dbHelper.getWordsStartingWith(db, searchString);
					if (words.getCount() <= 0) {
						words = dbHelper.getWordsMatching(db, searchString);
					}
					
					SearchListAdapter adapter = new SearchListAdapter(context, words);
					
					searchResults.setAdapter(adapter);
					searchResults.invalidateViews();
				}
			}
		}
    	
    }
}