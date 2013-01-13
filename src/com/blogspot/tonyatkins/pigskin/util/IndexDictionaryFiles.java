package com.blogspot.tonyatkins.pigskin.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.blogspot.tonyatkins.pigskin.Constants;

/**
 * This is a helper class designed to create the required dictionary files based
 * on a single combined text file with one word per line. This should run from the 
 * command line using commands like:
 * 
 * cd SOURCE_DIR
 * mvn clean install
 * java -classpath target/classes:libs/* com.blogspot.tonyatkins.pigskin.util.IndexDictionaryFiles
 * 
 * Once you've done that, the results will be stored in /tmp, under directories like index-opentaal.  
 * Use a tool like luke to make sure the indexes are usable.  Once you're satisfied, 
 * zip up each of the dictionaries using a command like:
 * 
 * for i in sowpods opentaal; do cd /tmp/index-$i; jar fvc /tmp/$i.zip *; cp /tmp/$i.zip ~/Source/pig-skin/assets/dictionary/indexes; done
 * 
 * @author duhrer
 */
public class IndexDictionaryFiles {
	public static void main(String[] args) {
		for (String dictionary : Constants.DICTIONARIES) {
			String dictionaryPath = Constants.DICTIONARY_ROOT_FILESYSTEM_PATH + "/" + dictionary + "/";
			String indexFile = "/tmp/index-" + dictionary;
			System.out.println("Starting processing of dictionary '" + dictionary + "'...");
			
			try {
				Directory dir = FSDirectory.open(new File(indexFile));
				// We need to make sure no words are ignored, so we send an empty set of "stop words"
				CharArraySet stopWords = new CharArraySet(Version.LUCENE_40,0,true);
				Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40, stopWords);
				IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_40,
						analyzer);
				iwc.setOpenMode(OpenMode.CREATE);
				IndexWriter writer = new IndexWriter(dir, iwc);
				
				// FIXME:  We now have a single text file in each directory.  We don't need the zip code any more
				File dictionaryFile = new File(dictionaryPath + "/" + dictionary + ".txt");

				System.out.println("Generating index files from dictionary file '" + dictionaryFile.getAbsolutePath() + "'.");
				BufferedReader reader = new BufferedReader(new FileReader(dictionaryFile));
				String line;
				while ((line = reader.readLine()) != null) {
					String word = line.trim().toLowerCase();
					if (word.length() >= 2) {
						Document doc = new Document();
						Field field = new TextField(Constants.SEARCH_FIELD, word, Field.Store.YES);
						doc.add(field);
						writer.addDocument(doc);
//							System.out.println("Added word '" + word + "' to dictionary file.");
					}
				}
				
				writer.close();
			} catch (Exception e ){
				System.out.println("Error generating index file.");
				e.printStackTrace();
			}
		}
	}
}