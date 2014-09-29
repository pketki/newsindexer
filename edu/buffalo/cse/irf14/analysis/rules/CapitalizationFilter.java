/**
 * 
 */
package edu.buffalo.cse.irf14.analysis.rules;

import java.util.ArrayList;
import java.util.List;
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
				getStream().savePoint();

				if (getStream().hasPrevious()
						&& getStream().previous().isAllCaps())
					text = text.toLowerCase();
				else {
					// check upto 2 next tokens for all caps
					final Token next1 = getStream().next();
					if (next1 != null && next1.getTermText().matches(regex)) {
						if (getStream().hasNext()) {
							final Token next2 = getStream().next();
							if (next2.getTermText().matches(regex)) {
								next1.setAllCaps(true);
								next2.setAllCaps(true);
								text = text.toLowerCase();
							}
						} else {
							next1.setAllCaps(true);
							text = text.toLowerCase();
						}
					}
				}
				getStream().rollBack();
			}

			Matcher camelCaseMatcher = IndexHelper.camelCase.matcher(text);
			if (camelCaseMatcher.matches()) {
				getStream().savePoint();
				final List<Token> mergeList = new ArrayList<Token>();
				Token next;
				String nextText;
				while (getStream().hasNext()) {
					next = getStream().next();
					nextText = next.getTermText();
					if (nextText.matches(IndexHelper.camelCase.pattern())) {
						mergeList.add(next);
						getStream().remove();
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
						break;
					}
				}
				if (mergeList.size() > 0) {
					token.merge(mergeList.toArray(new Token[mergeList.size()]));
					text = token.getTermText();
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
