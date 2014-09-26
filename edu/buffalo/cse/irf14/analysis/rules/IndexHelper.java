/**
 * 
 */
package edu.buffalo.cse.irf14.analysis.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Pattern;

import edu.buffalo.cse.irf14.index.IndexType;

/**
 * @author ketkiram
 * 
 *         Helper class to store static data used by different filters based on
 *         different rules applied.
 */
public final class IndexHelper {

	public static Pattern endOfLineSymbols = Pattern.compile("\\w[.!\\?]+$");

	public static Pattern numeric = Pattern.compile("\\d*[\\/.,\\-]?\\d*%*$");
	public static Pattern camelCase = Pattern
			.compile("([a-z]*[A-Z]+[a-z]+)+|([a-z]+[A-Z]+[a-z]*)+");

	// public static Pattern numeric = Pattern.compile("[\\d*[\\/.,\\-]?]*%*$");
	public static Map<String, String> commonContractionsMap = new TreeMap<String, String>(
			String.CASE_INSENSITIVE_ORDER);
	public static List<String> stopWordsList = new ArrayList<String>();

	public static Map<IndexType, String> postingsMapping = new HashMap<IndexType, String>();

	static {
		postingsMapping.put(IndexType.AUTHOR, "authorPostings.ser");
		postingsMapping.put(IndexType.CATEGORY, "categoryPostings.ser");
		postingsMapping.put(IndexType.PLACE, "placePostings.ser");
		postingsMapping.put(IndexType.TERM, "termPostings.ser");
	}

	static {
		commonContractionsMap.put("ain't", "am not");
		commonContractionsMap.put("aren't", "are not");
		commonContractionsMap.put("can't", "cannot");
		commonContractionsMap.put("could've", "could have");
		commonContractionsMap.put("couldn't", "could not");
		commonContractionsMap.put("didn't", "did not");
		commonContractionsMap.put("doesn't", "does not");
		commonContractionsMap.put("don't", "do not");
		commonContractionsMap.put("hadn't", "had not");
		commonContractionsMap.put("hasn't", "has not");
		commonContractionsMap.put("haven't", "have not");
		commonContractionsMap.put("he'd", "he would");
		commonContractionsMap.put("he's", "he is");
		commonContractionsMap.put("he'll", "he will");
		commonContractionsMap.put("how'd", "how would");
		commonContractionsMap.put("how'll", "how will");
		commonContractionsMap.put("how's", "how is");
		commonContractionsMap.put("I'd", "I would");
		commonContractionsMap.put("I'll", "I will");
		commonContractionsMap.put("I'm", "I am");
		commonContractionsMap.put("I've", "I have");
		commonContractionsMap.put("isn't", "is not");
		commonContractionsMap.put("it'd", "it would");
		commonContractionsMap.put("it'll", "it will");
		commonContractionsMap.put("it's", "it is");
		commonContractionsMap.put("let's", "let us");
		commonContractionsMap.put("ma'am", "madam");
		commonContractionsMap.put("mightn't", "might not");
		commonContractionsMap.put("might've", "might have");
		commonContractionsMap.put("mustn't", "must not");
		commonContractionsMap.put("must've", "must have");
		commonContractionsMap.put("needn't", "need not");
		commonContractionsMap.put("not've", "not have");
		commonContractionsMap.put("o'clock", "of the clock");
		commonContractionsMap.put("'em", "them");
		commonContractionsMap.put("shan't", "shall not");
		commonContractionsMap.put("she'd", "she would");
		commonContractionsMap.put("she'll", "she will");
		commonContractionsMap.put("she's", "she is");
		commonContractionsMap.put("should've", "should have");
		commonContractionsMap.put("shouldn't", "should not");
		commonContractionsMap.put("that's", "that is");
		commonContractionsMap.put("there'd", "there would");
		commonContractionsMap.put("there's", "there is");
		commonContractionsMap.put("they'd", "they would");
		commonContractionsMap.put("they'll", "they will");
		commonContractionsMap.put("they're", "they are");
		commonContractionsMap.put("they've", "they have");
		commonContractionsMap.put("wasn't", "was not");
		commonContractionsMap.put("we'd", "we would");
		commonContractionsMap.put("we'll", "we will");
		commonContractionsMap.put("we're", "we are");
		commonContractionsMap.put("we've", "we have");
		commonContractionsMap.put("weren't", "were not");
		commonContractionsMap.put("what'll", "what will");
		commonContractionsMap.put("what're", "what are");
		commonContractionsMap.put("what's", "what is");
		commonContractionsMap.put("what've", "what have");
		commonContractionsMap.put("when's", "whe is");
		commonContractionsMap.put("where'd", "where did");
		commonContractionsMap.put("where's", "where is");
		commonContractionsMap.put("where've", "where have");
		commonContractionsMap.put("who'd", "who would");
		commonContractionsMap.put("who'll", "who will");
		commonContractionsMap.put("who're", "who are");
		commonContractionsMap.put("who's", "who is");
		commonContractionsMap.put("who've", "who have");
		commonContractionsMap.put("why'll", "why will");
		commonContractionsMap.put("why're", "why are");
		commonContractionsMap.put("why's", "why is");
		commonContractionsMap.put("won't", "will not");
		commonContractionsMap.put("would've", "would have");
		commonContractionsMap.put("wouldn't", "would not");
		commonContractionsMap.put("y'all", "you all");
		commonContractionsMap.put("you'd", "you would");
		commonContractionsMap.put("you'll", "you will");
		commonContractionsMap.put("you're", "you are");
		commonContractionsMap.put("you've", "you have");
	}

	static {
		stopWordsList.add("a");
		stopWordsList.add("able");
		stopWordsList.add("about");
		stopWordsList.add("across");
		stopWordsList.add("after");
		stopWordsList.add("all");
		stopWordsList.add("almost");
		stopWordsList.add("also");
		stopWordsList.add("am");
		stopWordsList.add("among");
		stopWordsList.add("an");
		stopWordsList.add("and");
		stopWordsList.add("any");
		stopWordsList.add("are");
		stopWordsList.add("as");
		stopWordsList.add("at");
		stopWordsList.add("be");
		stopWordsList.add("because");
		stopWordsList.add("been");
		stopWordsList.add("but");
		stopWordsList.add("by");
		stopWordsList.add("can");
		stopWordsList.add("cannot");
		stopWordsList.add("could");
		stopWordsList.add("dear");
		stopWordsList.add("did");
		stopWordsList.add("do");
		stopWordsList.add("does");
		stopWordsList.add("either");
		stopWordsList.add("else");
		stopWordsList.add("ever");
		stopWordsList.add("every");
		stopWordsList.add("for");
		stopWordsList.add("from");
		stopWordsList.add("get");
		stopWordsList.add("got");
		stopWordsList.add("had");
		stopWordsList.add("has");
		stopWordsList.add("have");
		stopWordsList.add("he");
		stopWordsList.add("her");
		stopWordsList.add("hers");
		stopWordsList.add("him");
		stopWordsList.add("his");
		stopWordsList.add("how");
		stopWordsList.add("however");
		stopWordsList.add("i");
		stopWordsList.add("if");
		stopWordsList.add("in");
		stopWordsList.add("into");
		stopWordsList.add("is");
		stopWordsList.add("it");
		stopWordsList.add("its");
		stopWordsList.add("just");
		stopWordsList.add("least");
		stopWordsList.add("let");
		stopWordsList.add("like");
		stopWordsList.add("likely");
		stopWordsList.add("may");
		stopWordsList.add("me");
		stopWordsList.add("might");
		stopWordsList.add("most");
		stopWordsList.add("must");
		stopWordsList.add("my");
		stopWordsList.add("neither");
		stopWordsList.add("no");
		stopWordsList.add("nor");
		stopWordsList.add("not");
		stopWordsList.add("of");
		stopWordsList.add("off");
		stopWordsList.add("often");
		stopWordsList.add("on");
		stopWordsList.add("only");
		stopWordsList.add("or");
		stopWordsList.add("oth");
		stopWordsList.add("er");
		stopWordsList.add("our");
		stopWordsList.add("own");
		stopWordsList.add("rather");
		stopWordsList.add("said");
		stopWordsList.add("say");
		stopWordsList.add("says");
		stopWordsList.add("she");
		stopWordsList.add("should");
		stopWordsList.add("since");
		stopWordsList.add("so");
		stopWordsList.add("some");
		stopWordsList.add("than");
		stopWordsList.add("that");
		stopWordsList.add("the");
		stopWordsList.add("their");
		stopWordsList.add("them");
		stopWordsList.add("then");
		stopWordsList.add("there");
		stopWordsList.add("these");
		stopWordsList.add("they");
		stopWordsList.add("this");
		stopWordsList.add("tis");
		stopWordsList.add("to");
		stopWordsList.add("too");
		stopWordsList.add("twas");
		stopWordsList.add("us");
		stopWordsList.add("wants");
		stopWordsList.add("was");
		stopWordsList.add("we");
		stopWordsList.add("were");
		stopWordsList.add("what");
		stopWordsList.add("when");
		stopWordsList.add("where");
		stopWordsList.add("which");
		stopWordsList.add("while");
		stopWordsList.add("who");
		stopWordsList.add("whom");
		stopWordsList.add("why");
		stopWordsList.add("will");
		stopWordsList.add("with");
		stopWordsList.add("would");
		stopWordsList.add("yet");
		stopWordsList.add("you");
		stopWordsList.add("your");
	}

	/**
	 * Method to handle hyphens in the token string as per following rules: 1.
	 * If a hyphen occurs within a alphanumeric token it should be retained
	 * (e.g. B-52, at least one of the two constituents must have a number). 2.
	 * If both are alphabetic, it should be replaced with a whitespace and
	 * retained as a single token (week-day => week day). 3. Any other hyphens
	 * padded by spaces on either or both sides should be removed.
	 * 
	 * @param input
	 *            : The token string
	 * @return The formatted string
	 */
	public static String filterHyphen(String input) {
		String result = "";
		if (input.trim().length() > 1) {
			String[] parts = input.split("-");

			for (String part : parts) {
				if (part.matches("\\w*[0-9]+$")) {
					result = input;
					break;
				}
				result = input.replaceAll("-", " ");
			}
			result = result.trim();
		}
		return result;
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(
			Map<K, V> map) {
		LinkedList<Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(
				map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
}
