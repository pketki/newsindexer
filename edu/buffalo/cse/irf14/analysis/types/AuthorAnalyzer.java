/**
 * 
 */
package edu.buffalo.cse.irf14.analysis.types;

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
		TokenFilter capitalFilter = factory.getFilterByType(
				TokenFilterType.CAPITALIZATION, this.getStream());

		capitalFilter.setChaining(true);

		return accentFilter.increment() && capitalFilter.increment();
	}

}
