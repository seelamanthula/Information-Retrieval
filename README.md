# Information-Retrieval

# Cross-language information retrieval (CLIR)

Implemented a variant of Information Retrieval system that would allow the user to type in queries in English and search documents 
in a foreign language such as Chinese or Hindi. Furthermore, the top ranked documents returned from search should have their most 
relevant passage extracted and translated into English using Google Translate or similar.

The English query is first translated into the source language (Chinese or Hindi) and then used to search the source document collection. 
Original source documents are indexed in usual way. For this purpose, wrote own search engine (Used Vector Space IR Model) and also used 
the open source systems such as Lucene, etc. The system returns ranked list of documents (actually pointers) from which the most relevant 
passages are selected and translated back into English. For the purpose of this project we will assume that top N documents are processed 
this way, where N is a parameter to be determined.

Following are the list of things, which are performed:
1. A collection of Chinese or Hindi or any foreign documents. (Data source from past TREC evaluations or can be harvested from the 
Internet). 

2. A list of queries, in English, to be run against the data. Made own queries, or selectively pick queries from past TREC evaluations. 
These are available for free from TREC website: http://trec.nist.gov/data/test_coll.html

3. Relevance judgments. This is the trickiest part that requires human judgment against every document returned by every query. 
One way around it could be to use online monolingual search (i.e., Chinese queries against Chinese data, using an online search against 
the same data) and mark top returned documents as relevant. We will then compare ranks with cross language search.

4. An open source IR system (Lucene) or implemented my own different system. The system you select should support indexing and 
retrieval of the source language, including some preprocessing such as segmentation, stemming, etc.
         
          o Basic text search, with multiple retrieval models (Vector Space)
          o Terrier IR platform http://terrier.org/
          o Can be configured with different retrieval models
          o Lemur Project http://www.lemurproject.org/
          o Primarily INQUERY-based Indri system
          o ATIRE system http://atire.org
          o Research system based at Univ. of Otago
          
5. Passage extractor. extracted passages from the returned documents, or indexed the original documents at passage level. The advantage 
of the second option is that you simply retrieve passages and then translate the top ranked ones. A disadvantage is that don’t know 
how to segment for maximum relevance before you run the query (although segmenting as paragraph breaks may be ok.)

6. Some language processing tools such as segmenters, stemmers, stop-word lists, etc. are included to IR system from 
stanford nlp group: https://nlp.stanford.edu/IR-book/html/htmledition/stemming-and-lemmatization-1.html

7. A scoring script for evaluating precision and recall of the output. This is based upon the trec-eval script developed at NIST 
(and available from TREC website).

