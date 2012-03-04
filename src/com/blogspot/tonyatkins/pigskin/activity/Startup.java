package com.blogspot.tonyatkins.pigskin.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.blogspot.tonyatkins.pigskin.Constants;
import com.blogspot.tonyatkins.pigskin.R;
import com.blogspot.tonyatkins.pigskin.view.TiledView;

public class Startup extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startup);

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

		boolean fullScreen = preferences.getBoolean(Constants.FULL_SCREEN_PREF,
				Constants.DEFAULT_FULL_SCREEN);
		if (fullScreen) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}

		// search button
		Button searchButton = (Button) findViewById(R.id.startupSearchButton);
		searchButton.setOnClickListener(new StartActivityListener(this, Search.class));

		// bingo button
		Button bingoButton = (Button) findViewById(R.id.startupBingoButton);
		bingoButton.setOnClickListener(new StartActivityListener(this, Bingo.class));

		// bingo with benefits
		Button bingoBenefitsButton = (Button) findViewById(R.id.startupBingoBenefitsButton);

		// preferences button
		Button prefsButton = (Button) findViewById(R.id.startupPrefsButton);
		prefsButton.setOnClickListener(new StartActivityListener(this, Prefs.class));

		// quit
		Button quitButton = (Button) findViewById(R.id.startupQuitButton);
		quitButton.setOnClickListener(new QuitListener(this));
	}

	// FIXME: Check to see if preferences have been updated and force a reload if
	// needed
	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// super.onActivityResult(requestCode, resultCode, data);
	// }

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