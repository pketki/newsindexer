/**
 * 
 */
package edu.buffalo.cse.irf14.index;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import edu.buffalo.cse.irf14.IndexHelper;
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
	private final Tokenizer tokenizer;

	private final Map<Integer, Set<Integer>> termIndex;
	private final Map<Integer, Set<Integer>> authorIndex;
	private final Map<Integer, Set<Integer>> categoryIndex;
	private final Map<Integer, Set<Integer>> placeIndex;

	private final Map<String, Integer> docDictionary;
	private final Map<String, Integer> termDictionary;
	private final Map<String, Integer> authorDictionary;
	private final Map<String, Integer> categoryDictionary;
	private final Map<String, Integer> placeDictionary;

	private final Map<String, Map<String, Integer>> termPostings;

	/**
	 * Default constructor
	 * 
	 * @param indexDir
	 *            : The root directory to be sued for indexing
	 */
	public IndexWriter(String indexDir) {
		this.indexDir = indexDir;
		tokenizer = new Tokenizer();

		termIndex = new HashMap<Integer, Set<Integer>>();
		authorIndex = new TreeMap<Integer, Set<Integer>>();
		categoryIndex = new TreeMap<Integer, Set<Integer>>();
		placeIndex = new TreeMap<Integer, Set<Integer>>();

		docDictionary = new HashMap<String, Integer>();
		termDictionary = new HashMap<String, Integer>();
		authorDictionary = new TreeMap<String, Integer>();
		categoryDictionary = new TreeMap<String, Integer>();
		placeDictionary = new TreeMap<String, Integer>();

		termPostings = new HashMap<String, Map<String, Integer>>();
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
		String fileId = d.getField(FieldNames.FILEID)[0];

		if (!docDictionary.containsKey(fileId))
			docDictionary.put(fileId, ++IndexHelper.DOCUMENTINDEX);

		String[] authorInputs = d.getField(FieldNames.AUTHOR);
		AnalyzerFactory af = AnalyzerFactory.getInstance();
		Analyzer an;

		try {
			if (authorInputs != null) {
				TokenStream ts = null;
				for (String input : authorInputs)
					ts = tokenizer.consume(input);
				System.out.println(ts);

				an = af.getAnalyzerForField(FieldNames.AUTHOR, ts);
				while (an.increment())
					;

				ts.reset();
				Token token;
				String term;
				Set<Integer> authorDocSet;
				while (ts.hasNext()) {
					token = ts.next();
					term = token.getTermText();

					if (!authorDictionary.containsKey(term)) {
						authorDictionary.put(term, ++IndexHelper.AUTHORINDEX);
						authorDocSet = new HashSet<Integer>();
					} else {
						authorDocSet = authorIndex.get(IndexHelper.AUTHORINDEX);
					}
					authorDocSet.add(docDictionary.get(fileId));
					authorIndex.put(IndexHelper.AUTHORINDEX, authorDocSet);
				}
			}

			// if (d.getField(FieldNames.TITLE) != null) {
			// TokenStream ts = tokenizer
			// .consume(d.getField(FieldNames.TITLE)[0]);
			// System.out.println(ts);
			// an = af.getAnalyzerForField(FieldNames.TITLE, ts);
			// while (an.increment())
			// ;
			//
			// System.out.println(ts);
			// }
			if (d.getField(FieldNames.PLACE) != null) {
				TokenStream ts = tokenizer
						.consume(d.getField(FieldNames.PLACE)[0]);
				System.out.println(ts);
				an = af.getAnalyzerForField(FieldNames.PLACE, ts);
				while (an.increment())
					;
				ts.reset();
				Token token;
				String term;
				Set<Integer> placeDocSet;
				while (ts.hasNext()) {
					token = ts.next();
					term = token.getTermText();

					if (!placeDictionary.containsKey(term)) {
						placeDictionary.put(term, ++IndexHelper.PLACEINDEX);
						placeDocSet = new HashSet<Integer>();
					} else {
						placeDocSet = authorIndex.get(IndexHelper.PLACEINDEX);
					}
					placeDocSet.add(docDictionary.get(fileId));
					authorIndex.put(IndexHelper.PLACEINDEX, placeDocSet);
				}
			}
			// if (d.getField(FieldNames.CONTENT) != null) {
			// TokenStream ts = tokenizer.consume(d
			// .getField(FieldNames.CONTENT)[0]);
			// System.out.println(ts);
			// an = af.getAnalyzerForField(FieldNames.CONTENT, ts);
			// while (an.increment())
			// ;

			// System.out.println("Final: " + ts);
			// ts.reset();
			// Map<String, Integer> documentList;
			// while (ts.hasNext()) {
			// Token token = ts.next();
			// String term = token.getTermText();
			// System.out.println(term);
			// Integer count = 1;
			// if (!termPostings.containsKey(term)) {
			// documentList = new HashMap<String, Integer>();
			// } else {
			// documentList = termPostings.get(term);
			// if (documentList.containsKey(fileId)) {
			// count = documentList.get(fileId);
			// count++;
			// }
			// }
			// documentList.put(fileId, count);
			// termPostings.put(term, documentList);
			// }
			// System.out.println(termPostings.get("bid"));
			// }

		} catch (TokenizerException te) {
			te.printStackTrace();
		}
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	private boolean writeToDisk(Map dictionary, String fileName) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(indexDir + "\\" + fileName + ".ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(dictionary);
			oos.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
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
		if (!(writeToDisk(docDictionary, "Document_Dictionary")
				&& writeToDisk(authorDictionary, "Author_Dictionary")
				&& writeToDisk(authorIndex, "Author_Index")
				&& writeToDisk(placeDictionary, "Place_Dictionary") && writeToDisk(
					placeIndex, "Place_Index")))
			throw new IndexerException();
	}
}
