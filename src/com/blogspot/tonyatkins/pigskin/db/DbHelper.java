package com.blogspot.tonyatkins.pigskin.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.TreeSet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.blogspot.tonyatkins.pigskin.model.Word;

public class DbHelper extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "pigskin";
	public static final int DATABASE_VERSION = 1;
	
	private Activity activity;
	
	public DbHelper(Activity activity) {
		super(activity, DATABASE_NAME, null, DATABASE_VERSION);
		this.activity = activity;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Toast.makeText(activity, "Creating database...", Toast.LENGTH_SHORT);
		
		// create the table
		db.execSQL(Word.TABLE_CREATE);

		loadData(db);
	}

	private void loadData(SQLiteDatabase db) {
		// load the data
		try {
			Toast.makeText(activity, "Loading data...", Toast.LENGTH_SHORT);
			
			InputStream in = activity.getAssets().open("sowpods.mp3");
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));

			ProgressDialog dialog = new ProgressDialog(activity);
			int progress = 0;
			int max = 267751;
			dialog.setTitle("Loading SOWPODS data...");
			dialog.setMax(max);
			dialog.setProgress(progress);
			dialog.show();
			
			String line;
			while ((line = reader.readLine()) != null) {
				addWord(db, line.trim());
				dialog.setProgress(progress++);
			}

			dialog.dismiss();
			
			Toast.makeText(activity, "Finished loading SOWPODS data...", Toast.LENGTH_SHORT);
		} catch (IOException e) {
			Log.e(this.getClass().toString(), "Error loading SOWPODS data from file.", e);
			Toast.makeText(activity, "Error loading SOWPODS data...", Toast.LENGTH_LONG);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// There are no upgrades possible at the moment, so this is blank for now.
	}

	public long addWord(SQLiteDatabase db, String word) {
		ContentValues values = new ContentValues();
		values.put(Word.WORD, word);
		return db.insert(Word.TABLE_NAME, null, values );
	}
	
	public boolean isWordValid(SQLiteDatabase db, String word) {
		Cursor cursor = db.query(Word.TABLE_NAME, Word.COLUMNS , Word.WORD + "=" + word, null, null, null, null);
		if (cursor.getCount() > 0) { return true; }

		return false;
	}
	
	public Cursor getWordsMatching(SQLiteDatabase db, String pattern) {
		TreeSet<String> words = new TreeSet<String>();
		return db.query(Word.TABLE_NAME, Word.COLUMNS , Word.WORD + " like '%" + pattern + "%'", null, null, null, Word.WORD);
	}
	
	public Cursor getWordsStartingWith(SQLiteDatabase db, String pattern) {
		TreeSet<String> words = new TreeSet<String>();
		return db.query(Word.TABLE_NAME, Word.COLUMNS , Word.WORD + " like '" + pattern + "%'", null, null, null, Word.WORD);
	}
	
	public int getWordCount(SQLiteDatabase db) {
		Cursor cursor = db.query(Word.TABLE_NAME, Word.COLUMNS , null, null, null, null, Word.WORD);
		return cursor.getCount();
	}
}
