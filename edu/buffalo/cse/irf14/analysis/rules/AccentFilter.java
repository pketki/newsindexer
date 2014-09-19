package edu.buffalo.cse.irf14.analysis.rules;

import java.text.Normalizer;

import edu.buffalo.cse.irf14.analysis.Token;
import edu.buffalo.cse.irf14.analysis.TokenFilter;
import edu.buffalo.cse.irf14.analysis.TokenStream;
import edu.buffalo.cse.irf14.analysis.TokenizerException;

/**
 * @author jlimaye
 * 
 */
public class AccentFilter extends TokenFilter {
	private final TokenStream stream;

	public AccentFilter(TokenStream stream) {
		super(stream);
		this.stream = stream;
	}

	@Override
	public boolean increment() throws TokenizerException {
		if (stream.hasNext()) {
			Token token = stream.next();
			if (token != null) {
				String tokenString = Normalizer.normalize(token.getTermText(),
						Normalizer.Form.NFD).replaceAll(
						"\\p{InCombiningDiacriticalMarks}+", "");
				token.setTermText(tokenString);
				return true;
			}
		}
		return false;
	}

	@Override
	public TokenStream getStream() {
		return stream;
	}

}
