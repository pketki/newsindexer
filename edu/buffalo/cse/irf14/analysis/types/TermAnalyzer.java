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
public class TermAnalyzer extends FieldAnalyzer {

	/**
	 * 
	 */
	public TermAnalyzer(TokenStream stream) {
		super(stream);
	}

	@Override
	public boolean increment() throws TokenizerException {
		TokenFilter symbolFilter = factory.getFilterByType(
				TokenFilterType.SYMBOL, getStream());
		TokenFilter accentFilter = factory.getFilterByType(
				TokenFilterType.ACCENT, this.getStream());
		TokenFilter specialCharFilter = factory.getFilterByType(
				TokenFilterType.SPECIALCHARS, getStream());
		TokenFilter dateFilter = factory.getFilterByType(TokenFilterType.DATE,
				getStream());
		TokenFilter numberFilter = factory.getFilterByType(
				TokenFilterType.NUMERIC, getStream());
		TokenFilter capitalFilter = factory.getFilterByType(
				TokenFilterType.CAPITALIZATION, this.getStream());
		TokenFilter stemmerFilter = factory.getFilterByType(
				TokenFilterType.STEMMER, getStream());
		TokenFilter stopwordFilter = factory.getFilterByType(
				TokenFilterType.STOPWORD, getStream());

		accentFilter.setChaining(true);
		specialCharFilter.setChaining(true);
		dateFilter.setChaining(true);
		numberFilter.setChaining(true);
		symbolFilter.setChaining(true);
		stemmerFilter.setChaining(true);
		stopwordFilter.setChaining(true);

		return capitalFilter.increment() && symbolFilter.increment()
				&& accentFilter.increment() && specialCharFilter.increment()
				&& dateFilter.increment() && numberFilter.increment()
				&& stemmerFilter.increment() && stopwordFilter.increment();
	}
}
