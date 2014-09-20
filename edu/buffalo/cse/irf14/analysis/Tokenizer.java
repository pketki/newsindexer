/**
 * 
 */
package edu.buffalo.cse.irf14.analysis;

import java.util.List;
import java.util.Scanner;

/**
 * @author nikhillo Class that converts a given string into a
 *         {@link TokenStream} instance
 */
public class Tokenizer {

	private final String delimiter;

	/**
	 * Default constructor. Assumes tokens are whitespace delimited
	 */
	public Tokenizer() {
		this.delimiter = " ";
	}

	/**
	 * Overloaded constructor. Creates the tokenizer with the given delimiter
	 * 
	 * @param delim
	 *            : The delimiter to be used
	 */
	public Tokenizer(String delim) {
		this.delimiter = delim;
	}

	/**
	 * Method to convert the given string into a TokenStream instance. This must
	 * only break it into tokens and initialize the stream. No other processing
	 * must be performed. Also the number of tokens would be determined by the
	 * string and the delimiter. So if the string were "hello world" with a
	 * whitespace delimited tokenizer, you would get two tokens in the stream.
	 * But for the same text used with lets say "~" as a delimiter would return
	 * just one token in the stream.
	 * 
	 * @param str
	 *            : The string to be consumed
	 * @return : The converted TokenStream as defined above
	 * @throws TokenizerException
	 *             : In case any exception occurs during tokenization
	 */
	public TokenStream consume(String str) throws TokenizerException {
		final TokenStream tokenStream = new TokenStream();
		final List<Token> tokenList = tokenStream.getTokenList();

		Scanner tokenReader = new Scanner(str);
		tokenReader.useDelimiter(this.delimiter);
		int position = 0;

		while (tokenReader.hasNext()) {
			tokenList.add(new Token(tokenReader.next(), position));
			position++;
		}
		return tokenStream;
	}
}
