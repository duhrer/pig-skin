package com.blogspot.tonyatkins.pigskin.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.blogspot.tonyatkins.pigskin.model.Word;

public class DbHelper extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "pigskin";
	public static final int DATABASE_VERSION = 1;
	private static final int DB_INSERT_BATCH_SIZE = 500;
	
	private Activity activity;
	
	public DbHelper(Activity activity) {
		super(activity, DATABASE_NAME, null, DATABASE_VERSION);
		this.activity = activity;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Toast.makeText(activity, "Creating database...", Toast.LENGTH_SHORT).show();
		
		try {
			// create a table for each letter
			for (char letter = 'a'; letter <='z'; letter++) {
				db.execSQL("CREATE TABLE " +
							Word.TABLE_NAME + "_" + letter + " (" +
							Word._ID + " integer primary key, " +
							Word.WORD + " varchar(15));");
			}
		} catch (SQLException e) {
			Log.e(getClass().toString(), "Error creating table:", e);
			Toast.makeText(activity, "Error creating table, check log for details...", Toast.LENGTH_LONG).show();
		}
		
		// We're going to make the user explicitly start the data load with a dialog
		//loadData(db);
	}

	public void loadData(SQLiteDatabase db) {
		// turn off database locking to improve performance
		db.setLockingEnabled(false);

		
		// load the data
		try {
			Toast.makeText(activity, "Loading data...", Toast.LENGTH_SHORT).show();
			
			InputStream in = activity.getAssets().open("sowpods.mp3");
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));

			ProgressDialog dialog = new ProgressDialog(activity);
			int progress = 0;
			int max = 267751;
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			dialog.setTitle("Loading SOWPODS data...");
			dialog.setMax(max);
			dialog.setProgress(progress);
			dialog.show();
			
			String line;
			
			char oldLetter = 'a';
			List<String> wordList = new ArrayList<String>();;
			while ((line = reader.readLine()) != null) {
				String word = line.trim().toLowerCase();
				char letter = word.charAt(0);
				if (letter != oldLetter){
					addWords(db,letter,wordList);
					oldLetter = letter;
					wordList = new ArrayList<String>();
				}
				
				wordList.add(word);
				dialog.setProgress(progress++);
			}
			
			addWords(db,oldLetter,wordList);
			
			dialog.dismiss();
			
			Toast.makeText(activity, "Finished loading SOWPODS data...", Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			Log.e(this.getClass().toString(), "Error loading SOWPODS data from file.", e);
			Toast.makeText(activity, "Error loading SOWPODS data...", Toast.LENGTH_LONG).show();
		}
		
		// turn database locking back on
		db.setLockingEnabled(true);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// There are no upgrades possible at the moment, so this is blank for now.
	}
	
	public void addWords(SQLiteDatabase db, char letter, List<String> words) {
		try {
			db.beginTransaction();

			int batchRecord = 0;
			
			Iterator<String> iterator = words.iterator();
			while (iterator.hasNext()) {
				batchRecord++;
				String word = iterator.next();
				ContentValues values = new ContentValues();
				values.put(Word.WORD, word);
				db.insertOrThrow(Word.TABLE_NAME + "_" + letter, null, values);
				if (batchRecord >= DB_INSERT_BATCH_SIZE) {
					db.endTransaction();
					batchRecord=0;
					db.beginTransaction();
				}
			}
			
			db.endTransaction();
		} catch (SQLException e) {
			Log.e(getClass().toString(), "Error inserting data:", e);
			Toast.makeText(activity, "Error inserting data, check log for details...", Toast.LENGTH_LONG).show();
		}
	}
	
	public boolean isWordValid(SQLiteDatabase db, String word) {
		char letter = word.charAt(0);
		Cursor cursor = db.query(Word.TABLE_NAME + "_" + letter, Word.COLUMNS , Word.WORD + "=" + word, null, null, null, null);
		if (cursor.getCount() > 0) { return true; }

		return false;
	}
	
	public Cursor getWordsStartingWith(SQLiteDatabase db, String pattern) {
		char letter = pattern.charAt(0);
		return db.query(Word.TABLE_NAME + "_" + letter, Word.COLUMNS , Word.WORD + " like '" + pattern + "%'", null, null, null, Word.WORD);
	}
	
	public int getWordCount(SQLiteDatabase db, char letter) {
		Cursor cursor = db.query(Word.TABLE_NAME + "_" + letter, Word.COLUMNS , null, null, null, null, Word.WORD);
		return cursor.getCount();
	}
}
