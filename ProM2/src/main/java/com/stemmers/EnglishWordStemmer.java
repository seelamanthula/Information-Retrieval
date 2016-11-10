package com.stemmers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.dictionary.PrepareDictionary;
import com.google.common.io.Files;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class EnglishWordStemmer {

	private String folderLocation = "";
	private StopWords stopWords = null;
	private PrepareDictionary dictionary = null;
	
	public EnglishWordStemmer(StopWords stopWords) throws IOException {
		this.stopWords = stopWords;
	}
	
	public EnglishWordStemmer(StopWords stopWords, PrepareDictionary dictionary) throws IOException {
		this.stopWords = stopWords;
		this.dictionary = dictionary;
	}
	
	/*
	 * @Param
	 * 	A New location of files where needs to perform Stemming and Stopping
	 * 
	 * @Return
	 * 	A New dictionary, that contains only those files dictionary word specified in the folder location
	 */
	public Object getNewDictionaryOfLocation(String folderLocation) throws IOException {
		this.folderLocation = folderLocation;
		return getStemmedWordListFromFiles();
	}

	private Object getStemmedWordListFromFiles() throws IOException {
		
		Set<String> stemmedWords = new HashSet<String>();
		if(folderLocation.equals(""))
			return null;
		
		File folder = new File(folderLocation);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
		  File file = listOfFiles[i];
		  if (file.isFile() && file.getName().endsWith(".txt")) {
			  getSingleFileStemmedStopWords(file);
		  } 
		}
		
		return dictionary.getDictionary();
	}

	private StanfordCoreNLP getPipeLine() {
	    Properties props = new Properties();
	    props.put("annotators", "tokenize, ssplit, pos, lemma");
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	    
	    return pipeline;
	}

	// Assuming there are different file Location
	private void getSingleFileStemmedStopWords(File file) throws IOException {
		
		Set resultWords = getSingleFileStemStopWords(file);
		// addthese stemers to dict
		dictionary.addStemWordsToDictionary(resultWords, file.getAbsolutePath());		
	}
	
	/*
	 * @Param
	 * 	New File where stemming is done
	 * 
	 * @Return
	 * 	Removes all the stop words in that file and returns a new stemmed words in thst file
	 */

	public Set<String> getSingleFileStemStopWords(File file) throws IOException {
		Set stemmedWords = getStemmedWords(file);
		// Remove Stop Words
		return stopWords.removeStopWordsFrom(stemmedWords);
	}
	
	public Set<String> getQueryStemmedWords(String query) throws IOException {
		Set stemmedWords = getStemmedWords(query);
		// Remove Stop Words
		return stopWords.removeStopWordsFrom(stemmedWords);
	}
	
	private Set getStemmedWords(File file) throws IOException {
	    
		System.out.println("File Name : "+file.getName());	
		String text = Files.toString(file, Charset.forName("UTF-8"));
		
		return getStemmedWords(text);
	}
	
	private Set getStemmedWords(String text) {
		StanfordCoreNLP pipeline = getPipeLine();

		// create an empty Annotation just with the given text
	    Annotation document = new Annotation(text);

	    // run all Annotators on this text
	    pipeline.annotate(document);

	    // these are all the sentences in this document
	    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);

	    Set<String> lemmatizedSet = new HashSet<String>(); 
	    
	    for(CoreMap sentence: sentences) {
	      // traversing the words in the current sentence
	      // a CoreLabel is a CoreMap with additional token-specific methods
	      for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
	        String le = token.get(LemmaAnnotation.class);
	        lemmatizedSet.add(le);
	      }
	    }

	    return lemmatizedSet;	  
	}

}
