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
public class CapitalizationFilter extends TokenFilter {

	/**
	 * @param stream
	 */
	public CapitalizationFilter(TokenStream stream) {
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

			String regex = "[A-Z0-9]*$";
			if (token.isAllCaps())
				text = text.toLowerCase();
			else if (text.matches(regex)) {
				// check upto 2 next tokens for all caps
				getStream().savePoint();

				final Token next1 = getStream().next();
				if (next1 != null && next1.getTermText().matches(regex)) {
					final Token next2 = getStream().next();
					if (next2 != null && next2.getTermText().matches(regex)) {
						next1.setAllCaps(true);
						next2.setAllCaps(true);
						text = text.toLowerCase();
					}
				}
				getStream().rollBack();
			}

			Matcher camelCaseMatcher = RulesHelper.camelCase.matcher(text);
			int count = 0;
			while (camelCaseMatcher.find()) {
				count++;
			}
			// if its simply a capital case word check for adjacent
			if (count == 1 && Character.isUpperCase(text.charAt(0))) {
				getStream().savePoint();
				final Token next = getStream().next();
				String nextText;
				if (next != null
						&& (nextText = next.getTermText())
								.matches("[A-Z][a-z0-9]*")) {

					token.merge(next);
					getStream().remove();
					text = token.getTermText();

				} else {
					getStream().rollBack();
					getStream().savePoint();
					if (getStream().hasPrevious()) {
						nextText = getStream().previous().getTermText();
						if (nextText.charAt(nextText.length() - 1) == '.')
							text = text.toLowerCase();
						getStream().rollBack();
					} else {
						text = text.toLowerCase();
					}
				}
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
