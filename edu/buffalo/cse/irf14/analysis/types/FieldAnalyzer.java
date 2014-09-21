/**
 * 
 */
package edu.buffalo.cse.irf14.analysis.types;

import edu.buffalo.cse.irf14.analysis.Analyzer;
import edu.buffalo.cse.irf14.analysis.TokenFilterFactory;
import edu.buffalo.cse.irf14.analysis.TokenStream;

/**
 * @author Dell
 * 
 */
public abstract class FieldAnalyzer implements Analyzer {

	private final TokenStream stream;
	protected final TokenFilterFactory factory;

	/**
	 * 
	 */
	public FieldAnalyzer(TokenStream stream) {
		factory = TokenFilterFactory.getInstance();
		this.stream = stream;
	}

	@Override
	public TokenStream getStream() {
		return this.stream;
	}
}
