package com.blogspot.tonyatkins.pigskin.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a helper class designed to create the required dictionary files based on a single combined text file with one word per line.
 * This should be tailored to match the dictionary name and run from the command line.
 * 
 * @author duhrer
 */
public class GenerateDictionaryFiles {
	private final static String DICTIONARY = "nl";
	private final static String DICTIONARY_ROOT_PATH = "C:/Users/duhrer/workspace/PigSkin/assets/dictionary";
	private final static String DICTIONARY_PATH = DICTIONARY_ROOT_PATH + "/" + DICTIONARY + "/";
	private final static String DICTIONARY_FILE = DICTIONARY + ".mp3";
	
	public static void main(String[] args) {
		// break down each word and store it in an array based on the first two letters
		Map<String,List<String>> wordData = new HashMap<String,List<String>>();
		
		File dictionaryFile = new File(DICTIONARY_PATH + "/" + DICTIONARY_FILE);
		try {
			BufferedReader in = new BufferedReader(new FileReader(dictionaryFile));
			String oldPrefix = "";
			String line;
			List<String> wordList = new ArrayList<String>();
			while ((line = in.readLine()) != null) {
				String word = line.trim().toLowerCase();
				if (word.length() >= 2) {
					String prefix = word.substring(0, 2);
					
					if (!prefix.equals(oldPrefix)) {
						oldPrefix = prefix;
						wordList = wordData.get(prefix);
						if (wordList == null) {
							wordList = new ArrayList<String>();
							wordData.put(prefix, wordList);
						}
					}
					
					wordList.add(word);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

		// generate output based on the data
		for (String prefix : wordData.keySet()) {
			try {
				FileWriter out = new FileWriter(DICTIONARY_PATH + "/words_" + prefix + ".txt");
				
				for (String word : wordData.get(prefix)) {
					out.append(word + "\n");
				}
				
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
