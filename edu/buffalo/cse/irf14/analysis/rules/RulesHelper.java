/**
 * 
 */
package edu.buffalo.cse.irf14.analysis.rules;

import java.util.HashMap;

/**
 * @author ketkiram
 * 
 *         Helper class to store static data used by different filters based on
 *         different rules applied.
 */
public final class RulesHelper {

	public static HashMap<String, String> commonContractionsMap = new HashMap<String, String>();

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
}
