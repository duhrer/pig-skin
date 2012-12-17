package com.blogspot.tonyatkins.pigskin.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
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
 * on a single combined text file with one word per line. This should be
 * tailored to match the dictionary name and run from the command line.
 * 
 * @author duhrer
 */
public class IndexDictionaryFiles {
	public static void main(String[] args) {
		for (String dictionary : Constants.DICTIONARIES) {
			String dictionaryPath = Constants.DICTIONARY_ROOT_FILESYSTEM_PATH + "/" + dictionary + "/";
			String dictionaryFile = dictionary + ".txt";
			String indexFile = "/tmp/index-" + dictionary;
			System.out.println("Starting processing of dictionary '" + dictionary + "'...");
			
			try {
				Directory dir = FSDirectory.open(new File(indexFile));
				Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
				IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_40,
						analyzer);
				iwc.setOpenMode(OpenMode.CREATE);
				IndexWriter writer = new IndexWriter(dir, iwc);
				
				File dictionaryDir = new File(dictionaryPath);
				FileFilter filter = new FileFilter() {
					@Override
					public boolean accept(File pathname) {
						if (pathname.getName().endsWith(".zip")) return true;
						return false;
					}
				};
				
				for (File dictionaryZipFile : dictionaryDir.listFiles(filter)) {
					ZipInputStream zin = new ZipInputStream(new FileInputStream(dictionaryZipFile));
					ZipEntry ze;
					while ((ze = zin.getNextEntry()) != null) {
						if (ze.getName().matches("words_.*.txt")) {
							BufferedReader reader = new BufferedReader(new InputStreamReader(zin));
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
						}
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