/**
 * 
 */
package edu.buffalo.cse.irf14.analysis.types;

import edu.buffalo.cse.irf14.analysis.Token;
import edu.buffalo.cse.irf14.analysis.TokenFilter;
import edu.buffalo.cse.irf14.analysis.TokenFilterType;
import edu.buffalo.cse.irf14.analysis.TokenStream;
import edu.buffalo.cse.irf14.analysis.TokenizerException;

/**
 * @author ketkiram
 * 
 */
public class AuthorAnalyzer extends FieldAnalyzer {

	public AuthorAnalyzer(TokenStream stream) {
		super(stream);
	}

	@Override
	public boolean increment() throws TokenizerException {
		TokenFilter accentFilter = factory.getFilterByType(
				TokenFilterType.ACCENT, this.getStream());

		accentFilter.increment();

		Token token = getStream().getCurrent();
		if (token == null && getStream().hasNext()) {
			token = getStream().next();
		}
		if (token != null) {
			if (getStream().hasNext()) {
				token.merge(getStream().next());
				getStream().remove();
			}
			token.setTermText(token.getTermText().toLowerCase());
			return true;
		}
		return false;
	}
}
