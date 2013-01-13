package com.blogspot.tonyatkins.pigskin.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.blogspot.tonyatkins.pigskin.Constants;

public class SearchManager {
	private Map<String, List<String>> wordData = new HashMap<String, List<String>>();
	private Context context;
	private final String dictionary;

	public SearchManager(Context context) {
		super();
		this.context = context;

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		dictionary = preferences.getString(Constants.DICTIONARY_PREF,
				Constants.DEFAULT_DICTIONARY);
	}

	// FIXME:  Make this work with Lucene
//	public List<String> findBingo(String searchString) {
//		// FIXME: Add support for blank characters
//
//		// FIXME: Make our own extension of SortedSet with a longest, then
//		// alphabetical sort order
//		List<String> validAnagrams = new ArrayList<String>();
//
//		// we only work with 7-letter words for this search
//		if (searchString != null && searchString.length() == 7) {
//			// the list of possible matches
//			List<String> anagrams = new ArrayList<String>();
//			TreeSet<String> prefixes = new TreeSet<String>();
//
//			// build the list of candidates
//			for (int a = 0; a < 7; a++) {
//				for (int b = 0; b < 7; b++) {
//					if (b == a) {
//						continue;
//					}
//					for (int c = 0; c < 7; c++) {
//						if (c == b || c == a) {
//							continue;
//						}
//						for (int d = 0; d < 7; d++) {
//							if (d == c || d == b || d == a) {
//								continue;
//							}
//							for (int e = 0; e < 7; e++) {
//								if (e == d || e == c || e == b || e == a) {
//									continue;
//								}
//								for (int f = 0; f < 7; f++) {
//									if (f == e || f == d || f == c || f == b
//											|| f == a) {
//										continue;
//									}
//									for (int g = 0; g < 7; g++) {
//										if (g == f || g == e || g == d
//												|| g == c || g == b || g == a) {
//											continue;
//										}
//										char[] charData = {
//												searchString.charAt(a),
//												searchString.charAt(b),
//												searchString.charAt(c),
//												searchString.charAt(d),
//												searchString.charAt(e),
//												searchString.charAt(f),
//												searchString.charAt(g) };
//
//										String candidateWord = new String(
//												charData);
//										if (!anagrams.contains(candidateWord)) {
//											anagrams.add(candidateWord);
//										}
//
//										prefixes.add(candidateWord.substring(0,
//												2));
//									}
//								}
//							}
//						}
//					}
//				}
//
//			}
//
//			List<String> validWords = new ArrayList<String>();
//
//			for (String prefix : prefixes) {
//				validWords.addAll(getWordsForPrefix(prefix));
//			}
//
//			// validate the list
//			// validAnagrams = ListUtils.intersection(anagrams, validWords);
//		}
//
//		return validAnagrams;
//	}

	public String getDictionary() {
		return dictionary;
	}

	public List<String> getWordsStartingWith(String searchString) {
		return getWordsMatching(searchString + "*");
	}

	public List<String> getWordsMatching(String searchString) {
		List<String> matchingWords = new ArrayList<String>();
		IndexReader reader;
		try {
			File indexDirectory = new File(Constants.INDEX_DIRECTORY + "/" + dictionary);
			// TODO:  Move this to a startup activity instead
			if (!indexDirectory.exists()){
				Log.d(Constants.TAG, "Index directory doesn't exist.  Creating it now...");
				if (indexDirectory.mkdirs()) {
					Log.d(Constants.TAG, "Created index directory '" + indexDirectory.getAbsolutePath() + "'.");
				}
				else {
					Log.e(Constants.TAG, "Unable to create index directory '" + indexDirectory.getAbsolutePath() + "'.");
				}
			}
				
			InputStream assetInputStream = context.getAssets().open("dictionary/indexes/" + dictionary + ".zip" );
			ZipInputStream zin = new ZipInputStream(assetInputStream);
			ZipEntry ze;
			while ((ze = zin.getNextEntry()) != null) {
				if (ze.getName().contains("META")) {
					Log.d(Constants.TAG, "Skipping META information in dictionary zip.");
				}
				else if (ze.isDirectory()) {
					File unzippedDirectory = new File(indexDirectory + "/" + ze.getName());
					unzippedDirectory.mkdirs();
				}
				else {
					File file = new File(indexDirectory.getAbsolutePath() + "/" + ze.getName());
					if (file.exists()) {
						Log.d(Constants.TAG, "File '" + file.getAbsolutePath() + "' already exists, no need to recreate it.");
					}
					else {
						Log.d(Constants.TAG, "File '" + file.getAbsolutePath() + "' does not exist, creating from zip file.");
						
						BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file), Constants.BUFFER_SIZE);
						byte[] buffer = new byte[Constants.BUFFER_SIZE];
						int count;
						while ((count = zin.read(buffer, 0, Constants.BUFFER_SIZE)) != -1) {
							out.write(buffer, 0, count);
						}
						out.flush();
						out.close();					
					}
				}
			}
			
			reader = DirectoryReader.open(FSDirectory.open(indexDirectory));
			IndexSearcher searcher = new IndexSearcher(reader);
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
			QueryParser parser = new QueryParser(Version.LUCENE_40,Constants.SEARCH_FIELD, analyzer);
			Query query = parser.parse(searchString);
			TopDocs topDocs = searcher.search(query,Constants.MAX_SEARCH_RESULTS);
			for (ScoreDoc doc : topDocs.scoreDocs) {
				Document document = searcher.doc(doc.doc);
				matchingWords.add(document.get(Constants.SEARCH_FIELD));
			}
		} catch (Exception e) {
			Log.e(Constants.TAG, "Error opening index file:", e);
		}

		return matchingWords;
	}

	public boolean isValidWord(String wordToValidate) {
		List<String> letterWords = getWordsMatching(wordToValidate);
		if (letterWords.contains(wordToValidate)) return true;

		return false;
	}
}
