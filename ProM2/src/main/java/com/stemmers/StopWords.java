package com.stemmers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class StopWords {

	private HashSet<String> stopWords = new HashSet<String>();
	private String fileLoc = "";
	
	public StopWords() throws FileNotFoundException {		
		stopWords = retrieveAllStopWords();
	}
	
	public StopWords(String fileLocation) throws FileNotFoundException {		
		this.fileLoc = fileLocation;
		stopWords = retrieveAllStopWords();
	}

	public HashSet<String> getAllStopWords() {
		return this.stopWords;
	}
	
	private HashSet retrieveAllStopWords() throws FileNotFoundException {
		File file = new File(fileLoc);	
		Scanner scan = new Scanner(file);
	
		HashSet<String> stop = new HashSet<String>();
		while(scan.hasNext()) {
			String s = scan.next();
			stop.add(s);
		}
		
		scan.close();
		return stop;
	}
	
	/*
	 * Returns a list of works after removing Stop Words
	 */
	public Set<String> removeStopWordsFrom(Set stemWords) {
		
		int size1 = stemWords.size();
		
		Iterator iterate = stemWords.iterator();
		while(iterate.hasNext()) {
			if(stopWords.contains(iterate.next())) {
				iterate.remove();
			}
		}
		
		int size2 = stemWords.size();
		System.out.println("Stop Words : "+(size1-size2));
		return stemWords;		
	}
}
