package com.parsing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import com.dictionary.PrepareDictionary;
import com.search.Searcher;
import com.stemmers.EnglishWordStemmer;
import com.stemmers.StopWords;

public class RunIR {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter Stop words Location : ");
		String loc1 = scan.next();
		
		System.out.println("Enter Hindi Repository : ");
		String loc2 = scan.next();
		
		System.out.println("Enter Index Location : ");
		String loc3 = scan.next();
		
		
		/*// Stop Words Location
		private String fileLoc = "C:/Users/Harish/Desktop/IR/Data/stop words/stopwords.txt";
		private String fileLoc = "C:/Users/Harish/Desktop/IR/Data/stop words/stopwords_hindi.txt";
		String stopLoc = "C:/Users/Harish/Desktop/IR/Data/stop words/stopwords_zh.txt";
		
		
		// Dictionary-Index Location		
		private String dictinoaryLocation = "C:/Users/Harish/Desktop/IR/Data/Data English/masc_500k_texts/written/letters/index/dict.txt";
		private String dictinoaryLocation = "C:/Users/Harish/Desktop/IR/Data/Hindi Data/hin_corp_unicode/index/hin-dict.txt";
		String dictinoaryLocation = "C:/Users/Harish/Desktop/IR/Data/Hindi Data/hin_corp_unicode/index/chinese-dict.txt";
		
		
		// Repository Location
		String folderLoc = "C:/Users/Harish/Desktop/IR/Data/Chinese Data/output/";
		String folderLoc = "C:/Users/Harish/Desktop/IR/Data/Hindi Data/hin_corp_unicode/sample/";
		String folderLoc = "C:/Users/Harish/Desktop/IR/Data/Data English/masc_500k_texts/written/letters/";
		*/
		
		makeAndStoreDictionary(loc1, loc2, loc3);

		
	}
	
	public static void searchDictionary(String stopWordsLoc, String dictinoaryLocation) throws IOException, ClassNotFoundException {

		// Gather Stop words
		StopWords stops = new StopWords(stopWordsLoc);		
		
		// Read the initial index	
		EnglishWordStemmer stem = new EnglishWordStemmer(stops);
		
		Scanner scan = new Scanner(System.in);
		String query = scan.nextLine();

		// Collected the result words in Query after performing stemming
		Set set = stem.getQueryStemmedWords(query);
		
		// Creating an index dictionary
		PrepareDictionary dictionary = new PrepareDictionary(dictinoaryLocation);
		Searcher searcher = new Searcher(dictionary);
		
		// Performing search with the dictionary and query set
		HashMap<String, Integer> map = (HashMap<String, Integer>) searcher.searchQuery(set);
		iterateMap(map);
	}
	
	public static void iterateSet(Set set) {
		
		System.out.println("Set Size : "+set.size());
		Iterator iterate = set.iterator();
		while(iterate.hasNext()) {
			System.out.println(iterate.next());
		}
	}
	
	public static void makeAndStoreDictionary(String stopLoc, String dictinoaryLocation, String repoLoc) throws ClassNotFoundException, IOException {

		// Gather Stop words
		StopWords stops = new StopWords(stopLoc);		

		// Read the initial index
		PrepareDictionary dictionary = new PrepareDictionary(dictinoaryLocation);

		// Read the initial index
		EnglishWordStemmer stem = new EnglishWordStemmer(stops, dictionary);		
		
		// Store the resultant dictionary in a different or same location
		HashMap<String, HashSet<String>> map = (HashMap<String, HashSet<String>>) stem.getNewDictionaryOfLocation(dictinoaryLocation);		

		 dictionary.addNewToOldDictionary(map);
		 if(dictionary.storeDictionaryInSameLocation())
			 System.out.println("Stored successfully");
		 else
			 System.out.println("Not Stored successfully");
	}

	public static void iterateMap(Map map) {
		
	    Iterator it = map.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.println(pair.getKey() + " = " + ((HashSet) pair.getValue()).size()+" "+pair.getValue());
	    }
	}
}
