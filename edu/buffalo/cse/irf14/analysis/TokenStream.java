/**
 * 
 */
package edu.buffalo.cse.irf14.analysis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author nikhillo Class that represents a stream of Tokens. All
 *         {@link Analyzer} and {@link TokenFilter} instances operate on this to
 *         implement their behavior
 */
public class TokenStream implements Iterator<Token> {

	private List<Token> tokenList;
	private int position;

	/**
	 * 
	 */
	public TokenStream() {
		super();
		this.setTokenList(new ArrayList<Token>());
		this.position = -1;
	}

	/**
	 * Method that checks if there is any Token left in the stream with regards
	 * to the current pointer. DOES NOT ADVANCE THE POINTER
	 * 
	 * @return true if at least one Token exists, false otherwise
	 */
	@Override
	public boolean hasNext() {
		return (this.position < this.tokenList.size());
	}

	/**
	 * Method to return the next Token in the stream. If a previous hasNext()
	 * call returned true, this method must return a non-null Token. If for any
	 * reason, it is called at the end of the stream, when all tokens have
	 * already been iterated, return null
	 */
	@Override
	public Token next() {
		this.position++;
		if (this.hasNext())
			return this.tokenList.get(position);
		return null;
	}

	/**
	 * Method to remove the current Token from the stream. Note that "current"
	 * token refers to the Token just returned by the next method. Must thus be
	 * NO-OP when at the beginning of the stream or at the end
	 */
	@Override
	public void remove() {
		List<Token> tempList = new ArrayList<Token>();
		for (int i = 0; i < this.tokenList.size(); i++) {
			if (i != this.position)
				tempList.add(this.tokenList.get(i));
		}
		this.tokenList = tempList;
		this.position++;
	}

	/**
	 * Method to reset the stream to bring the iterator back to the beginning of
	 * the stream. Unless the stream has no tokens, hasNext() after calling
	 * reset() must always return true.
	 */
	public void reset() {
		this.position = -1;
	}

	/**
	 * Method to append the given TokenStream to the end of the current stream
	 * The append must always occur at the end irrespective of where the
	 * iterator currently stands. After appending, the iterator position must be
	 * unchanged Of course this means if the iterator was at the end of the
	 * stream and a new stream was appended, the iterator hasn't moved but that
	 * is no longer the end of the stream.
	 * 
	 * @param stream
	 *            : The stream to be appended
	 */
	public void append(TokenStream stream) {
		while (stream.hasNext()) {
			this.tokenList.add(stream.next());
		}
	}

	/**
	 * Method to get the current Token from the stream without iteration. The
	 * only difference between this method and {@link TokenStream#next()} is
	 * that the latter moves the stream forward, this one does not. Calling this
	 * method multiple times would not alter the return value of
	 * {@link TokenStream#hasNext()}
	 * 
	 * @return The current {@link Token} if one exists, null if end of stream
	 *         has been reached or the current Token was removed
	 */
	public Token getCurrent() {
		return (position < 0 ? null : this.tokenList.get(position));
	}

	/**
	 * @return the tokenList
	 */
	public List<Token> getTokenList() {
		return tokenList;
	}

	/**
	 * @param tokenList
	 *            the tokenList to set
	 */
	public void setTokenList(List<Token> tokenList) {
		this.tokenList = tokenList;
	}

	@Override
	public String toString() {
		StringBuffer retVal = new StringBuffer("TokenStream : ");
		for (Token t : tokenList) {
			retVal.append(t.getTermText() + " | ");
		}
		return retVal.toString();
	}
}
