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
public class SpecialCharsFilter extends TokenFilter {
	private final TokenStream stream;

	/**
	 * @param stream
	 */
	public SpecialCharsFilter(TokenStream stream) {
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
		// TODO Auto-generated method stub
		if (stream.hasNext()) {
			Token token = stream.next();
			if (token != null) {
				Pattern pattern = Pattern.compile("[^a-zA-Z0-9.-]");
				Matcher match = pattern.matcher(token.getTermText());
				if (match.find()) {
					token.setTermText(token.getTermText().replaceAll(
							"[^a-zA-Z0-9.-]", ""));
				}
				if (token.getTermText().trim().isEmpty()) {
					stream.remove();
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
