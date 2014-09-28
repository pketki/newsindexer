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

	private final Map<Integer, Map<Integer, Integer>> termPostings;

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

		termPostings = new HashMap<Integer, Map<Integer, Integer>>();
	}

	private int addtoDictionaryAndIndex(Map<String, Integer> dictionary,
			Integer dictionaryIndex, Map<Integer, Set<Integer>> index,
			String term, String fileId) {
		Set<Integer> postings;
		int idx = dictionaryIndex;
		if (!dictionary.containsKey(term)) {
			idx++;
			dictionary.put(term, idx);
			postings = new HashSet<Integer>();
		} else {
			postings = index.get(dictionaryIndex);
		}
		postings.add(docDictionary.get(fileId));
		index.put(dictionaryIndex, postings);
		return idx;
	}

	private void addToTermMapping(String term, String fileId) {
		Map<Integer, Integer> documentList;
		Integer count = 1;
		Integer termId = termDictionary.get(term);
		Integer docId = docDictionary.get(fileId);
		if (!termPostings.containsKey(termId)) {
			documentList = new HashMap<Integer, Integer>();
		} else {
			documentList = termPostings.get(termId);
			if (documentList.containsKey(docId)) {
				count = documentList.get(docId);
				count++;
			}
		}
		documentList.put(docId, count);
		termPostings.put(termId, documentList);
	}

	private int generateHash(String term) {
		if (term != null && !term.isEmpty()) {
			int hash = 7;
			for (int i = 0; i < term.length(); i++) {
				hash = hash * 31 + term.charAt(i);
			}
			return hash;
		}
		return 0;
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

		if (!docDictionary.containsKey(fileId)) {
			docDictionary.put(fileId, generateHash(fileId));
		}
		String[] authorInputs = d.getField(FieldNames.AUTHOR);
		AnalyzerFactory af = AnalyzerFactory.getInstance();
		Analyzer an;

		try {
			String category = d.getField(FieldNames.CATEGORY)[0];
			TokenStream stream = tokenizer.consume(category);
			an = af.getAnalyzerForField(FieldNames.CATEGORY, stream);
			addtoDictionaryAndIndex(categoryDictionary, generateHash(category),
					categoryIndex, category, fileId);

			if (authorInputs != null) {
				TokenStream ts = null;
				for (String input : authorInputs)
					ts = tokenizer.consume(input);
				System.out.println(ts);

				an = af.getAnalyzerForField(FieldNames.AUTHOR, ts);
				while (an.increment())
					;

				System.out.println(ts);

				ts.reset();
				Token token;
				String term;
				while (ts.hasNext()) {
					token = ts.next();
					term = token.getTermText();
					addtoDictionaryAndIndex(authorDictionary,
							generateHash(term), authorIndex, term, fileId);
				}
			}

			if (d.getField(FieldNames.TITLE) != null) {
				TokenStream ts = tokenizer
						.consume(d.getField(FieldNames.TITLE)[0]);
				System.out.println(ts);
				an = af.getAnalyzerForField(FieldNames.TITLE, ts);
				while (an.increment())
					;
				System.out.println(ts);

				ts.reset();
				Token token;
				String term;
				while (ts.hasNext()) {
					token = ts.next();
					term = token.getTermText();
					addtoDictionaryAndIndex(termDictionary, generateHash(term),
							termIndex, term, fileId);

					addToTermMapping(term, fileId);
				}
			}
			if (d.getField(FieldNames.PLACE) != null) {
				TokenStream ts = tokenizer
						.consume(d.getField(FieldNames.PLACE)[0]);
				System.out.println(ts);
				an = af.getAnalyzerForField(FieldNames.PLACE, ts);
				while (an.increment())
					;
				System.out.println(ts);

				ts.reset();
				Token token;
				String term;
				while (ts.hasNext()) {
					token = ts.next();
					term = token.getTermText();

					addtoDictionaryAndIndex(placeDictionary,
							generateHash(term), placeIndex, term, fileId);
				}
			}
			if (d.getField(FieldNames.CONTENT) != null) {
				TokenStream ts = tokenizer.consume(d
						.getField(FieldNames.CONTENT)[0]);
				System.out.println(ts);
				an = af.getAnalyzerForField(FieldNames.CONTENT, ts);
				while (an.increment())
					;

				System.out.println("Final: " + ts);
				ts.reset();
				while (ts.hasNext()) {
					Token token = ts.next();
					String term = token.getTermText();
					addtoDictionaryAndIndex(termDictionary, generateHash(term),
							termIndex, term, fileId);

					addToTermMapping(term, fileId);
				}
			}

		} catch (TokenizerException te) {
			te.printStackTrace();
		}
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
