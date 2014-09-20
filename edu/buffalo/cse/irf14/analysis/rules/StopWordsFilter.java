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
public class StopWordsFilter extends TokenFilter {

	/**
	 * @param stream
	 */
	public StopWordsFilter(TokenStream stream) {
		super(stream);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.buffalo.cse.irf14.analysis.Analyzer#increment()
	 */
	@Override
	public boolean increment() throws TokenizerException {
		if (getStream().hasNext()) {
			Token token = getStream().next();
			if (token != null) {
				if (RulesHelper.stopWordsList.contains(token.getTermText())) {
					getStream().remove();
				}
				return true;
			}
		}
		return false;
	}

}
