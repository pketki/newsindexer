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

	public AccentFilter(TokenStream stream) {
		super(stream);
	}

	@Override
	public boolean increment() throws TokenizerException {
		if (getStream().hasNext()) {
			Token token = getStream().next();
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

}
