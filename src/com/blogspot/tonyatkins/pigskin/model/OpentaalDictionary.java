package com.blogspot.tonyatkins.pigskin.model;

public class OpentaalDictionary implements Dictionary {

	@Override
	public String getName() {
		return "OpenTaal";
	}

	@Override
	public String getPrefix() {
		return "opentaal";
	}

	@Override
	public String getLookupURL(String word) {
		return "http://nl.thefreedictionary.com/" + word;
	}

}
