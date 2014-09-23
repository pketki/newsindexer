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
public class SpecialCharsFilter extends TokenFilter {

	/**
	 * @param stream
	 */
	public SpecialCharsFilter(TokenStream stream) {
		super(stream);

	}

	@Override
	public boolean increment() throws TokenizerException {
		Token token = null;

		if (this.isChaining())
			token = getStream().getCurrent();
		if (token == null && getStream().hasNext()) {
			token = getStream().next();
		}

		if (token != null) {
			/*
			 * Pattern pattern = Pattern.compile("[^a-zA-Z0-9.\\-]"); Matcher
			 * match = pattern.matcher(token.getTermText()); if (match.find()) {
			 * token.setTermText(token.getTermText().replaceAll(
			 * "[^a-zA-Z0-9.\\-]", "")); }
			 */
			if (token.getTermText().trim().isEmpty()) {
				getStream().remove();
			}
			return true;
		}

		return false;
	}

}
