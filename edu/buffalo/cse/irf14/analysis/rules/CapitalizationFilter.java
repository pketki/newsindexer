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
public class CapitalizationFilter extends TokenFilter {

	/**
	 * @param stream
	 */
	public CapitalizationFilter(TokenStream stream) {
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
			Pattern pattern = Pattern.compile("[a-z]+[A-Z]+");
			Matcher match = pattern.matcher(token.getTermText());
			if (!match.find()) {
				if (!token.getTermText().equals(
						token.getTermText().toUpperCase())) {
					token.setTermText(token.getTermText().toLowerCase());
				} else if (!Character
						.isLowerCase(token.getTermText().charAt(0))) {
					token.setTermText(token.getTermText().toLowerCase());
				}
			}
			return true;
		}
		getStream().reset();
		return false;
	}

}
