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
public class PlaceAnalyzer extends FieldAnalyzer {

	/**
	 * 
	 */
	public PlaceAnalyzer(TokenStream stream) {
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
		TokenFilter capitalFilter = factory.getFilterByType(
				TokenFilterType.CAPITALIZATION, this.getStream());

		accentFilter.setChaining(true);
		specialCharFilter.setChaining(true);
		capitalFilter.setChaining(true);

		return symbolFilter.increment() && accentFilter.increment()
				&& specialCharFilter.increment() && capitalFilter.increment();
	}

}
