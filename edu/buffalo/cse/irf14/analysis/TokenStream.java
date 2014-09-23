/**
 * 
 */
package edu.buffalo.cse.irf14.analysis;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * @author nikhillo Class that represents a stream of Tokens. All
 *         {@link Analyzer} and {@link TokenFilter} instances operate on this to
 *         implement their behavior
 */
public class TokenStream implements Iterator<Token> {

	private List<Token> tokenList;
	private int position;
	private int size;
	private int savePointer;

	/**
	 * 
	 */
	public TokenStream() {
		super();
		setTokenList(new Vector<Token>());
		position = -1;
		size = 0;
	}

	/**
	 * Method that checks if there is any Token left in the stream with regards
	 * to the current pointer. DOES NOT ADVANCE THE POINTER
	 * 
	 * @return true if at least one Token exists, false otherwise
	 */
	@Override
	public boolean hasNext() {
		return (!tokenList.isEmpty() && position < (size - 1));
	}

	/**
	 * Method to return the next Token in the stream. If a previous hasNext()
	 * call returned true, this method must return a non-null Token. If for any
	 * reason, it is called at the end of the stream, when all tokens have
	 * already been iterated, return null
	 */
	@Override
	public Token next() {
		if (this.hasNext()) {
			this.position++;
			return tokenList.get(position);
		}
		return null;
	}

	public boolean hasPrevious() {
		return (!tokenList.isEmpty() && position > 0);
	}

	public Token previous() {
		if (this.hasPrevious()) {
			this.position--;
			return tokenList.get(position);
		}
		return null;
	}

	/**
	 * Method to remove the current Token from the stream. Note that "current"
	 * token refers to the Token just returned by the next method. Must thus be
	 * NO-OP when at the beginning of the stream or at the end
	 */
	@Override
	public void remove() {
		// check we're not at the beginning of the token list
		if (!tokenList.isEmpty() && position >= 0) {
			tokenList.remove(position);
			position--;
			size = tokenList.size();
		}
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
		if (stream != null && stream.getSize() > 0) {
			stream.reset();
			while (stream.hasNext()) {
				tokenList.add(stream.next());
			}
			size += stream.getSize();
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
		return (position < 0 ? null : tokenList.get(position));
	}

	/**
	 * @param tokenList
	 *            the tokenList to set
	 */
	public void setTokenList(List<Token> tokenList) {
		this.tokenList = tokenList;
		size = tokenList.size();
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	public void savePoint() {
		savePointer = position;
	}

	public void rollBack() {
		position = savePointer;
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
