package com.search;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.dictionary.PrepareDictionary;

public class Searcher {

	private HashMap<String, HashSet<String>> dict = new HashMap<String, HashSet<String>>();
	private PrepareDictionary dictionary = null;
	
	public Searcher(PrepareDictionary dictionary) {
		this.dictionary = dictionary;
		this.dict = dictionary.getDictionary();
	}
	
	public Object  searchQuery(Set<String> stemWords) {
		return findInDictionary(stemWords);
	}
	
	private Object findInDictionary(Set<String> stemWords) {
		
		HashMap<String, HashSet<String>> map = new HashMap<String, HashSet<String>>();
		System.out.println("Stemmers size : "+stemWords.size());
		
		Iterator<String> iterate = stemWords.iterator();
		while(iterate.hasNext()) {
			String s = iterate.next();
			if(dict.containsKey(s)) {
				if(map.containsKey(s)) {
					Set k = map.get(s);
					k.addAll(dict.get(s));
					map.put(s, (HashSet<String>) k);
					
				} else {
				//	map.put(s, 1);
					map.put(s, dict.get(s));
				}					
			}
		}
		
		System.out.println("Query Size : "+map.size());
		return map;
	}
}
