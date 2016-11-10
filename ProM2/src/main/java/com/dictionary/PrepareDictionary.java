package com.dictionary;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PrepareDictionary {

	private HashMap<String, HashSet<String>> dict = new HashMap<String, HashSet<String>>();
	private String dictinoaryLocation = "";
	
	public PrepareDictionary() throws ClassNotFoundException, IOException {
		dict = readFromDictionary();
	}
	
	public PrepareDictionary(String dictionaryFileLocation) throws ClassNotFoundException, IOException {
		this.dictinoaryLocation = dictionaryFileLocation;
		dict = readFromDictionary();
	}
	
	public HashMap getDictionary() {
		return this.dict;
	}
	
	private HashMap readFromDictionary() throws IOException, ClassNotFoundException {
		
//		String fileName = "C:/Users/Harish/Desktop/IR/Data/Data English/masc_500k_texts/written/letters/index/dict.txt";
		
		if(dictinoaryLocation.equals(""))
			return null;
		try {
			FileInputStream fileInputStream  = new FileInputStream(dictinoaryLocation);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
	
			Map myNewlyReadInMap = (HashMap) objectInputStream.readObject();
			objectInputStream.close();
			
			System.out.println("Retrieved Dictionary Index from File");
			return (HashMap) myNewlyReadInMap;
		} catch(IOException e) {
			return dict;
		}
	}

	
	public void addStemWordsToDictionary(Set stemWords, String fileName) {
		addWordsToDictionary(stemWords, fileName);
	}
	
	private void addWordsToDictionary(Set stemWords, String fileName) {

		Iterator<String> iterate = stemWords.iterator();
		HashSet<String> list = new HashSet<String>();
		list.add(fileName);
			
		System.out.println("Stem word size : "+stemWords.size());
		
		while(iterate.hasNext()) {
			String s = iterate.next();
			if(dict.containsKey(s)) {
				HashSet hs = dict.get(s);
				hs.add(fileName);
				dict.put(s, hs);
			} else {
				dict.put(s, list);
			}
		}
		
		System.out.println("Dictionary Size : "+dict.size());
	}
	
	/*
	 * Add New Dictionary to the Old Dictionary
	 */
	public void addNewToOldDictionary(HashMap<String, HashSet<String>>  newOne) {
		
		System.out.println("Old Dict Size : "+dict.size());
		    Iterator it = newOne.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		    	if(dict.containsKey(pair.getKey())) {
		    		HashSet hs = dict.get(pair.getKey());
		    		hs.addAll((Collection) pair.getValue());
		    		dict.put((String) pair.getKey(), hs);
		    	} else {
		    		dict.put((String) pair.getKey(), (HashSet<String>) pair.getValue());
		    	}
		    }
			System.out.println("Added Dict Size : "+dict.size());		    
	}
	
	public boolean storeDictionaryInSameLocation() throws IOException {
		return storeDictionary(this.dictinoaryLocation);
	}

	public boolean storeDictionaryInDifferentLocation(String dictionaryLocation) throws IOException {
		return storeDictionary(dictionaryLocation);
	}
	
	private boolean storeDictionary(String dictionaryLocation) throws IOException {
		
		if(dictionaryLocation.equals(""))
			return false;
		
		FileOutputStream fileOutputStream = new FileOutputStream(dictinoaryLocation);
		ObjectOutputStream objectOutputStream= new ObjectOutputStream(fileOutputStream);

		objectOutputStream.writeObject(dict);
		objectOutputStream.close();
		
		System.out.println("stored in file");
		return true;
	}
	
}
