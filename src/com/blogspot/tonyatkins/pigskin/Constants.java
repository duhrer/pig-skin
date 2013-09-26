package com.blogspot.tonyatkins.pigskin;

import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;

import android.os.Environment;



public class Constants {
	public static final String TAG = "pigskin";

	public static final String HOME_DIRECTORY = Environment.getExternalStorageDirectory() + "/pigskin";
	public static final String INDEX_DIRECTORY = HOME_DIRECTORY + "/indexes";
	
	// Preference keys
	public static final String FULL_SCREEN_PREF = "fullScreen";
	public static final String DICTIONARY_PREF = "dictionary";
	
	// Defaults
	public static final boolean DEFAULT_FULL_SCREEN=true;
	public static final String DEFAULT_DICTIONARY= "sowpods";
	
	public final static String[] DICTIONARIES = new String[] { "sowpods", "opentaal"};
	public static final String SEARCH_FIELD = "word";
	public static final int MAX_SEARCH_RESULTS = 100;
	public static final int BUFFER_SIZE = 4096;
	
	// Only used in generating index files, not used by the app itself.
	public final static String DICTIONARY_ROOT_FILESYSTEM_PATH = "/Users/etht/Source/pig-skin/extras/dictionary";
	public final static String DICTIONARY_INDEX_ROOT_FILESYSTEM_PATH = "/Users/etht/Source/pig-skin/assets/dictionary/indexes";

	public static final CharArraySet STOP_WORDS = new CharArraySet(Version.LUCENE_40,0,true);
}
