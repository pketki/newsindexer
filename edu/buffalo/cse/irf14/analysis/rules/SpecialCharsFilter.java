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
		String text = null;

		if (this.isChaining())
			token = getStream().getCurrent();
		if (token == null && getStream().hasNext()) {
			token = getStream().next();
		}

		if (token != null) {
			text = token.getTermText();
			text = text.replaceAll("[\"$&+,:;=@#|<>^*()%~{}_/\\\\]", "");

			if (text.contains("-")) {
				text = IndexHelper.filterHyphen(text);
			}

			if (text.equals(""))
				getStream().remove();
			else
				token.setTermText(text);
			return true;
		}

		return false;
	}

}
