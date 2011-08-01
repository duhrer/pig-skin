package com.blogspot.tonyatkins.pigskin.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.collections.ListUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.blogspot.tonyatkins.pigskin.Constants;

public class SearchManager {
	private Map<String,List<String>> wordData = new HashMap<String,List<String>>();
	private Context context;
	private	SharedPreferences preferences; 
	
    public SearchManager(Context context) {
		super();
		this.context = context;
		
		this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
	}


	private List<String> getWordsForPrefix(String prefix) {
    	List<String> prefixWords = wordData.get(prefix);
    	
    	// if there's no data, load it
    	if (prefixWords == null) {
    		// load the required data
    		prefixWords = new ArrayList<String>();
    		wordData.put(prefix, prefixWords);

    		String dictionary = preferences.getString(Constants.DICTIONARY_PREF, Constants.DEFAULT_DICTIONARY);
    		
    		InputStream in;
			try {
				in = context.getAssets().open("dictionary/" + dictionary + "/words_" + prefix + ".txt");
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String line;
				while ((line = reader.readLine()) != null) {
					prefixWords.add(line.trim().toLowerCase());
				}
			} catch (IOException e) {
				Log.e(getClass().toString(), "Can't load word data", e);
			}
    	}
    	
    	return prefixWords;
   }

	public List<String> getWordsStartingWith(String searchString) {
		// FIXME: Add support for blank characters
		List<String> wordsStartingWith = new ArrayList<String>();
		String prefix = searchString.substring(0, 2).toLowerCase();
		
		List<String> letterWords = getWordsForPrefix(prefix);
	
		// look for words starting with the string first
		for (String word : letterWords) {
			if (word.toLowerCase().startsWith(searchString)) { wordsStartingWith.add(word); }
		}
		
		return wordsStartingWith;
	}
	
	public boolean isValidWord(String wordToValidate) {
		String prefix = wordToValidate.substring(0, 2).toLowerCase();
		List<String> letterWords = getWordsForPrefix(prefix);
	
		for (String word : letterWords) {
			if (word.toLowerCase().equals(wordToValidate.toLowerCase())) { return true; }
		}
		
		return false;
	}

	public List<String> findBingo(String searchString) {
		// FIXME: Add support for blank characters
		
		// FIXME: Make our own extension of SortedSet with a longest, then alphabetical sort order
		List<String> validAnagrams = new ArrayList<String>();

		// we only work with 7-letter words for this search
		if (searchString != null && searchString.length() == 7) {
			// the list of possible matches
			List<String> anagrams = new ArrayList<String>();
			TreeSet<String> prefixes = new TreeSet<String>();
			
			// build the list of candidates
			for (int a=0; a < 7; a++) {
				for (int b=0; b < 7; b++) {
					if (b == a) { continue; }
					for (int c=0; c < 7; c++) {
						if (c == b || c == a ) { continue; }
						for (int d=0; d<7; d++) {
							if (d ==c || d == b || d == a ) { continue; }
							for (int e=0; e<7; e++) {
								if (e ==d || e ==c || e == b || e == a ) { continue; }
								for (int f=0; f<7; f++) {
									if (f==e || f==d || f ==c || f == b || f == a ) { continue; }
									for (int g=0; g<7; g++) {
										if (g==f || g==e || g==d || g==c || g==b || g==a) { continue; }	 
										char[] charData = { searchString.charAt(a),
															searchString.charAt(b),
															searchString.charAt(c),
															searchString.charAt(d),
															searchString.charAt(e),
															searchString.charAt(f),
															searchString.charAt(g)};
										
										String candidateWord = new String(charData);
										if (!anagrams.contains(candidateWord)) {
											anagrams.add(candidateWord);
										}
										
										prefixes.add(candidateWord.substring(0, 2));
									}
								}
							}
						}
					}
				}

			}
			
			List<String> validWords = new ArrayList<String>();
			
			for (String prefix : prefixes) {
				validWords.addAll(getWordsForPrefix(prefix));
			}
			
			// validate the list
			validAnagrams = ListUtils.intersection(anagrams, validWords);
		}
		
		return validAnagrams;
	}
}
