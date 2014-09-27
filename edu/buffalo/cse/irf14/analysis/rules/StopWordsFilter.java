/**
 * 
 */
package edu.buffalo.cse.irf14.analysis.rules;

import edu.buffalo.cse.irf14.IndexHelper;
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

	@Override
	public boolean increment() throws TokenizerException {
		Token token = null;

		if (this.isChaining())
			token = getStream().getCurrent();
		if (token == null && getStream().hasNext()) {
			token = getStream().next();
		}

		if (token != null) {
			if (IndexHelper.stopWordsList.contains(token.getTermText()
					.toLowerCase())) {
				getStream().remove();
			}
			return true;
		}

		return false;
	}

}
