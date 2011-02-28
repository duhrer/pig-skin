package com.blogspot.tonyatkins.pigskin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.blogspot.tonyatkins.pigskin.R;
import com.blogspot.tonyatkins.pigskin.db.DbHelper;

public class Startup extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup);
        
        // Check to make sure the database is set up
        DbHelper db = new DbHelper(this);
        int wordCount = db.getWordCount(db.getReadableDatabase());
        
        Toast.makeText(this, "Dictionary contains " + wordCount + " words.", Toast.LENGTH_LONG);

        if (wordCount > 0) {
        	// Open the search page
        	Intent mainIntent = new Intent(this, Search.class);
        	startActivity(mainIntent);
        }
    }
}