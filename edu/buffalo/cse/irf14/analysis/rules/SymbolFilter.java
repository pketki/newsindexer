/**
 * 
 */
package edu.buffalo.cse.irf14.analysis.rules;

import java.util.regex.Matcher;

import edu.buffalo.cse.irf14.IndexHelper;
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
	 * 1. Any possessive apostrophes should be removed (�s s� or just � at
	 * the end of a word). 2. Common contractions should be replaced with
	 * expanded forms but treated as one token. (e.g. should�ve => should
	 * have). 3. All other apostrophes should be removed.
	 * 
	 * @param input
	 *            : The token string
	 * @return The formatted string with no apostrophe
	 */
	private String filterApostrophe(String input) {
		String result = IndexHelper.commonContractionsMap.get(input);

		if (result != null) {
			// check for case as map only has lowercase values
			if (input.charAt(0) != '\'' && input.charAt(0) != result.charAt(0)) {
				result = result.replaceFirst(String.valueOf(result.charAt(0)),
						String.valueOf(input.charAt(0)));
			}

		} else {
			// if it is not a common contraction remove apostrophe
			// as per above rules
			input = input.replaceAll("'s", "");
			input = input.replaceAll("'", "");
			result = input;
		}
		return result;
	}

	@Override
	public boolean increment() throws TokenizerException {
		Token token = null;
		String text = null;

		if (this.isChaining())
			token = getStream().getCurrent();
		if (token == null && getStream().hasNext()) {
			token = getStream().next();
		}

		if (token != null) {
			text = token.getTermText();

			String punctuationRegex = "[.!\\?]+";
			if (text.matches(punctuationRegex))
				text = "";
			Matcher symbolMatcher = IndexHelper.endOfLineSymbols.matcher(text);
			while (symbolMatcher.find()) {
				text = text.replace(symbolMatcher.group(0), symbolMatcher
						.group(0).replaceAll(punctuationRegex, ""));
			}

			if (text.contains("'")) {
				text = this.filterApostrophe(text);
			}

			if (text.contains("-")) {
				text = IndexHelper.filterHyphen(text);
			}

			if (text.isEmpty())
				getStream().remove();
			else
				token.setTermText(text);
			return true;
		}
		return false;
	}
}
