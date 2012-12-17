package com.blogspot.tonyatkins.pigskin;


public class Constants {
	// Preference keys
	public static final String FULL_SCREEN_PREF = "fullScreen";
	public static final String DICTIONARY_PREF = "dictionary";
	
	// Defaults
	public static final boolean DEFAULT_FULL_SCREEN=true;
	public static final String DEFAULT_DICTIONARY= "sowpods";
	
	public final static String[] DICTIONARIES = new String[] { "sowpods", "opentaal"};
	public final static String DICTIONARY_ROOT_FILESYSTEM_PATH = "/Users/aatkins/Source/pig-skin/extras/dictionary";
	public final static String DICTIONARY_INDEX_ROOT_FILESYSTEM_PATH = "/Users/aatkins/Source/pig-skin/assets/dictionary/indexes";
	public static final String SEARCH_FIELD = "word";
	public static final int MAX_SEARCH_RESULTS = 100;
	public static final int BUFFER_SIZE = 4096;
}
