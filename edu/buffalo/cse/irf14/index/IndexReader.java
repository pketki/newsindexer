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
import java.util.HashSet;
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
	private Map<String, Map<String, Integer>> postingsMap;

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
		try {
			// File fileDir = new File(indexDir);
			File fileDir = new File(indexDir);
			File[] fileList = fileDir.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				// if (fileList[i].getName().equals("termPostings.ser")) {
				if (IndexHelper.postingsMapping.get(IndexType.TERM).equals(
						fileList[i].getName())) {
					fileInputStream = new FileInputStream(fileList[i]);
					if (fileInputStream != null) {
						objectInputStream = new ObjectInputStream(
								fileInputStream);
						postingsMap = new HashMap<String, Map<String, Integer>>();
						postingsMap = ((Map<String, Map<String, Integer>>) objectInputStream
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
	}

	private Map<String, Map<String, Integer>> getTermPostings() {
		return this.postingsMap;
	}

	/**
	 * Get total number of terms from the "key" dictionary associated with this
	 * index. A postings list is always created against the "key" dictionary
	 * 
	 * @return The total number of terms
	 */
	public int getTotalKeyTerms() {
		return getTermPostings().size();
	}

	/**
	 * Get total number of terms from the "value" dictionary associated with
	 * this index. A postings list is always created with the "value" dictionary
	 * 
	 * @return The total number of terms
	 */
	public int getTotalValueTerms() {
		Set<String> documentSet = new HashSet<String>();
		for (Entry<String, Map<String, Integer>> entry : getTermPostings()
				.entrySet()) {
			Map<String, Integer> innerMap = entry.getValue();
			documentSet.addAll(innerMap.keySet());
		}
		return documentSet.size();
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
		return getTermPostings().get(term);
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

		List<String> topKlist = new ArrayList<String>();
		Map<String, Integer> sortMap = new HashMap<String, Integer>();
		for (Entry<String, Map<String, Integer>> entry : getTermPostings()
				.entrySet()) {
			sortMap.put(entry.getKey(), entry.getValue().size());
		}
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		sortedMap = IndexHelper.sortByValue(sortMap);

		for (Entry<String, Integer> entry : sortedMap.entrySet()) {
			topKlist.add(entry.getKey().toString());
		}

		if (k > 0 && !topKlist.isEmpty()) {
			if (topKlist.size() > k) {
				while (topKlist.size() > k) {
					topKlist.remove(topKlist.size() - 1);
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
