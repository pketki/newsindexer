/**
 * 
 */
package edu.buffalo.cse.irf14.index;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import edu.buffalo.cse.irf14.IndexHelper;

/**
 * @author nikhillo
 * 
 *         Class that emulates reading data back from a written index
 */
public class IndexReader {
	private final String indexDir;
	private final IndexType type;
	private FileInputStream fileInputStream;
	private ObjectInputStream objectInputStream;
	private Map<Integer, Map<String, Integer>> postingsMap = null;
	private Map<String, Set<String>> indexMap = null;
	private Map<String, Integer> dictionaryMap;
	private Map<String, Integer> docDictionaryMap;

	/**
	 * Default constructor
	 * 
	 * @param indexDir
	 *            : The root directory from which the index is to be read. This
	 *            will be exactly the same directory as passed on IndexWriter.
	 *            In case you make subdirectories etc., you will have to handle
	 *            it accordingly.
	 * @param type
	 *            The {@link IndexType} to read from
	 */
	@SuppressWarnings("unchecked")
	public IndexReader(String indexDir, IndexType type) {
		this.indexDir = indexDir;
		this.type = type;
		this.dictionaryMap = getDictionary(IndexHelper.dictionaryMapping
				.get(this.type));
		this.docDictionaryMap = getDictionary(IndexHelper.docDictionaryName);
		this.indexMap = getIndexMap();
	}

	@SuppressWarnings("unchecked")
	private Map<Integer, Map<String, Integer>> getTermPostings() {
		try {
			File fileDir = new File(this.indexDir);
			File[] fileList = fileDir.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				if (IndexHelper.termPostings.equals(fileList[i].getName())) {
					fileInputStream = new FileInputStream(fileList[i]);
					if (fileInputStream != null) {
						objectInputStream = new ObjectInputStream(
								fileInputStream);
						postingsMap = new HashMap<Integer, Map<String, Integer>>();
						postingsMap = ((Map<Integer, Map<String, Integer>>) objectInputStream
								.readObject());
						break;
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.postingsMap;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Set<String>> getIndexMap() {
		try {
			File fileDir = new File(this.indexDir);
			File[] fileList = fileDir.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				if (IndexHelper.indexMapping.get(this.type).equals(
						fileList[i].getName())) {
					fileInputStream = new FileInputStream(fileList[i]);
					if (fileInputStream != null) {
						objectInputStream = new ObjectInputStream(
								fileInputStream);
						this.indexMap = new HashMap<String, Set<String>>();
						this.indexMap = ((Map<String, Set<String>>) objectInputStream
								.readObject());
						break;
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.indexMap;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Integer> getDictionary(String fileName) {
		Map<String, Integer> dictionaryMap = new HashMap<String, Integer>();
		// String fileName = IndexHelper.dictionaryMapping.get(this.type);

		try {
			File fileDir = new File(this.indexDir);
			File[] fileList = fileDir.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				if (fileName.equals(fileList[i].getName())) {
					fileInputStream = new FileInputStream(fileList[i]);
					if (fileInputStream != null) {
						objectInputStream = new ObjectInputStream(
								fileInputStream);
						dictionaryMap = ((Map<String, Integer>) objectInputStream
								.readObject());
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dictionaryMap;

	}

	/**
	 * Get total number of terms from the "key" dictionary associated with this
	 * index. A postings list is always created against the "key" dictionary
	 * 
	 * @return The total number of terms
	 */
	public int getTotalKeyTerms() {
		if (this.dictionaryMap != null) {
			return dictionaryMap.size();
		}
		return 0;
	}

	/**
	 * Get total number of terms from the "value" dictionary associated with
	 * this index. A postings list is always created with the "value" dictionary
	 * 
	 * @return The total number of terms
	 */
	public int getTotalValueTerms() {
		return this.docDictionaryMap.size();
	}

	/**
	 * Method to get the postings for a given term. You can assume that the raw
	 * string that is used to query would be passed through the same Analyzer as
	 * the original field would have been.
	 * 
	 * @param term
	 *            : The "analyzed" term to get postings for
	 * @return A Map containing the corresponding fileid as the key and the
	 *         number of occurrences as values if the given term was found, null
	 *         otherwise.
	 */
	public Map<String, Integer> getPostings(String term) {
		Map<String, Integer> returnMap = null;
		if (this.type == IndexType.TERM) {
			if (this.postingsMap == null)
				this.postingsMap = getTermPostings();
			return this.postingsMap.get(term);
		} else {
			Set<String> docSet = this.indexMap.get(term);
			if (docSet != null) {
				returnMap = new HashMap<String, Integer>();
				for (String doc : docSet) {
					returnMap.put(doc, 1);
				}
			}
		}
		return returnMap;
	}

	/**
	 * Method to get the top k terms from the index in terms of the total number
	 * of occurrences.
	 * 
	 * @param k
	 *            : The number of terms to fetch
	 * @return : An ordered list of results. Must be <=k fr valid k values null
	 *         for invalid k values
	 */
	public List<String> getTopK(int k) {

		int count = 0;
		List<String> topKlist = new ArrayList<String>();

		if (k > 0 && this.indexMap != null) {
			Map<String, Set<String>> sortedMap = new LinkedHashMap<String, Set<String>>();
			sortedMap = IndexHelper.sortByValue(indexMap);

			for (Entry<String, Set<String>> entry : sortedMap.entrySet()) {
				count++;
				topKlist.add(entry.getKey());
				if (count == k) {
					break;
				}
			}
			return topKlist;
		}
		return null;
	}

	/**
	 * Method to implement a simple boolean AND query on the given index
	 * 
	 * @param terms
	 *            The ordered set of terms to AND, similar to getPostings() the
	 *            terms would be passed through the necessary Analyzer.
	 * @return A Map (if all terms are found) containing FileId as the key and
	 *         number of occurrences as the value, the number of occurrences
	 *         would be the sum of occurrences for each participating term.
	 *         return null if the given term list returns no results BONUS ONLY
	 */
	public Map<String, Integer> query(String... terms) {
		// TODO : BONUS ONLY
		return null;
	}
}
