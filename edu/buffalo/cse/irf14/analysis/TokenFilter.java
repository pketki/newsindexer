/**
 * 
 */
package edu.buffalo.cse.irf14.analysis;

/**
 * The abstract class that you must extend when implementing your TokenFilter
 * rule implementations. Apart from the inherited Analyzer methods, we would use
 * the inherited constructor (as defined here) to test your code.
 * 
 * @author nikhillo
 * 
 */
public abstract class TokenFilter implements Analyzer {

	private final TokenStream stream;
	private boolean chaining;

	/**
	 * Default constructor, creates an instance over the given TokenStream
	 * 
	 * @param stream
	 *            : The given TokenStream instance
	 */
	public TokenFilter(TokenStream stream) {
		this.stream = stream;
		this.chaining = false;
	}

	/**
	 * @return the chaining
	 */
	public boolean isChaining() {
		return chaining;
	}

	/**
	 * @param chaining
	 *            the chaining to set
	 */
	public void setChaining(boolean chaining) {
		this.chaining = chaining;
	}

	/**
	 * @return the stream
	 */
	@Override
	public TokenStream getStream() {
		return this.stream;
	}
}
