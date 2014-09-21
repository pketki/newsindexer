/**
 * 
 */
package edu.buffalo.cse.irf14.analysis.rules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.buffalo.cse.irf14.analysis.Token;
import edu.buffalo.cse.irf14.analysis.TokenFilter;
import edu.buffalo.cse.irf14.analysis.TokenStream;
import edu.buffalo.cse.irf14.analysis.TokenizerException;

/**
 * @author jlimaye
 * 
 */
public class SpecialCharsFilter extends TokenFilter {

	/**
	 * @param stream
	 */
	public SpecialCharsFilter(TokenStream stream) {
		super(stream);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.buffalo.cse.irf14.analysis.Analyzer#increment()
	 */
	@Override
	public boolean increment() throws TokenizerException {
		Token token = null;

		if (this.isChaining())
			token = getStream().getCurrent();
		if (token == null && getStream().hasNext()) {
			token = getStream().next();
		}

		if (token != null) {
			Pattern pattern = Pattern.compile("[^a-zA-Z0-9.-]");
			Matcher match = pattern.matcher(token.getTermText());
			if (match.find()) {
				token.setTermText(token.getTermText().replaceAll(
						"[^a-zA-Z0-9.-]", ""));
			}
			if (token.getTermText().trim().isEmpty()) {
				getStream().remove();
			}
			return true;
		}

		return false;
	}

}
