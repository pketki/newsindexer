/**
 * 
 */
package edu.buffalo.cse.irf14.analysis.rules;

import edu.buffalo.cse.irf14.analysis.Token;
import edu.buffalo.cse.irf14.analysis.TokenFilter;
import edu.buffalo.cse.irf14.analysis.TokenStream;
import edu.buffalo.cse.irf14.analysis.TokenizerException;

/**
 * @author ketkiram
 * 
 */
public class SymbolFilter extends TokenFilter {

	/**
	 * @param stream
	 */

	public SymbolFilter(TokenStream stream) {
		super(stream);
	}

	/**
	 * Method to handle apostrophes in the token string as per following rules:
	 * 1. Any possessive apostrophes should be removed (‘s s’ or just ‘ at the
	 * end of a word). 2. Common contractions should be replaced with expanded
	 * forms but treated as one token. (e.g. should’ve => should have). 3. All
	 * other apostrophes should be removed.
	 * 
	 * @param input
	 *            : The token string
	 * @return The formatted string with no apostrophe
	 */
	private String filterApostrophe(String input) {
		String result = RulesHelper.commonContractionsMap.get(input);

		// if it is not a common contraction simply remove the apostrophe
		if (result == null) {
			result = input.replaceAll("'", "");
		}
		return result;
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
	private String fiterHyphen(String input) {
		String result = null;
		String[] parts = input.split("-");

		for (String part : parts) {
			if (part.matches("^[0-9]+$")) {
				result = input;
				break;
			}
			result = input.replaceAll("-", " ");
		}
		result = result.trim();
		return result;
	}

	@Override
	public boolean increment() throws TokenizerException {
		Token token = null;
		String text = null;

		if (getStream().hasNext()) {
			token = getStream().next();
			if (token != null) {
				text = token.getTermText();

				for (char symbol : RulesHelper.endOfLineSymbols) {
					if (text.charAt(text.length() - 1) == symbol) {
						text.replace("" + symbol, "");
					}
				}

				if (text.contains("'")) {
					text = this.filterApostrophe(text);
				}

				if (text.contains("-")) {
					text = this.fiterHyphen(text);
				}
				token.setTermText(text);
				return true;
			}
		}
		return false;
	}

}
