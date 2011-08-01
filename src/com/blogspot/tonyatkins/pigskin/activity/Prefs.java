package com.blogspot.tonyatkins.pigskin.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.view.WindowManager;
import android.widget.Toast;

import com.blogspot.tonyatkins.pigskin.Constants;
import com.blogspot.tonyatkins.pigskin.R;


public class Prefs extends PreferenceActivity {
	private static final int TTS_CHECK_CODE = 777;
	public static final int EDIT_PREFERENCES = 999;
	public static final int RESULT_PREFS_CHANGED = 134;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);

		// register for preference changes
		preferences.registerOnSharedPreferenceChangeListener(new PreferenceChangeListener(this));

		boolean fullScreen = preferences.getBoolean(Constants.FULL_SCREEN_PREF,
				Constants.DEFAULT_FULL_SCREEN);
		if (fullScreen) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}

		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);

		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, TTS_CHECK_CODE);
		
		setResult(RESULT_OK);
	}


	private class PreferenceChangeListener implements OnSharedPreferenceChangeListener {
		private Context context;

		public PreferenceChangeListener(Context context) {
			this.context = context;
		}

		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
			Toast.makeText(context, "Preferences updated...", Toast.LENGTH_LONG).show();
			setResult(RESULT_PREFS_CHANGED);
		}

	}
}
