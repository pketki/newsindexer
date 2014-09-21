/**
 * 
 */
package edu.buffalo.cse.irf14.analysis.rules;

import edu.buffalo.cse.irf14.analysis.Token;
import edu.buffalo.cse.irf14.analysis.TokenFilter;
import edu.buffalo.cse.irf14.analysis.TokenStream;
import edu.buffalo.cse.irf14.analysis.TokenizerException;

/**
 * @author jlimaye
 * 
 */
public class NumberFilter extends TokenFilter {

	/**
	 * @param stream
	 */
	public NumberFilter(TokenStream stream) {
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
			token.setTermText(token.getTermText().replaceAll(
					"(\\s*\\d+),(\\d+)", ""));
			token.setTermText(token.getTermText().replaceAll(
					"(\\d.*?\\.\\d.*?)(%)", ""));
			token.setTermText(token.getTermText().replaceAll(
					"(\\d+)(\\/)(\\d+)", ""));
			token.setTermText(token.getTermText().replaceAll("\\s*\\d+", ""));
			if (token.getTermText().trim().isEmpty()) {
				getStream().remove();
			}
			return true;
		}

		return false;
	}

}
