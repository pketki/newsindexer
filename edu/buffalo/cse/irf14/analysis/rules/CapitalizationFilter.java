/**
 * 
 */
package edu.buffalo.cse.irf14.analysis.rules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.buffalo.cse.irf14.analysis.Token;
import edu.buffalo.cse.irf14.analysis.TokenFilter;
import edu.buffalo.cse.irf14.analysis.TokenStream;
import edu.buffalo.cse.irf14.analysis.TokenizerException;

/**
 * @author jlimaye
 * 
 */
public class CapitalizationFilter extends TokenFilter {
	private final TokenStream stream;

	/**
	 * @param stream
	 */
	public CapitalizationFilter(TokenStream stream) {
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
				Pattern pattern = Pattern.compile("[a-z]+[A-Z]+");
				Matcher match = pattern.matcher(token.getTermText());
				if (!match.find()) {
					if (!token.getTermText().equals(
							token.getTermText().toUpperCase())) {
						token.setTermText(token.getTermText().toLowerCase());
					} else if (!Character.isLowerCase(token.getTermText()
							.charAt(0))) {
						token.setTermText(token.getTermText().toLowerCase());
					}
				}
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
