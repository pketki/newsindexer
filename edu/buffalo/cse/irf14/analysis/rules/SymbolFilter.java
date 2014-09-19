/**
 * 
 */
package edu.buffalo.cse.irf14.analysis.rules;

import edu.buffalo.cse.irf14.analysis.Token;
import edu.buffalo.cse.irf14.analysis.TokenFilter;
import edu.buffalo.cse.irf14.analysis.TokenStream;
import edu.buffalo.cse.irf14.analysis.TokenizerException;

/**
 * @author ketkiram
 * 
 */
public class SymbolFilter extends TokenFilter {

	/**
	 * @param stream
	 */
	char[] endOfLineSymbols = { '.', '?', '!' };
	private final TokenStream stream;

	public SymbolFilter(TokenStream stream) {
		super(stream);
		this.stream = stream;
	}

	private String filterApostrophe(String input) {
		String result = RulesHelper.commonContractionsMap.get(input);

		// if it is not a common contraction simply remove the apostrophe
		if (result == null) {
			String[] parts = input.split("'");
			result = parts[0] + (parts.length > 1 ? parts[1] : "");
		}
		return result;
	}

	private String fiterHyphen(String input) {
		String result = null;
		if (input.matches(" ^\\w+\\-[0-9]+$")) {
			result = input;
		} else {
			result = input.replaceAll("-", "");
		}
		return result;
	}

	@Override
	public boolean increment() throws TokenizerException {
		Token token = null;
		String text = null;

		if (stream.hasNext()) {

			token = stream.next();
			text = token.getTermText();

			for (char symbol : endOfLineSymbols) {
				if (text.charAt(text.length() - 1) == symbol) {
					text.replace("" + symbol, "");
				}
			}

			if (text.contains("'")) {
				text = this.filterApostrophe(text);
			}

			if (text.contains("-")) {
				text = this.fiterHyphen(text);
			}

		}
		return true;
	}

	@Override
	public TokenStream getStream() {
		return this.stream;
	}

}
