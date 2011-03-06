package com.blogspot.tonyatkins.pigskin.activity;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.blogspot.tonyatkins.pigskin.R;

public class Startup extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.startup);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
        
    	
    	List<String> words = Arrays.asList(getResources().getStringArray(R.array.words_x));
        if (words.size() > 0) {
        	Toast.makeText(this, "Dictionary contains " + words.size() + " words beginning with 'x'.", Toast.LENGTH_LONG).show();
        	
        	// Open the search page
        	Intent mainIntent = new Intent(this, Search.class);
        	startActivity(mainIntent);
        }
        else {
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	AlertDialog dialog = builder.create();
        	dialog.setIcon(R.drawable.icon);
        	dialog.setTitle("Dictionary Not Loaded");
        	dialog.setMessage("Couldn't find word data.");
        	dialog.setCancelable(false);
        	dialog.setButton("Quit", new QuitListener(this));
        	dialog.show();
        }
    }

	private class QuitListener implements OnClickListener {
		private Activity activity;

		public QuitListener(Activity activity) {
			super();
			this.activity = activity;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
			activity.finish();
		}
	}
}