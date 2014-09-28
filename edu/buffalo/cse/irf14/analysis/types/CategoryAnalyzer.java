/**
 * 
 */
package edu.buffalo.cse.irf14.analysis.types;

import edu.buffalo.cse.irf14.analysis.TokenStream;
import edu.buffalo.cse.irf14.analysis.TokenizerException;

/**
 * @author ketkiram
 * 
 */
public class CategoryAnalyzer extends FieldAnalyzer {

	/**
	 * 
	 */
	public CategoryAnalyzer(TokenStream stream) {
		super(stream);
	}

	@Override
	public boolean increment() throws TokenizerException {
		return false;
	}

}
