/**
 * 
 */
package edu.buffalo.cse.irf14.analysis;

import edu.buffalo.cse.irf14.analysis.rules.AccentFilter;
import edu.buffalo.cse.irf14.analysis.rules.CapitalizationFilter;
import edu.buffalo.cse.irf14.analysis.rules.DateFilter;
import edu.buffalo.cse.irf14.analysis.rules.NumberFilter;
import edu.buffalo.cse.irf14.analysis.rules.SpecialCharsFilter;
import edu.buffalo.cse.irf14.analysis.rules.StemmerFilter;
import edu.buffalo.cse.irf14.analysis.rules.StopWordsFilter;
import edu.buffalo.cse.irf14.analysis.rules.SymbolFilter;

/**
 * Factory class for instantiating a given TokenFilter
 * 
 * @author nikhillo
 * 
 */
public class TokenFilterFactory {

	private static TokenFilterFactory tokenFilterFactory;
	private TokenFilter tokenFilter;

	/**
	 * Static method to return an instance of the factory class. Usually factory
	 * classes are defined as singletons, i.e. only one instance of the class
	 * exists at any instance. This is usually achieved by defining a private
	 * static instance that is initialized by the "private" constructor. On the
	 * method being called, you return the static instance. This allows you to
	 * reuse expensive objects that you may create during instantiation
	 * 
	 * @return An instance of the factory
	 */
	public static TokenFilterFactory getInstance() {
		if (tokenFilterFactory == null) {
			tokenFilterFactory = new TokenFilterFactory();
		}
		return tokenFilterFactory;
	}

	/**
	 * Returns a fully constructed {@link TokenFilter} instance for a given
	 * {@link TokenFilterType} type
	 * 
	 * @param type
	 *            : The {@link TokenFilterType} for which the
	 *            {@link TokenFilter} is requested
	 * @param stream
	 *            : The TokenStream instance to be wrapped
	 * @return The built {@link TokenFilter} instance
	 */
	public TokenFilter getFilterByType(TokenFilterType type, TokenStream stream) {
		// TODO : YOU MUST IMPLEMENT THIS METHOD
		switch (type) {
		case ACCENT:
			tokenFilter = new AccentFilter(stream);
			break;
		case CAPITALIZATION:
			tokenFilter = new CapitalizationFilter(stream);
			break;
		case DATE:
			tokenFilter = new DateFilter(stream);
			break;
		case NUMERIC:
			tokenFilter = new NumberFilter(stream);
			break;
		case SPECIALCHARS:
			tokenFilter = new SpecialCharsFilter(stream);
			break;
		case STEMMER:
			tokenFilter = new StemmerFilter(stream);
			break;
		case STOPWORD:
			tokenFilter = new StopWordsFilter(stream);
			break;
		case SYMBOL:
			tokenFilter = new SymbolFilter(stream);
			break;
		default:
			tokenFilter = null;
			break;
		}
		return tokenFilter;
	}
}
