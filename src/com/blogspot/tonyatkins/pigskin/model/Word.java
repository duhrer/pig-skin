package com.blogspot.tonyatkins.pigskin.model;

public class Word {
	// Column names for table creation
	public static final String _ID            = "id";
	public static final String WORD          = "word";

	// table name
	public static final String TABLE_NAME = "words";
	
	// the list of columns
	public static final String[] COLUMNS = {
			_ID,
			WORD
	};

	private long id;
	private String word;
	
	public long getId() {
		return id;
	}
	public String getWord() {
		return word;
	}
}
