package com.blogspot.tonyatkins.pigskin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.blogspot.tonyatkins.pigskin.R;
import com.blogspot.tonyatkins.pigskin.db.DbHelper;

public class Startup extends Activity {
	private DbHelper db;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.startup);
        
        // Check to make sure the database is set up
        db = new DbHelper(this); // 1s
        int wordCount = db.getWordCount(db.getReadableDatabase(), 'a');
        

        if (wordCount > 0) {
        	Toast.makeText(this, "Dictionary contains " + wordCount + " words beginning with 'a'.", Toast.LENGTH_LONG).show();
        	
        	// Open the search page
        	Intent mainIntent = new Intent(this, Search.class);
        	startActivity(mainIntent);
        }
        else {
        	Toast.makeText(this, "Unable to load dictionary, can't continue...", Toast.LENGTH_LONG).show();
        }
    }

	@Override
	protected void onDestroy() {
		db.close();
		super.onDestroy();
	}
}