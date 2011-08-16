package com.blogspot.tonyatkins.pigskin.model;

public class SowpodsDictionary implements Dictionary {

	@Override
	public String getName() {
		return "SOWPODS";
	}

	@Override
	public String getPrefix() {
		return "sowpods";
	}

	@Override
	public String getLookupURL(String word) {
		return "http://dictionary.reference.com/browse/" + word; 
	}
}
