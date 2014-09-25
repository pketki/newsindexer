/**
 * 
 */
package edu.buffalo.cse.irf14.index;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import edu.buffalo.cse.irf14.analysis.Analyzer;
import edu.buffalo.cse.irf14.analysis.AnalyzerFactory;
import edu.buffalo.cse.irf14.analysis.Token;
import edu.buffalo.cse.irf14.analysis.TokenStream;
import edu.buffalo.cse.irf14.analysis.Tokenizer;
import edu.buffalo.cse.irf14.analysis.TokenizerException;
import edu.buffalo.cse.irf14.document.Document;
import edu.buffalo.cse.irf14.document.FieldNames;

/**
 * @author nikhillo Class responsible for writing indexes to disk
 */
public class IndexWriter {
	private final String indexDir;
	private final Map<String, Map<String, Integer>> termPostings = new HashMap<String, Map<String, Integer>>();

	/**
	 * Default constructor
	 * 
	 * @param indexDir
	 *            : The root directory to be sued for indexing
	 */
	public IndexWriter(String indexDir) {
		this.indexDir = indexDir;
	}

	/**
	 * Method to add the given Document to the index This method should take
	 * care of reading the filed values, passing them through corresponding
	 * analyzers and then indexing the results for each indexable field within
	 * the document.
	 * 
	 * @param d
	 *            : The Document to be added
	 * @throws IndexerException
	 *             : In case any error occurs
	 */
	public void addDocument(Document d) throws IndexerException {
		Tokenizer tokenizer = new Tokenizer();
		try {
			if (d.getField(FieldNames.AUTHOR) != null) {
				Map<String, Map<String, Integer>> postings = new HashMap<String, Map<String, Integer>>();

				TokenStream ts = tokenizer.consume(d
						.getField(FieldNames.AUTHOR)[0]);
				System.out.println(ts);
				AnalyzerFactory af = AnalyzerFactory.getInstance();
				Analyzer an = af.getAnalyzerForField(FieldNames.AUTHOR, ts);
				while (an.increment())
					;

				ts.reset();
				System.out.println(ts);
				Map<String, Integer> documentList;
				while (ts.hasNext()) {
					Token token = ts.next();
					String term = token.getTermText();
					Integer count = 0;
					if (!postings.containsKey(term)) {
						documentList = new HashMap<String, Integer>();
						postings.put(term, documentList);
						count = 1;
						documentList.put(d.getField(FieldNames.FILEID)[0],
								count);
					} else {
						documentList = postings.get(term);
						count = documentList
								.get(d.getField(FieldNames.FILEID)[0]);
						count++;
					}
				}
				FileOutputStream fos = new FileOutputStream(indexDir
						+ "\\authorPostings" + ".ser");
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(postings);
				oos.close();
			}

			if (d.getField(FieldNames.TITLE) != null) {
				TokenStream ts = tokenizer
						.consume(d.getField(FieldNames.TITLE)[0]);
				System.out.println(ts);
				AnalyzerFactory af = AnalyzerFactory.getInstance();
				Analyzer an = af.getAnalyzerForField(FieldNames.TITLE, ts);
				while (an.increment())
					;

				System.out.println(ts);
			}
			if (d.getField(FieldNames.PLACE) != null) {
				TokenStream ts = tokenizer
						.consume(d.getField(FieldNames.PLACE)[0]);
				System.out.println(ts);
				AnalyzerFactory af = AnalyzerFactory.getInstance();
				Analyzer an = af.getAnalyzerForField(FieldNames.PLACE, ts);
				while (an.increment())
					;

				System.out.println(ts);
			}
			if (d.getField(FieldNames.CONTENT) != null) {
				TokenStream ts = tokenizer.consume(d
						.getField(FieldNames.CONTENT)[0]);
				System.out.println(ts);
				AnalyzerFactory af = AnalyzerFactory.getInstance();
				Analyzer an = af.getAnalyzerForField(FieldNames.CONTENT, ts);
				while (an.increment())
					;

				System.out.println(ts);
				Map<String, Integer> documentList;
				while (ts.hasNext()) {
					Token token = ts.next();
					String term = token.getTermText();
					Integer count = 0;
					if (!termPostings.containsKey(term)) {
						documentList = new HashMap<String, Integer>();
						termPostings.put(term, documentList);
						count = 1;
						documentList.put(d.getField(FieldNames.FILEID)[0],
								count);
					} else {
						documentList = termPostings.get(term);
						count = documentList
								.get(d.getField(FieldNames.FILEID)[0]);
						count++;
					}
				}
			}

		} catch (TokenizerException te) {
			te.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Method that indicates that all open resources must be closed and cleaned
	 * and that the entire indexing operation has been completed.
	 * 
	 * @throws IndexerException
	 *             : In case any error occurs
	 * @throws IOException
	 */
	public void close() throws IndexerException {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(indexDir + "\\termPostings" + ".ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(termPostings);
			oos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
