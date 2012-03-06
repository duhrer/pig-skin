package com.blogspot.tonyatkins.pigskin.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.tonyatkins.pigskin.Constants;
import com.blogspot.tonyatkins.pigskin.R;
import com.blogspot.tonyatkins.pigskin.db.SearchListAdapter;
import com.blogspot.tonyatkins.pigskin.util.SearchManager;

public class Search extends Activity {
	protected EditText searchText;
	protected ListView searchResults;
	protected TextView dictionaryName;
	protected Activity activity = this;
	private SearchManager searchManager;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

		boolean fullScreen = preferences.getBoolean(Constants.FULL_SCREEN_PREF,
				Constants.DEFAULT_FULL_SCREEN);
		if (fullScreen) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}

		
		dictionaryName = (TextView) findViewById(R.id.searchDictionaryName);

		searchManager = new SearchManager(this);
		dictionaryName.setText(searchManager.getDictionary());
		dictionaryName.setOnClickListener(new LaunchPrefsListener());
	
		
		// wire up the text entry field
		searchText = (EditText) findViewById(R.id.searchPattern);

		// wire up the results list
		searchResults = (ListView) findViewById(R.id.searchResults);

		// wire up the search button
		Button searchButton = (Button) findViewById(R.id.searchButton);
		searchButton.setOnClickListener(new SearchButtonListener());
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuPrefs:
			launchPrefs();
			break;
		case R.id.menuExit:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void launchPrefs() {
		Intent prefsIntent = new Intent(this, Prefs.class);
		startActivityForResult(prefsIntent, Prefs.REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Prefs.REQUEST_CODE) {
			searchManager = new SearchManager(this);
			dictionaryName.setText(searchManager.getDictionary());
			performSearch();
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private class SearchButtonListener implements OnClickListener {
		public void onClick(View v) {
			performSearch();
		}
	}

	public void performSearch() {
	// Check to make sure the text entered is valid
				if (searchText == null || searchText.getText() == null) {
					Toast.makeText(activity, "Invalid or empty search string...", Toast.LENGTH_LONG).show();
					searchResults.setAdapter(null);
					searchResults.invalidateViews();
				} 
				else {
					String searchString = searchText.getText().toString().trim().toLowerCase();

					if (searchString.length() <= 1) {
						Toast.makeText(activity, "Words must be at least two letters long...", Toast.LENGTH_LONG)
								.show();
						searchResults.setAdapter(null);
						searchResults.invalidateViews();
					} else if (searchString.length() > 15) {
						Toast.makeText(activity, "Words can only be 15 letters or less...", Toast.LENGTH_LONG)
								.show();
						searchResults.setAdapter(null);
						searchResults.invalidateViews();
					} else if (searchString.matches(".*[^a-zA-Z]+.*")) {
						Toast.makeText(activity, "Words can only contain letters...", Toast.LENGTH_LONG).show();
						searchResults.setAdapter(null);
						searchResults.invalidateViews();
					} else {
						// If the text is valid, search and wire in a new ListAdapter with the
						// results
						List<String> words = searchManager.getWordsStartingWith(searchString);

						if (words.size() > 0) {
							SearchListAdapter adapter = new SearchListAdapter(activity, words);
							searchResults.setAdapter(adapter);
							searchResults.invalidateViews();
						}
					}
				}
	}
	
	private class LaunchPrefsListener implements OnClickListener {
		@Override
		public void onClick(View view) {
			launchPrefs();
		}
	}
}