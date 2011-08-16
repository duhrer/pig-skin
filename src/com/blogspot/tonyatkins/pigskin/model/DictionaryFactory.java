package com.blogspot.tonyatkins.pigskin.model;

import java.util.HashMap;
import java.util.Map;

public class DictionaryFactory {
	private Map<String,Dictionary> dictionaries = new HashMap<String,Dictionary>();

	public DictionaryFactory() {
		OpentaalDictionary nlDict = new OpentaalDictionary();
		dictionaries.put(nlDict.getPrefix(), nlDict);
		
		SowpodsDictionary sowpodsDict = new SowpodsDictionary();
		dictionaries.put(sowpodsDict.getPrefix(), sowpodsDict);
	}
	
	public Dictionary getDictionary(String prefix) {
		return dictionaries.get(prefix);
	}
}
