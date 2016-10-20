package com.pages;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;

public class RetrieveFiles {

	StandardAnalyzer analyzer = new StandardAnalyzer();
	ArrayList queue = new ArrayList();
	IndexWriter writer = null;
	
	String folderName = "C:/Users/Harish/Desktop/Dr.Atrey/presentation/Secure Data Deduplication Papers/";
	String indexDir = "C:/Users/Harish/Desktop/Dr.Atrey/presentation/Secure Data Deduplication Papers/index/";
	String query = "deduplication";
	
	void storeIndexFiles() throws IOException, ParseException {
		
		System.out.println("Came to Store Index Files");
		
		Path path = FileSystems.getDefault().getPath(indexDir, "");
		FSDirectory dir = FSDirectory.open(path);
		
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		writer = new IndexWriter(dir, config);
		
		indexFileOrDirectory(folderName);		
	}
	
	void indexFileOrDirectory(String folderName) throws IOException, ParseException {

		getFilesInQueue(folderName);
		System.out.println("Index or Directory");
		
//		int originalNumDocs = writer.numDocs();
		Reader fr = null;		
		Iterator<File> iterate = queue.iterator();
		
		while(iterate.hasNext()) {			
			File f = iterate.next();			
			
			// Add Contents to a File
			fr = new FileReader(f);
			
			Document doc = new Document();
			doc.add(new TextField("contents", fr));
			doc.add(new StringField("path", f.getPath(), Field.Store.YES));
			doc.add(new StringField("fileName", f.getName(), Field.Store.YES));
			
			writer.addDocument(doc);
		}		
		fr.close();

		System.out.println("Adding is done");		
		System.out.println("Queue Size : "+queue.size());
		System.out.println("Writer size : "+writer.numDocs());	
		
		writer.close();
		
		searchDirectory();
	}

	void searchDirectory() {
		// Search
		System.out.println("Searching the Inventory");

		Path path = FileSystems.getDefault().getPath(indexDir, "");
		IndexReader reader;
		try {
			reader = DirectoryReader.open(FSDirectory.open(path));
			IndexSearcher searcher = new IndexSearcher(reader);
			
			TopScoreDocCollector collector = TopScoreDocCollector.create(5);
			
			System.out.println("Query : "+query);
			Query q = new QueryParser("contents", analyzer).parse(query);
			searcher.search(q, collector);
			
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
	
			System.out.println("Found Hits : "+hits.length);
			for(int i = 0; i < hits.length; i++) {
				int docId = hits[i].doc;
				Document d = searcher.doc(docId);
				System.out.println(i + ". "+ d.get("path") +" score = "+hits[i].score);
			}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	void getFilesInQueue(String folderName) {
		
		File folder = new File(folderName);
		File[] filesList = folder.listFiles();
		
		System.out.println("Files List : "+filesList.length);
		
		for(int i = 0; i < filesList.length; i++) {
			addFiles(filesList[i]);
		}
		
	}
	
	void addFiles(File file) {
		
		if(!file.exists()) {
			System.out.println("Does not find");
		}
		
		if(file.isDirectory()) {
			for(File f:file.listFiles()) {
				addFiles(f);
			}
		} else {
			String fName = file.getName().toLowerCase();
			
			if(fName.endsWith(".pdf") || fName.endsWith(".txt"))
				queue.add(file);
			
		}
	}
}
