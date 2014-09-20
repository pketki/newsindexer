/**
 * 
 */
package edu.buffalo.cse.irf14.analysis.rules;

import edu.buffalo.cse.irf14.analysis.Token;
import edu.buffalo.cse.irf14.analysis.TokenFilter;
import edu.buffalo.cse.irf14.analysis.TokenStream;
import edu.buffalo.cse.irf14.analysis.TokenizerException;

/**
 * @author jlimaye
 * 
 */
public class DateFilter extends TokenFilter {
	private final TokenStream stream;

	/**
	 * @param stream
	 */
	public DateFilter(TokenStream stream) {
		super(stream);
		this.stream = stream;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.buffalo.cse.irf14.analysis.Analyzer#increment()
	 */
	@Override
	public boolean increment() throws TokenizerException {
		if (stream.hasNext()) {
			Token token = stream.next();
			if (token != null) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.buffalo.cse.irf14.analysis.Analyzer#getStream()
	 */
	@Override
	public TokenStream getStream() {
		return stream;
	}

}
