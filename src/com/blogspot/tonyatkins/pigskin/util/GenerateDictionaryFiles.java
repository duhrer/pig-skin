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
import java.util.TreeSet;

public class GenerateDictionaryFiles {
	private static String dictionaryPath = "C:/Users/duhrer/workspace/Pig Skin/assets/sowpods.mp3";
	private static String arrayOutput = "C:/Users/duhrer/workspace/Pig Skin/res/values/arrays_generated.xml";
	
	public static void main(String[] args) {
		// break down each word and store it in an array based on the first two letters
		Map<String,List<String>> wordData = new HashMap<String,List<String>>();
		
		File dictionaryFile = new File(dictionaryPath);
		try {
			BufferedReader in = new BufferedReader(new FileReader(dictionaryFile));
			String oldPrefix = "";
			String line;
			List<String> wordList = new ArrayList<String>();
			while ((line = in.readLine()) != null) {
				String word = line.trim().toLowerCase();
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

		// generate output based on the data
		
		/*
		 * <?xml version="1.0" encoding="utf-8"?>
				<resources>
					<string-array name="words_u">
						<item>UAKARI</item>
		 */
		try {
			// Save the output to a file
			FileWriter out = new FileWriter(new File(arrayOutput));
			out.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			out.append("\t<resources>\n");

			for (String key: new TreeSet<String>(wordData.keySet())) {
				List<String> values = wordData.get(key);
				
				if (values != null && values.size() > 0) {
					int wordCount = 0;
					int segment = 0;
					out.append("\t\t<string-array name=\"words_" + key + "_" + segment + "\">\n");
					for (String word : values) {
						if (wordCount >= 512) {
							wordCount = 0;
							segment++;
							out.append("\t\t</string-array>\n");
							out.append("\t\t<string-array name=\"words_" + key + "_" + segment + "\">\n");
						}
						
						out.append("\t\t\t<item>" + word +  "</item>\n");
						wordCount++;
					}
					out.append("\t\t</string-array>\n");
				}
			}

			out.append("\t</resources>\n");
			out.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
