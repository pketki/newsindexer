/**
 * 
 */
package edu.buffalo.cse.irf14.analysis.rules;

import java.util.regex.Matcher;

import edu.buffalo.cse.irf14.analysis.Token;
import edu.buffalo.cse.irf14.analysis.TokenFilter;
import edu.buffalo.cse.irf14.analysis.TokenStream;
import edu.buffalo.cse.irf14.analysis.TokenizerException;

/**
 * @author ketkiram
 * 
 */
public class NumberFilter extends TokenFilter {

	/**
	 * @param stream
	 */
	public NumberFilter(TokenStream stream) {
		super(stream);

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
			Matcher symbolMatcher = IndexHelper.numeric.matcher(text);
			while (symbolMatcher.find()) {
				text = text.replace(symbolMatcher.group(0), symbolMatcher
						.group(0).replaceAll("\\d+[.,\\-]?\\d+", ""));
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
