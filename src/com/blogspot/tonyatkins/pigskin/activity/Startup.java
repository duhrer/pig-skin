package com.blogspot.tonyatkins.pigskin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.blogspot.tonyatkins.pigskin.R;

public class Startup extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.startup);
        
        // search button
        Button searchButton = (Button) findViewById(R.id.startupSearchButton);
        searchButton.setOnClickListener(new StartActivityListener(this, Search.class));
        
        // bingo button
        Button bingoButton = (Button) findViewById(R.id.startupBingoButton);
        bingoButton.setOnClickListener(new StartActivityListener(this, Bingo.class));
        
        // bingo with benefits
        Button bingoBenefitsButton = (Button) findViewById(R.id.startupBingoBenefitsButton);
        
        // quit
        Button quitButton = (Button) findViewById(R.id.startupQuitButton);
        quitButton.setOnClickListener(new QuitListener(this));
    }
    

    private class StartActivityListener implements OnClickListener {
    	private Activity activity;
    	private Class startClass;

		public StartActivityListener(Activity activity, Class startClass) {
			super();
			this.activity = activity;
			this.startClass = startClass;
		}

		@Override
		public void onClick(View view) {
        	Intent mainIntent = new Intent(activity, startClass);
        	startActivity(mainIntent);
		}
    }
    
	private class QuitListener implements OnClickListener {
		private Activity activity;

		public QuitListener(Activity activity) {
			super();
			this.activity = activity;
		}

		@Override
		public void onClick(View view) {
			activity.finish();
		}
	}
}