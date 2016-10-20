package com.pages;

import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;

public class Main {

	public static void main(String[] args) throws IOException, ParseException {
		// TODO Auto-generated method stub

		RetrieveFiles ret = new RetrieveFiles();
		ret.storeIndexFiles();
	}

}
